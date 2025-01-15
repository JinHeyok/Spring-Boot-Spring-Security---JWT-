package com.colabear754.authentication_example_java.Util;

import com.colabear754.authentication_example_java.entity.ExcelSheetData;
import com.colabear754.authentication_example_java.handler.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class ExcelUtil {
    /**
     * NOTE 엑셀파일의 데이터 목록 가져오기 (파라미터들은 위에서 설명함)
     *
     * @param file        Excel 파일
     * @param sheetNumber 시트 넘버 0 이 첫번쨰
     * @param startIndex  열 데이터 존재 유무 시작 인덱스
     *                    데이터 값이 있는 것만 가져온다.
     * @return {List} response
     * @throws InvalidFormatException
     * @throws IOException            Excel 파일을 처리하는 과정에서 에러가 발생했습니다. -> {파일위치.함수위치}
     * @author 방진혁
     */
    public static List<Map<String, Object>> getListExcelData(MultipartFile file,
                                                             int sheetNumber,
                                                             int startIndex) throws InvalidFormatException, IOException {

        List<Map<String, Object>> excelList = new ArrayList<>();

        try {
            OPCPackage opcPackage = OPCPackage.open(file.getInputStream()); // MEMO 엑셀파일의 내용을 읽는다.

            @SuppressWarnings("resource") // MEMO 자원(resource) 누수 경고를 억제 XSSWorkbook 객체가 자동으로 닫히지 않아 발생 할 수 있는 경고를 억제하는데 사용한다.
            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

            // NOTE 첫번째 시트
            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);

            int rowIndex = 0;
            int columnIndex = 0;

            // NOTE 첫번째 행(0)은 컬럼 명이기 때문에 두번째 행(1) 부터 검색
            for (rowIndex = 1; rowIndex < sheet.getLastRowNum() + 1; rowIndex++) {
                XSSFRow row = sheet.getRow(rowIndex);

                // NOTE 빈 행은 Skip
                if (row != null && row.getCell(startIndex) != null && !row.getCell(startIndex).toString().isBlank()) {

                    Map<String, Object> map = new HashMap<>();

                    int cells = row.getLastCellNum();

                    for (columnIndex = 0; columnIndex < cells; columnIndex++) {
                        XSSFCell cell = row.getCell(columnIndex);
                        map.put(String.valueOf(columnIndex), getCellValue(cell));
                        // NOTE log.info(rowIndex + " 행 : " + columnIndex + " 열 = " + getCellValue(cell));
                    }

                    excelList.add(map);
                }
            }

        } catch (InvalidFormatException e) {
            e.printStackTrace();
            throw new InvalidFormatException("ExcelUtil.getFirstExcelData");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("ExcelUtil.getFirstExcelData");
        }
        return excelList;
    }

    /**
     * NOTE 각 셀의 데이터타입에 맞게 값 가져오기
     *
     * @param cell Excel의 셀 하나의 데이텅
     * @return {String} response
     * @author 방진혁
     */
    public static String getCellValue(XSSFCell cell) {
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

    /**
     * NOTE Excel 파일의 첫 행만 가져온다 (대부분은 이름을 가져온다.)
     *
     * @param file        Excel 파일
     * @param sheetNumber 시트 넘버 0 이 첫번쨰
     * @return {Map}
     * @throws IOException
     * @throws InvalidFormatException Excel 파일을 처리하는 과정에서 에러가 발생했습니다. -> {파일위치.함수위치}
     * @author 방진혁
     */
    public static Map<String, Object> getFirstExcelData(MultipartFile file, int sheetNumber) throws IOException, InvalidFormatException {

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
                        // NOTE log.info(rowIndex + " 행 : " + columnIndex + " 열 = " + getCellValue(cell));
                    }
                }
                // NOTE 첫 번째 행에서 열의 수를 확인했다면 반복문 종료
                break;
            }

        } catch (InvalidFormatException e) {
            e.printStackTrace();
            throw new InvalidFormatException("ExcelUtil.getFirstExcelData");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("ExcelUtil.getFirstExcelData");
        }

        return map;
    }

    /**
     * @param commentList
     * @param dataMap
     * @param sheetName
     * @return
     */
    public static Workbook excelCreateDownload(List<String> commentList,
                                               Map<Integer, List<String>> dataMap,
                                               String sheetName) {
        Workbook workbook = new SXSSFWorkbook(); // MEMO Excel 파일을 생성한다.

        Sheet sheet = workbook.createSheet(sheetName); // MEMO 엑셀 시트를 생성

        // MEMO 공통 스타일을 위한 CellStyle 객체 생성
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);                    // MEMO 아래쪽 테두리
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // MEMO 아래쪽 테두리 색상
        style.setBorderLeft(BorderStyle.THIN);                     // MEMO 왼쪽 테두리
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // MEMO 왼쪽 테두리 색상
        style.setBorderRight(BorderStyle.THIN);                     // MEMO 오른쪽 테두리
        style.setRightBorderColor(IndexedColors.BLACK.getIndex()); // MEMO 오른쪽 테두리 색상
        style.setBorderTop(BorderStyle.THIN);                     // MEMO 위쪽 테두리
        style.setTopBorderColor(IndexedColors.BLACK.getIndex()); // MEMO 위쪽 테두리 색상

        // MEMO 배경색 설정
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex()); // MEMO 배경색으로 노란색 선택
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND); // MEMO 채우기 패턴으로 단색 채우기 선택

        Row headerRow = sheet.createRow(0); // MEMO 2행을 선택 (0행은 일반적으로 헤더로 사용됨)
        for (int i = 0; i < commentList.size(); i++) { // MEMO 코멘트 이름으로 2행의 카테고리 이름을 넣는다.
            Cell cell = headerRow.createCell(i); // MEMO 새 셀 생성 및 헤더 텍스트 설정
            cell.setCellValue(commentList.get(i)); // MEMO commentList에서 가져온 값으로 셀 값 설정
            cell.setCellStyle(style); // MEMO 앞서 생성한 스타일 적용
            sheet.setColumnWidth(i, 3500); // MEMO 너비를 3500으로 설정
        }

        // MEMO 필터 설정 (코멘트값의 수에 따라 입력)
        CellRangeAddress range = new CellRangeAddress(0, 0, 0, commentList.size() - 1);
        sheet.setAutoFilter(range);

        Set<Integer> keys = dataMap.keySet(); // MEMO 키의 값들을 추출한다.
        int rowNo = 1; // MEMO 데이털르 열을 지정
        for (int i = 0; i < dataMap.get(0).size(); i++) { // MEMO g데이터의 수는 동일 하므로 인덱스 값으로 순회한다.
            Row row = sheet.createRow(rowNo++); // MEMO 하나의 데이터들을 순회할 때 마다 열을 바꿔준다.
            for (Integer key : keys) { // MEMO 해당 Key를 다 가져온다.
                // MEMO Key값에 있는 데이터 리스트를 전부 셀에 입력해준다.
//                row.createCell(key).setCellValue(dataMap.get(key).get(i));
                // MEMO 데이터가 없을 경우 빈값으로 셀을 생성한다.
                if (dataMap.get(key) != null && !dataMap.get(key).isEmpty() && i < dataMap.get(key).size()) {
                    row.createCell(key).setCellValue(dataMap.get(key).get(i));
                }
            }
        }
        log.info("==========> " + sheetName + " ExcelFile을 정상적으로 생성하였습니다.");
        return workbook;
    }

    public static Workbook excelCreateDownloadAll(Map<String, ExcelSheetData> sheetsDataMap) {
        Workbook workbook = new HSSFWorkbook();

        for (String sheetName : sheetsDataMap.keySet()) {
            ExcelSheetData sheetData = sheetsDataMap.get(sheetName);
            List<String> commentList = sheetData.getCommentList();
            Map<Integer, List<String>> dataMap = sheetData.getDataMap();

            Sheet sheet = workbook.createSheet(sheetName);

            // MEMO 공통 스타일을 위한 CellStyle 객체 생성
            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);                    // MEMO 아래쪽 테두리
            style.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // MEMO 아래쪽 테두리 색상
            style.setBorderLeft(BorderStyle.THIN);                     // MEMO 왼쪽 테두리
            style.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // MEMO 왼쪽 테두리 색상
            style.setBorderRight(BorderStyle.THIN);                     // MEMO 오른쪽 테두리
            style.setRightBorderColor(IndexedColors.BLACK.getIndex()); // MEMO 오른쪽 테두리 색상
            style.setBorderTop(BorderStyle.THIN);                     // MEMO 위쪽 테두리
            style.setTopBorderColor(IndexedColors.BLACK.getIndex()); // MEMO 위쪽 테두리 색상

            // MEMO 배경색 설정
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex()); // MEMO 배경색으로 노란색 선택
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND); // MEMO 채우기 패턴으로 단색 채우기 선택

            Row headerRow = sheet.createRow(0); // MEMO 2행을 선택 (0행은 일반적으로 헤더로 사용됨)
            for (int i = 0; i < commentList.size(); i++) { // MEMO 코멘트 이름으로 2행의 카테고리 이름을 넣는다.
                Cell cell = headerRow.createCell(i); // MEMO 새 셀 생성 및 헤더 텍스트 설정
                cell.setCellValue(commentList.get(i)); // MEMO commentList에서 가져온 값으로 셀 값 설정
                cell.setCellStyle(style); // MEMO 앞서 생성한 스타일 적용
                sheet.setColumnWidth(i, 3500); // MEMO 너비를 3500으로 설정
            }

            // MEMO 필터 설정 (코멘트값의 수에 따라 입력)
            CellRangeAddress range = new CellRangeAddress(0, 0, 0, commentList.size() - 1);
            sheet.setAutoFilter(range);

            Set<Integer> keys = dataMap.keySet();
            int rowNo = 1;
            for (int i = 0; i < dataMap.get(0).size(); i++) {
                Row row = sheet.createRow(rowNo++);
                for (Integer key : keys) {
//                    if (dataMap.containsKey(key) && !dataMap.get(key).isEmpty()) {
                    row.createCell(key).setCellValue(dataMap.get(key).get(i));
//                    }
                    // MEMO 데이터가 없을 경우 빈값으로 셀을 생성한다.
                    if (dataMap.get(key) != null && !dataMap.get(key).isEmpty() && i < dataMap.get(key).size()) {
                        row.createCell(key).setCellValue(dataMap.get(key).get(i));
                    }
                }
            }
            log.info("============> " + sheetName + " ExcelFile을 정상적으로 생성하였습니다.");
        }

        return workbook;
    }

    /**
     * NOTE CSV 파일을 읽어서 XLSX 파일로 변환
     *
     * @param file CSV 파일
     * @return {File} response
     * @throws IOException
     * @throws BadRequestException    파일 형식이 올바르지 않습니다. -> {파일위치.함수위치}
     * @throws InvalidFormatException
     */
    public static File convertCsvFileTOXlsxFile(MultipartFile file) {
        File xlsxFile = null;
        try {
            // NOTE 파일이 존재하지 않는 경우
            if (file.isEmpty()) {
                throw new BadRequestException("파일이 존재하지 않습니다.");
            }

            // NOTE 파일의 확장자 검사 엑셀 파일만 가능
            String contentType = file.getContentType();
            if (!contentType.equals("text/csv")) {
                throw new BadRequestException("파일 형식이 올바르지 않습니다.");
            }

            // NOTE CSV 파일을 읽어서 XLSX 파일로 변환
            Reader in = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

            xlsxFile = File.createTempFile("temp", ".xlsx");
            FileOutputStream fos = new FileOutputStream(xlsxFile);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");

            int rowNumber = 0;
            for (CSVRecord record : records) {
                Row row = sheet.createRow(rowNumber++);
                for (int i = 0; i < record.size(); i++) {
                    row.createCell(i).setCellValue(record.get(i));
                }
            }
            workbook.write(fos);
            fos.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xlsxFile;
    }

    /**
     * NOTE CSV 파일의 데이터를 모두 가져온다
     *
     * @param file CSV 파일
     * @return {Map}
     * @throws IOException
     */
    public static List<Map<String, String>> getCsvData(MultipartFile file) throws IOException {
        List<Map<String, String>> list = new ArrayList<>();

        try (Reader in = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);

            for (CSVRecord record : records) {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < record.size(); i++) {
                    map.put(String.valueOf(i), record.get(i));
                }
                list.add(map);
            }
        }
        return list;
    }


    // NOTE Map 에서 Key 값은 0,1,2,3,으로 지정 한다.
    // NOTE List<String> 은 데이터를 순서대로 담는다.
    // NOTE Excel에서 추출 할 때는 ketSet()으로 키값을 뽑느다.
    // NOTE key값이 row.creteCell값을 지정한다.
    // NOTE 하나의 List를 꺼내서 해당 리스트 만큼 데이터를 넣는다 데이터 수는 다 동일로 무결성 결성
    // NOTE Value는 해당 키의 반복문으로 돌려서 데이터를 주입
