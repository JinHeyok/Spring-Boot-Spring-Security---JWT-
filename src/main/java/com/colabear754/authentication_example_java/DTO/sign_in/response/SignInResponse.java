package com.colabear754.authentication_example_java.DTO.sign_in.response;

import com.colabear754.authentication_example_java.common.MemberType;
import com.colabear754.authentication_example_java.DTO.DateResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SignInResponse {
    @Schema(description = "회원 이름", example = "콜라곰")
    String name;
    @Schema(description = "회원 유형", example = "USER")
    MemberType type;
    @Schema(description = "JWT Token", example = "TOKEN")
    String token; //note Token 추가
}
