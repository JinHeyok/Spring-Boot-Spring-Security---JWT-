package com.colabear754.authentication_example_java.controller;

import com.colabear754.authentication_example_java.DTO.ApiResponse;
import com.colabear754.authentication_example_java.service.ZoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "ZOOM API 테스트")
@RequestMapping("/zoom")
@RestController
@RequiredArgsConstructor
public class ZoomController {


    private final ZoomService zoomService;


    // NOTE zoom 처음 code발금 url endpoint에 URL을 해당 API로 지정한다.
    @GetMapping(value="/code")
    public ApiResponse meetingRoomRegistration(HttpServletRequest req, @RequestParam String code) throws IOException {
        System.out.println("code: "+ code);
        return ApiResponse.success(zoomService.meetingRoomRegistration(code));
    }


}
