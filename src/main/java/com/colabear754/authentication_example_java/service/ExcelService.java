package com.colabear754.authentication_example_java.service;

import ch.qos.logback.core.util.ContentTypeUtil;
import com.colabear754.authentication_example_java.Util.ExcelUtil;
import com.colabear754.authentication_example_java.handler.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.internal.ContentType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExcelService {
    public boolean importExcel(MultipartFile file) throws IOException, InvalidFormatException {

        // NOTE 파일이 존재하지 않는 경우
        if (file.isEmpty()) {throw new BadRequestException("파일이 존재하지 않습니다.");}

        // NOTE 파일의 확장자 검사 엑셀 파일만 가능
        String contentType = file.getContentType();
        System.out.println(contentType);
        if (!contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            if(contentType.equals("text/csv")){
                File xlsxFile = ExcelUtil.convertCsvFileTOXlsxFile(file); // MEMO csv 파일을 xlsx파일로 변환
                Path dest = Paths.get("my_file.xlsx"); // MEMO 파일 저장
                Files.copy(xlsxFile.toPath(), dest);
            } else {
                throw new BadRequestException("파일 형식이 올바르지 않습니다.");
            }
        }

        Map<String, Object> nameMap = ExcelUtil.getFirstExcelData(file , 0);
        List<Map<String, Object>> listMap = ExcelUtil.getListExcelData(file , 0);

        // NOTE 1행의 이름 데이터만 가져오기
        for (int i = 0; i < nameMap.size(); i++) {
//            System.out.println(nameMap.get(String.valueOf(i)));
        }

        // NOTE 2행부터 데이터 전부 가져오기
        for (int i = 0; i < listMap.size(); i++) {
//            System.out.println(listMap.get(i));
            for (int j = 0; j < listMap.get(i).size(); j++) {
                System.out.println(listMap.get(i).get(String.valueOf(j)));
            }
        }

//        for (int i = 0; i < listMap.size(); i++) {
//            Map<String, Object> dataMap = listMap.get(i);
//            YourEntity entity = new YourEntity();
//
//            // 순서에 따라 엔티티 값 동적으로 설정
//            for (int j = 0; j < dataMap.size(); j++) {
//                String value = (String) dataMap.get(String.valueOf(j));
//                setEntityValueBasedOnOrder(entity, j, value);
//            }
//
//            // 엔티티 저장 또는 추가 처리
//            yourEntityRepository.save(entity);
//        }

        return true;
    }



}
