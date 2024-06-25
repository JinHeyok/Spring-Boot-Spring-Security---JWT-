package com.colabear754.authentication_example_java.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Schema(name = "ExcelSheetData", description = "엑셀 시트 데이터")
@AllArgsConstructor
@Getter
@Setter
public class ExcelSheetData {

    @Schema(name = "commentList", description = "컬럼 코멘트 리스트")
    private List<String> commentList;
    @Schema(name = "dataMap", description = "데이터 맵")
    private Map<Integer, List<String>> dataMap;
}
