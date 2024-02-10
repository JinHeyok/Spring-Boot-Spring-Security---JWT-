package com.colabear754.authentication_example_java.mapper;

import com.colabear754.authentication_example_java.DTO.EntityMapperConvert;
import com.colabear754.authentication_example_java.DTO.member.response.MemberResponse;
import com.colabear754.authentication_example_java.DTO.sign_up.request.SignUpRequest;
import com.colabear754.authentication_example_java.entity.BaseEntity;
import com.colabear754.authentication_example_java.entity.MemberEntity;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

@Component
public class MemberMapper implements EntityMapperConvert {


    @Override
    public MemberResponse fromEntity(BaseEntity entity) {
        MemberEntity member = (MemberEntity) entity;
        MemberResponse response = new MemberResponse();
        response.setId(member.getId());
        response.setAge(member.getAge());
        response.setAccount(member.getAccount());
        response.setType(member.getType());
        response.setName(member.getName());
        response.setCreateAt(member.getCreatedAt());
        response.setUpdateAt(member.getUpdatedAt());
        return response;
    }

    @Override
    public List<MemberResponse> fromEntities(List<? extends BaseEntity> entities) {
        List<MemberEntity> memberList = (List<MemberEntity>) entities;
        List<MemberResponse> responseList = new ArrayList<>();
        for (MemberEntity data : memberList) {
            responseList.add(fromEntity(data));
        }
        return responseList;
    }

    public MemberEntity convertRequestTOEntity(SignUpRequest request) {
        MemberEntity member = new MemberEntity();
        BeanUtils.copyProperties(request , member);
        return member;
    }
}
