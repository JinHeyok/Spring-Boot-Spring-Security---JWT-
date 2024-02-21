package com.colabear754.authentication_example_java.service;

import ch.qos.logback.core.util.ContentTypeUtil;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExcelService {
    public boolean importExcel(MultipartFile file) {

        // note 파일이 존재하지 않는 경우
        if (file.isEmpty()) {
            throw new BadRequestException("파일이 존재하지 않습니다.");
        }

        // note 파일의 확장자 검사 엑셀 파일만 가능
        String contentType = file.getContentType();
        if (!contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            throw new BadRequestException("파일 형식이 올바르지 않습니다.");
        }


        Map<String, Object> nameMap = getFirstExcelData(file);
        List<Map<String, Object>> listMap = getListExcelData(file);

        // note 1행의 이름 데이터만 가져오기
        for (int i = 0; i < nameMap.size(); i++) {
            System.out.println(nameMap.get(String.valueOf(i)));
        }

        // note 2행부터 데이터 전부 가져오기
        for (int i = 0; i < listMap.size(); i++) {
            System.out.println(listMap.get(i));
            for (int j = 0; j < listMap.get(i).size(); j++) {
                System.out.println(listMap.get(i).get(String.valueOf(j)));
            }
        }
        return true;
    }


    // 각 셀의 데이터타입에 맞게 값 가져오기
    public String getCellValue(XSSFCell cell) {

        String value = "";

        if (cell == null) {
            return value;
        }

        switch (cell.getCellType()) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 숫자이지만 날짜 형식일 경우
                    value = String.valueOf(cell.getDateCellValue()); // 날짜 값을 얻거나 다른 형식으로 변환
                } else {
                    if (cell.getCellStyle().getDataFormatString().contains("%")) {
                        // 백분율 형식일 경우
                        double numericValue = cell.getNumericCellValue() * 100;
                        value = String.format("%.2f%%", numericValue);
                    } else {
                        // 일반 숫자일 경우
                        value = String.valueOf((int) cell.getNumericCellValue());
                    }
                }
            default:
                break;
        }
        return value;
    }

    // 엑셀파일의 데이터 목록 가져오기 (파라미터들은 위에서 설명함)
    public List<Map<String, Object>> getListExcelData(MultipartFile file) {

        List<Map<String, Object>> excelList = new ArrayList<>();

        try {
            OPCPackage opcPackage = OPCPackage.open(file.getInputStream());

            @SuppressWarnings("resource")
            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

            // 첫번째 시트
            XSSFSheet sheet = workbook.getSheetAt(0);

            int rowIndex = 0;
            int columnIndex = 0;

            // 첫번째 행(0)은 컬럼 명이기 때문에 두번째 행(1) 부터 검색
            for (rowIndex = 1; rowIndex < sheet.getLastRowNum() + 1; rowIndex++) {
                XSSFRow row = sheet.getRow(rowIndex);

                // 빈 행은 Skip
                if (row.getCell(0) != null && !row.getCell(0).toString().isBlank()) {

                    Map<String, Object> map = new HashMap<>();

                    int cells = row.getLastCellNum();

                    for (columnIndex = 0; columnIndex < cells; columnIndex++) {
                        XSSFCell cell = row.getCell(columnIndex);
                        map.put(String.valueOf(columnIndex), getCellValue(cell));
//                        log.info(rowIndex + " 행 : " + columnIndex + " 열 = " + getCellValue(cell));
                    }

                    excelList.add(map);
                }
            }

        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return excelList;
    }

    public Map<String, Object> getFirstExcelData(MultipartFile file) {

        Map<String, Object> map = new HashMap<>();

        try {
            OPCPackage opcPackage = OPCPackage.open(file.getInputStream());
            @SuppressWarnings("resource")
            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

            // 첫번째 시트
            XSSFSheet sheet = workbook.getSheetAt(0);

            // 첫번째 행(0)은 컬럼 명만 검색
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                XSSFRow row = sheet.getRow(rowIndex);

                // 빈 행은 Skip
                if (row.getCell(0) != null && !row.getCell(0).toString().isBlank()) {

                    // 첫 번째 행에서 열의 수를 가져오기 (열의 수는 0번째 셀부터 시작)
                    int cells = row.getLastCellNum();

                    for (int columnIndex = 0; columnIndex < cells; columnIndex++) {
                        XSSFCell cell = row.getCell(columnIndex);
                        map.put(String.valueOf(columnIndex), getCellValue(cell));
//                        log.info(rowIndex + " 행 : " + columnIndex + " 열 = " + getCellValue(cell));
                    }
                }
                // 첫 번째 행에서 열의 수를 확인했다면 반복문 종료
                break;
            }

        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