//    public static Map<Integer, List<String>> convertEntityListTOMap(List<? extends BaseEntity> baseEntityList) throws IllegalAccessException {
//        // MEMO 필드 값들을 저장할 맵 초기화, 키는 문자열(필드 순서), 값은 리스트(필드 값)
//        Map<Integer, List<String>> fieldValuesMap = new TreeMap<>();
//
//        for (BaseEntity entity : baseEntityList) { // MEMO BaseEntity 리스트를 순회
//            Field[] fields = entity.getClass().getDeclaredFields(); // MEMO 현재 엔티티의 모든 필드 가져오기
//            int fieldIndex = 0; // MEMO 각 엔티티마다 필드 인덱스를 0부터 시작
//
//            for (Field field : fields) { // MEMO 모든 필드에 대해 반복
//                field.setAccessible(true); // MEMO 비공개 필드에도 접근 가능하게 설정
//                try {
//                    Object value = field.get(entity); // MEMO 필드의 값을 가져옴
//                    String valueStr = "";
//                    if (value == null || value.equals("null")) {
//                        valueStr = ""; // MEMO 값이 null이면 ""(빈값) 문자열 사용
//                    } else {
//                        valueStr = value.toString(); // MEMO 아니면 toString 호출
//                    }
//                    if (value instanceof  String) {valueStr = (String) value;} // MEMO 값이 문자열이면 그대로 사용
//                    if (value instanceof Integer) {valueStr = Integer.toString((Integer) value);} // MEMO 값이 정수형이면 문자열로 변환
//                    if (value instanceof Double) {valueStr = Double.toString((Double) value);} // MEMO 값이 실수형이면 문자열로 변환
//                    if (value instanceof Float) {valueStr = Float.toString((Float) value);} // MEMO 값이 실수형이면 문자열로 변환
//                    if (value instanceof Long) {valueStr = Long.toString((Long) value);} // MEMO 값이 정수형이면 문자열로 변환
//                    if (value instanceof Boolean) {valueStr = Boolean.toString((Boolean) value);} // MEMO 값이 불리언이면 문자열로 변환
//                    if (value instanceof Date || value instanceof LocalDateTime || value instanceof LocalDate) { // MEMO  값이 날짜형이면 문자열로 변환
//                        valueStr = com.ninefive.national.utils.DateUtil.convertDateToString(value);
//                    }
//                    String valueStr = value == null || value.equals("null") ? "" : value.toString(); // MEMO 값이 null이면 ""(빈값) 문자열 사용, 아니면 toString 호출
//                    int key = fieldIndex; // MEMO 맵의 키로 사용될 문자열 생성 (필드 인덱스)
//
//                    // MEMO 맵에 키가 없으면 새 리스트를 만들고 값 추가, 있으면 기존 리스트에 값 추가
//                    fieldValuesMap.computeIfAbsent(key, k -> new ArrayList<>()).add(valueStr);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace(); // MEMO 접근 권한 예외 처리
//                    throw new IllegalAccessException(e.getMessage());
//                }
//                fieldIndex++; // MEMO 다음 필드를 위해 인덱스 증가
//            }
//        }
//        return fieldValuesMap;
//    }


