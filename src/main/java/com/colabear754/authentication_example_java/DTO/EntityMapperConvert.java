package com.colabear754.authentication_example_java.DTO;

import com.colabear754.authentication_example_java.entity.BaseEntity;

import java.util.List;

public interface EntityMapperConvert {
    AbstractDTO fromEntity(BaseEntity entity);

    List<? extends AbstractDTO> fromEntities(List<? extends BaseEntity> entities);

}
