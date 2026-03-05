package com.qard.QardHasanah.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Generic API response wrapper")
public class ApiResponse<T> {

    @Schema(description = "Response message", example = "User registered successfully")
    private String message;

    @Schema(description = "Response data payload")
    private T data;

    @Schema(description = "Success status", example = "true")
    private Boolean success;

    @Schema(description = "HTTP status code", example = "201")
    private Integer statusCode;
}

