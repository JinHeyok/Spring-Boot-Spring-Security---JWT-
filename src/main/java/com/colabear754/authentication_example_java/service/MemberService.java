package com.colabear754.authentication_example_java.service;

import com.colabear754.authentication_example_java.DTO.AbstractDTO;
import com.colabear754.authentication_example_java.DTO.ListResponseDTO;
import com.colabear754.authentication_example_java.DTO.MessageDTO;
import com.colabear754.authentication_example_java.DTO.StateDTO;
import com.colabear754.authentication_example_java.DTO.member.request.MemberUpdateRequest;
import com.colabear754.authentication_example_java.entity.Member;
import com.colabear754.authentication_example_java.mapper.MemberMapper;
import com.colabear754.authentication_example_java.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private MemberMapper memberMapper;

    @Transactional(readOnly = true)
    public AbstractDTO getMemberInfo(String account) {
        Optional<Member> member = memberRepository.findByAccount(account);
        AbstractDTO response = memberMapper.fromEntity(member.get());
        return response;
    }

    @Transactional
    public AbstractDTO deleteMember(UUID id) {
        memberRepository.deleteById(id);
        return new StateDTO(true);
    }

    @Transactional
    public AbstractDTO updateMember(User user, MemberUpdateRequest request) {
        return memberRepository.findByAccount(user.getUsername())
                .filter(member -> passwordEncoder.matches(request.getPassword() , member.getPassword()))
                .map(member -> {
                    member.update(request , passwordEncoder); // note 새 비밀번호를 암호화 -> 파라미터를 추가해준다.
                    return memberMapper.fromEntity(member);
                })
                .orElseThrow(() -> new NoSuchElementException("아이디 또는 비밀번호가 일치하지 않습니다."));
    }
}
