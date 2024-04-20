package com.colabear754.authentication_example_java.DTO;

import com.colabear754.authentication_example_java.common.ApiStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

@RequiredArgsConstructor
@Getter
@Setter
public class ApiResponseDTO {

    private final  int code;
    private final  String message;
    private final  Object data;

    public static ApiResponseDTO success(Object data) {
        return new ApiResponseDTO(HttpStatus.OK.value(), ApiStatus.SUCCESS.toString(), data);
    }

    public static ApiResponseDTO error(String message) {
        return new ApiResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, new ArrayList<>());
    }
}
