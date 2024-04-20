package com.colabear754.authentication_example_java.DTO.Response;

import com.colabear754.authentication_example_java.DTO.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DateResponseDTO extends AbstractDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime updatedAt;
}
