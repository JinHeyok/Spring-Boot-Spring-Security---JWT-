package com.colabear754.authentication_example_java.DTO.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "SignUpRequest" ,description = "회원가입 Request 객체")
public class SignUpRequest extends BaseRequestDTO {
        @Schema(name = "account" , description = "회원 아이디", example = "admin")
        private String account;
        @Schema(name = "password" , description = "회원 비밀번호", example = "admin")
        private String password;
        @Schema(name = "name" , description = "회원 이름", example = "관리자")
        private String name;
        @Schema(name = "age" ,description = "회원 나이", example = "28")
        private Integer age;
}
