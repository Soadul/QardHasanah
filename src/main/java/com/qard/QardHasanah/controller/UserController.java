package com.qard.QardHasanah.controller;

import com.qard.QardHasanah.dto.ApiResponse;
import com.qard.QardHasanah.dto.AuthResponse;
import com.qard.QardHasanah.dto.LoginRequest;
import com.qard.QardHasanah.dto.RegisterRequest;
import com.qard.QardHasanah.dto.UserResponse;
import com.qard.QardHasanah.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "User Management", description = "APIs for user registration, email verification, login, and management")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Register a new user
     * POST /api/users/register
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account and sends email verification link")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or email already in use")
    })
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse userResponse = userService.register(request);
        ApiResponse<UserResponse> response = new ApiResponse<>(
                "User registered successfully. Please verify your email before login.",
                userResponse,
                true,
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Verify user email
     * GET /api/users/verify-email
     */
    @GetMapping("/verify-email")
    @Operation(summary = "Verify email", description = "Verifies user email with token sent to inbox")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        ApiResponse<String> response = new ApiResponse<>(
                "Email verified successfully. You can now login.",
                null,
                true,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Login user
     * POST /api/users/login
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates user with email and password, returns JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = userService.login(request);
        ApiResponse<AuthResponse> response = new ApiResponse<>(
                "Login successful",
                authResponse,
                true,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Get all users
     * GET /api/users
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        try {
            List<UserResponse> users = userService.getAllUsers();
            ApiResponse<List<UserResponse>> response = new ApiResponse<>(
                    "Users retrieved successfully",
                    users,
                    true,
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<UserResponse>> response = new ApiResponse<>(
                    "Error retrieving users",
                    null,
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get user by ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        try {
            UserResponse userResponse = userService.getUserById(id);
            ApiResponse<UserResponse> response = new ApiResponse<>(
                    "User retrieved successfully",
                    userResponse,
                    true,
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UserResponse> response = new ApiResponse<>(
                    e.getMessage(),
                    null,
                    false,
                    HttpStatus.NOT_FOUND.value()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Update user details
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update user details", description = "Updates user's first name, last name, and password")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody RegisterRequest request) {
        try {
            UserResponse userResponse = userService.updateUser(id, request);
            ApiResponse<UserResponse> response = new ApiResponse<>(
                    "User updated successfully",
                    userResponse,
                    true,
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UserResponse> response = new ApiResponse<>(
                    e.getMessage(),
                    null,
                    false,
                    HttpStatus.NOT_FOUND.value()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Delete user by ID
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user account permanently")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<String>> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        try {
            userService.deleteUser(id);
            ApiResponse<String> response = new ApiResponse<>(
                    "User deleted successfully",
                    null,
                    true,
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<String> response = new ApiResponse<>(
                    e.getMessage(),
                    null,
                    false,
                    HttpStatus.NOT_FOUND.value()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Deactivate user account
     * POST /api/users/{id}/deactivate
     */
    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user account", description = "Disables a user account without deleting it")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User deactivated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<UserResponse>> deactivateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        try {
            UserResponse userResponse = userService.deactivateUser(id);
            ApiResponse<UserResponse> response = new ApiResponse<>(
                    "User deactivated successfully",
                    userResponse,
                    true,
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UserResponse> response = new ApiResponse<>(
                    e.getMessage(),
                    null,
                    false,
                    HttpStatus.NOT_FOUND.value()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Activate user account
     * POST /api/users/{id}/activate
     */
    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate user account", description = "Enables a previously deactivated user account")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User activated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<UserResponse>> activateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        try {
            UserResponse userResponse = userService.activateUser(id);
            ApiResponse<UserResponse> response = new ApiResponse<>(
                    "User activated successfully",
                    userResponse,
                    true,
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UserResponse> response = new ApiResponse<>(
                    e.getMessage(),
                    null,
                    false,
                    HttpStatus.NOT_FOUND.value()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

