package com.colabear754.authentication_example_java.controller;


import com.colabear754.authentication_example_java.DTO.AbstractDTO;
import com.colabear754.authentication_example_java.DTO.ApiResponse;
import com.colabear754.authentication_example_java.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/excel")
public class ExcelController {



    @Autowired
    private ExcelService excelService;

    @PostMapping(value = "/importExcel")
    public ApiResponse importExcel(MultipartFile file){

        boolean response = excelService.importExcel(file);

        return ApiResponse.success(response);
    }



}
