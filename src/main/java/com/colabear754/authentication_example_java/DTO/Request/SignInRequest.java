package com.colabear754.authentication_example_java.DTO.Request;

import com.colabear754.authentication_example_java.DTO.BaseRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SignInRequest extends BaseRequestDTO {
    @Schema(description = "회원 아이디", example = "admin")
    String account;
    @Schema(description = "회원 비밀번호", example = "admin")
    String password;
}
