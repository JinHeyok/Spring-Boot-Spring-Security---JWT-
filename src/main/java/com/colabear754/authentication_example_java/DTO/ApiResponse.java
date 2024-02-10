package com.colabear754.authentication_example_java.DTO;

import com.colabear754.authentication_example_java.common.ApiStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiResponse{

    private final  ApiStatus status;
    private final  String message;
    private final  Object data;

    public static ApiResponse success(Object data) {
        return new ApiResponse(ApiStatus.SUCCESS, null, data);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(ApiStatus.ERROR, message, null);
    }
}
