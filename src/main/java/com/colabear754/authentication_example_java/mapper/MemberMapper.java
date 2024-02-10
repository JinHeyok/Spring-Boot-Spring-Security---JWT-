package com.colabear754.authentication_example_java.mapper;

import com.colabear754.authentication_example_java.DTO.EntityMapperConvert;
import com.colabear754.authentication_example_java.DTO.member.response.MemberResponse;
import com.colabear754.authentication_example_java.entity.Base;
import com.colabear754.authentication_example_java.entity.Member;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemberMapper implements EntityMapperConvert {


    @Override
    public MemberResponse fromEntity(Base entity) {
        Member member = (Member) entity;
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
    public List<MemberResponse> fromEntities(List<? extends Base> entities) {
        List<Member> memberList = (List<Member>) entities;
        List<MemberResponse> responseList = new ArrayList<>();
        for (Member data : memberList) {
            responseList.add(fromEntity(data));
        }
        return responseList;
    }
}
