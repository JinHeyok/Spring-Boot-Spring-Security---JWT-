package com.colabear754.authentication_example_java.service;

import com.colabear754.authentication_example_java.common.MemberType;
import com.colabear754.authentication_example_java.DTO.AbstractDTO;
import com.colabear754.authentication_example_java.DTO.ListResponseDTO;
import com.colabear754.authentication_example_java.entity.Member;
import com.colabear754.authentication_example_java.mapper.MemberMapper;
import com.colabear754.authentication_example_java.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Transactional(readOnly = true)
    public AbstractDTO getMembers(MemberType memberType) {
        List<Member> memberList = memberRepository.findAllByType(memberType);
        AbstractDTO response = ListResponseDTO.of(memberMapper.fromEntities(memberList));
        return response;
    }

}
