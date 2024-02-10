package com.colabear754.authentication_example_java.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "List Response" , description = "List Response Payload")
public class ListResponseDTO<T> extends AbstractDTO{

    @JsonProperty("list")
    private List<T> listResponseDTO;

    private ListResponseDTO(List<T> listObject) {
        this.listResponseDTO = listObject;
    }

    public static <T> ListResponseDTO<T> of(List<T> listObject) {
        return new ListResponseDTO<T>(listObject);
    }


}
