package com.colabear754.authentication_example_java.DTO.sign_up.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignUpRequest{
        @Schema(description = "회원 아이디", example = "admin")
        String account;
        @Schema(description = "회원 비밀번호", example = "admin")
        String password;
        @Schema(description = "회원 이름", example = "관리자")
        String name;
        @Schema(description = "회원 나이", example = "28")
        Integer age;
}
