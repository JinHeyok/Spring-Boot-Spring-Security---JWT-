package com.colabear754.authentication_example_java.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessageDTO extends AbstractDTO{
    private String errorMessage;
}
