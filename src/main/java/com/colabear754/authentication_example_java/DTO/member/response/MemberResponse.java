package com.colabear754.authentication_example_java.DTO.member.response;

import com.colabear754.authentication_example_java.common.MemberType;
import com.colabear754.authentication_example_java.DTO.DateResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(name = "Member Response DTO" , description = "Member Response DTO")
public class MemberResponse extends DateResponseDTO {
        @Schema(description = "회원 고유키", example = "c0a80121-7aeb-4b4b-8b0a-6b1c032f0e4a")
        Long id;
        @Schema(description = "회원 아이디", example = "colabear754")
        String account;
        @Schema(description = "회원 이름", example = "콜라곰")
        String name;
        @Schema(description = "회원 나이", example = "30")
        Integer age;
        @Schema(description = "회원 타입", example = "USER")
        MemberType type;
}
