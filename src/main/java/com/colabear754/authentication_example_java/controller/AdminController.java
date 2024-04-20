package com.colabear754.authentication_example_java.controller;

import com.colabear754.authentication_example_java.common.MemberType;
import com.colabear754.authentication_example_java.DTO.ApiResponseDTO;
import com.colabear754.authentication_example_java.security.AllAuthorize;
import com.colabear754.authentication_example_java.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자용 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@AllAuthorize
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "회원 목록 조회")
    @GetMapping("/members")
    public ApiResponseDTO getAllMembers(@RequestParam MemberType memberType) {
        return ApiResponseDTO.success(adminService.getMembers(memberType));
    }

}
