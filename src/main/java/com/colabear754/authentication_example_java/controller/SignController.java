package com.colabear754.authentication_example_java.controller;

import com.colabear754.authentication_example_java.DTO.ApiResponseDTO;
import com.colabear754.authentication_example_java.DTO.Request.SignInRequest;
import com.colabear754.authentication_example_java.DTO.Request.SignUpRequest;
import com.colabear754.authentication_example_java.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 가입 및 로그인")
@RequiredArgsConstructor
@RestController
@RequestMapping
public class SignController {
    private final SignService signService;

    @Operation(summary = "회원 가입")
    @PostMapping("/sign-up")
    public ApiResponseDTO signUp(@RequestBody SignUpRequest request) {
        return ApiResponseDTO.success(signService.registerMember(request));
    }

    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    public ApiResponseDTO signIn(@RequestBody SignInRequest request) {
        return ApiResponseDTO.success(signService.signIn(request));
    }
}
