package com.colabear754.authentication_example_java.DTO.Response;

import com.colabear754.authentication_example_java.DTO.DateResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public class SignUpResponse extends DateResponseDTO {
    @Schema(description = "회원 고유키", example = "c0a80121-7aeb-4b4b-8b7a-9b9b9b9b9b9b")
    Long Id;
    @Schema(description = "회원 아이디", example = "colabear754")
    String account;
    @Schema(description = "회원 이름", example = "콜라곰")
    String name;
    @Schema(description = "회원 나이", example = "30")
    Integer age;
}
