package com.colabear754.authentication_example_java.service;

import com.colabear754.authentication_example_java.DTO.AbstractDTO;
import com.colabear754.authentication_example_java.entity.MemberEntity;
import com.colabear754.authentication_example_java.mapper.MemberMapper;
import com.colabear754.authentication_example_java.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private MemberMapper memberMapper;

    @Transactional(readOnly = true)
    public AbstractDTO getMemberInfo(String account) {
        Optional<MemberEntity> member = memberRepository.findByAccount(account);
        AbstractDTO response = memberMapper.fromEntity(member.get());
        return response;
    }


}
