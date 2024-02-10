package com.colabear754.authentication_example_java.DTO.sign_in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SignInRequest {
    @Schema(description = "회원 아이디", example = "admin")
    String account;
    @Schema(description = "회원 비밀번호", example = "admin")
    String password;
}
