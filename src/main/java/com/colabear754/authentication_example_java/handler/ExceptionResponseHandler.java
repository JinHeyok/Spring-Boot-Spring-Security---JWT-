package com.colabear754.authentication_example_java.handler;

import com.colabear754.authentication_example_java.DTO.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionResponseHandler {
    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<ApiResponseDTO> handleIllegalArgumentException(Exception e) {
        return ResponseEntity.badRequest().body(ApiResponseDTO.error(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO> handleAccessDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseDTO.error("접근이 거부되었습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleException() {
        return ResponseEntity.internalServerError().body(ApiResponseDTO.error("서버에 문제가 발생했습니다."));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponseDTO> handleBadRequestException(BadRequestException e){
        return ResponseEntity.internalServerError().body(ApiResponseDTO.error(e.getMessage()));
    }
}
