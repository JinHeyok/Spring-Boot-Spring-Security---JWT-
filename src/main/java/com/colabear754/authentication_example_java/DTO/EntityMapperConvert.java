package com.colabear754.authentication_example_java.DTO;

import com.colabear754.authentication_example_java.entity.Base;

import java.util.List;

public interface EntityMapperConvert {
    AbstractDTO fromEntity(Base entity);

    List<? extends AbstractDTO> fromEntities(List<? extends  Base> entities);

}
