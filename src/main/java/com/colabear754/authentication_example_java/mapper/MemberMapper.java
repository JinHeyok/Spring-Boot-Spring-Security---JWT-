package com.colabear754.authentication_example_java.mapper;

import com.colabear754.authentication_example_java.DTO.Request.BaseRequestDTO;
import com.colabear754.authentication_example_java.DTO.EntityMapperConvert;
import com.colabear754.authentication_example_java.DTO.member.response.MemberResponse;
import com.colabear754.authentication_example_java.DTO.Request.SignUpRequest;
import com.colabear754.authentication_example_java.common.MemberType;
import com.colabear754.authentication_example_java.entity.BaseEntity;
import com.colabear754.authentication_example_java.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MemberMapper implements EntityMapperConvert {

    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberResponse fromEntity(BaseEntity entity) {
        MemberResponse response = new MemberResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    @Override
    public List<MemberResponse> fromEntities(List<? extends BaseEntity> entities) {
        List<MemberResponse> response = entities.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public BaseEntity convertRequestTOEntity(BaseRequestDTO requestDTO) {
        MemberEntity member = new MemberEntity();
        SignUpRequest request = (SignUpRequest) requestDTO;
        BeanUtils.copyProperties(request, member);
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setType(MemberType.ADMIN);
        return member;
    }

}