//    public static Map<Integer, List<String>> convertEntityListTOMapFilter(List<? extends BaseEntity> baseEntityList, Set<String> excludedFields) throws IllegalAccessException {
//        // MEMO 필드 값들을 저장할 맵 초기화, 키는 필드 순서, 값은 리스트(필드 값)
//        Map<Integer, List<String>> fieldValuesMap = new TreeMap<>();
//
//        // MEMO BaseEntity 리스트를 순회
//        for (BaseEntity entity : baseEntityList) {
//            // MEMO 현재 엔티티의 모든 필드 가져오기
//            Field[] fields = entity.getClass().getDeclaredFields();
//            // MEMO 각 엔티티마다 필드 인덱스를 0부터 시작
//            int fieldIndex = 0;
//            // MEMO 모든 필드에 대해 반복
//            for (Field field : fields) {
//                // MEMO 제외 필드 목록에 현재 필드 이름이 포함되어 있다면 처리하지 않고 넘어감
//                if (excludedFields.contains(field.getName())) {continue;}
//                // MEMO 비공개 필드에도 접근 가능하게 설정
//                field.setAccessible(true);
//                try {
//                    // MEMO 필드의 값을 가져옴
//                    Object value = field.get(entity);
//                    String valueStr = "";

//                    if (value == null || value.equals("null")) {
//                        valueStr = ""; // MEMO 값이 null이면 ""(빈값) 문자열 사용
//                    } else {
//                        valueStr = value.toString(); // MEMO 아니면 toString 호출
//                    }
//                    if (value instanceof  String) {valueStr = (String) value;} // MEMO 값이 문자열이면 그대로 사용
//                    if (value instanceof Integer) {valueStr = Integer.toString((Integer) value);} // MEMO 값이 정수형이면 문자열로 변환
//                    if (value instanceof Double) {valueStr = Double.toString((Double) value);} // MEMO 값이 실수형이면 문자열로 변환
//                    if (value instanceof Float) {valueStr = Float.toString((Float) value);} // MEMO 값이 실수형이면 문자열로 변환
//                    if (value instanceof Long) {valueStr = Long.toString((Long) value);} // MEMO 값이 정수형이면 문자열로 변환
//                    if (value instanceof Boolean) {valueStr = Boolean.toString((Boolean) value);} // MEMO 값이 불리언이면 문자열로 변환
//                    if (value instanceof Date || value instanceof LocalDateTime || value instanceof LocalDate) { // MEMO  값이 날짜형이면 문자열로 변환
//                        valueStr = com.ninefive.national.utils.DateUtil.convertDateToString(value);
//                    }
//                    // MEMO 값이 null이면 ""(빈값) 문자열 사용, 아니면 toString 호출
//                    String valueStr = value == null || value.equals("null") ? "" : value.toString();
//                    // MEMO 맵의 키로 사용될 필드 인덱스
//                    int key = fieldIndex;
//
//                    // MEMO 맵에 키가 없으면 새 리스트를 만들고 값 추가, 있으면 기존 리스트에 값 추가
//                    fieldValuesMap.computeIfAbsent(key, k -> new ArrayList<>()).add(valueStr);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace(); // MEMO 접근 권한 예외 처리
//                    throw new IllegalAccessException(e.getMessage());
//                }
//                // MEMO 다음 필드를 위해 인덱스 증가
//                fieldIndex++;
//            }
//        }
//        // MEMO 생성된 맵 반환
//        return fieldValuesMap;
//    }

    public static String convertDateToString(Object date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = "";
        if (date instanceof Date) {
            dateStr = df.format((Date) date);
        }
        if (date instanceof LocalDateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dateStr = ((LocalDateTime) date).format(formatter);
        }
        if (date instanceof LocalDate) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dateStr = ((LocalDate) date).format(formatter);
        }
        return dateStr;
    }
}
