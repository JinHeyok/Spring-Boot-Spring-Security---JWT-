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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExcelService {
    public boolean importExcel(MultipartFile file) {

        // NOTE 파일이 존재하지 않는 경우
        if (file.isEmpty()) {
            throw new BadRequestException("파일이 존재하지 않습니다.");
        }
        // NOTE 파일의 확장자 검사 엑셀 파일만 가능
        String contentType = file.getContentType();
        if (!contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            throw new BadRequestException("파일 형식이 올바르지 않습니다.");
        }

        Map<String, Object> nameMap = getFirstExcelData(file , 0);
        List<Map<String, Object>> listMap = getListExcelData(file , 0);

        // NOTE 1행의 이름 데이터만 가져오기
        for (int i = 0; i < nameMap.size(); i++) {
            System.out.println(nameMap.get(String.valueOf(i)));
        }

        // NOTE 2행부터 데이터 전부 가져오기
        for (int i = 0; i < listMap.size(); i++) {
            System.out.println(listMap.get(i));
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


    // NOTE 각 셀의 데이터타입에 맞게 값 가져오기
    public String getCellValue(XSSFCell cell) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(20);  // adjust this as per your requirement

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
                    // NOTE 숫자이지만 날짜 형식일 경우
                    value = String.valueOf(cell.getDateCellValue()); // NOTE 날짜 값을 얻거나 다른 형식으로 변환
                } else {
                    if (cell.getCellStyle().getDataFormatString().contains("%")) {
                        // NOTE 백분율 형식일 경우
                        double numericValue = cell.getNumericCellValue() * 100;
                        value = String.format("%.2f%%", numericValue);
                    } else {
                        // NOTE 일반 숫자일 경우
                        double numericValue = cell.getNumericCellValue();
                        String strValue = df.format(numericValue);
                        if (strValue.contains(".")) {
                            value = strValue;
                        } else {
                            value = String.valueOf((int) numericValue);
                        }
                    }
                }
            default:
                break;
        }
        return value;
    }

    // NOTE 엑셀파일의 데이터 목록 가져오기 (파라미터들은 위에서 설명함)
    public List<Map<String, Object>> getListExcelData(MultipartFile file ,  int sheetNumber) {

        List<Map<String, Object>> excelList = new ArrayList<>();

        try {
            OPCPackage opcPackage = OPCPackage.open(file.getInputStream());

            @SuppressWarnings("resource")
            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

            // NOTE 첫번째 시트
            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);

            int rowIndex = 0;
            int columnIndex = 0;

            // NOTE 첫번째 행(0)은 컬럼 명이기 때문에 두번째 행(1) 부터 검색
            for (rowIndex = 1; rowIndex < sheet.getLastRowNum() + 1; rowIndex++) {
                XSSFRow row = sheet.getRow(rowIndex);

                // NOTE 빈 행은 Skip
                if (row.getCell(0) != null && !row.getCell(0).toString().isBlank()) {

                    Map<String, Object> map = new HashMap<>();

                    int cells = row.getLastCellNum();

                    for (columnIndex = 0; columnIndex < cells; columnIndex++) {
                        XSSFCell cell = row.getCell(columnIndex);
                        map.put(String.valueOf(columnIndex), getCellValue(cell));
// NOTE                        log.info(rowIndex + " 행 : " + columnIndex + " 열 = " + getCellValue(cell));
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

    public Map<String, Object> getFirstExcelData(MultipartFile file , int sheetNumber) {

        Map<String, Object> map = new HashMap<>();

        try {
            OPCPackage opcPackage = OPCPackage.open(file.getInputStream());
            @SuppressWarnings("resource")
            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

            // NOTE 첫번째 시트
            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);

            // NOTE 첫번째 행(0)은 컬럼 명만 검색
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                XSSFRow row = sheet.getRow(rowIndex);

                // NOTE 빈 행은 Skip
                if (row.getCell(0) != null && !row.getCell(0).toString().isBlank()) {

                    // NOTE 첫 번째 행에서 열의 수를 가져오기 (열의 수는 0번째 셀부터 시작)
                    int cells = row.getLastCellNum();

                    for (int columnIndex = 0; columnIndex < cells; columnIndex++) {
                        XSSFCell cell = row.getCell(columnIndex);
                        map.put(String.valueOf(columnIndex), getCellValue(cell));
// NOTE                        log.info(rowIndex + " 행 : " + columnIndex + " 열 = " + getCellValue(cell));
                    }
                }
                // NOTE 첫 번째 행에서 열의 수를 확인했다면 반복문 종료
                break;
            }

        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

//    private void setEntityValueBasedOnOrder(YourEntity entity, int order, String value) {
//        // 순서에 상관없이 자동으로 엔티티 값 설정 (첫 번째 필드 제외)
//        Field[] fields = YourEntity.class.getDeclaredFields();
//        if (order < fields.length && order > 0) {
//            Field field = fields[order - 1]; // 첫 번째 필드를 제외하고 순서 조정
//            field.setAccessible(true);
//            try {
//                field.set(entity, value);
//            } catch (IllegalAccessException e) {
//                // 예외 처리
//            }
//        }
//    }

}