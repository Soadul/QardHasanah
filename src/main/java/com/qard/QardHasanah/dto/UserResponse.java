package com.qard.QardHasanah.dto;

import com.qard.QardHasanah.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User response object")
public class UserResponse {

    @Schema(description = "User's unique ID", example = "1")
    private Long id;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "User's role", example = "DEPOSITOR")
    private Role role;

    @Schema(description = "User account active status", example = "true")
    private Boolean isActive;

    @Schema(description = "Account creation timestamp", example = "1708851476000")
    private Long createdAt;

    @Schema(description = "Account last update timestamp", example = "1708851476000")
    private Long updatedAt;
}

