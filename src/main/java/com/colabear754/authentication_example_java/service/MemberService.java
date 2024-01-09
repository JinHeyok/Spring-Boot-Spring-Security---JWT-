package com.colabear754.authentication_example_java.service;

import com.colabear754.authentication_example_java.dto.member.request.MemberUpdateRequest;
import com.colabear754.authentication_example_java.dto.member.response.MemberDeleteResponse;
import com.colabear754.authentication_example_java.dto.member.response.MemberInfoResponse;
import com.colabear754.authentication_example_java.dto.member.response.MemberUpdateResponse;
import com.colabear754.authentication_example_java.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(String account) {
        return memberRepository.findByAccount(account)
                .map(MemberInfoResponse::from)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
    }

    @Transactional
    public MemberDeleteResponse deleteMember(UUID id) {
        if (!memberRepository.existsById(id)) return new MemberDeleteResponse(false);
        memberRepository.deleteById(id);
        return new MemberDeleteResponse(true);
    }

    @Transactional
    public MemberUpdateResponse updateMember(UUID id, MemberUpdateRequest request) {
        return memberRepository.findById(id)
                .filter(member -> member.getPassword().equals(request.password()))
                .map(member -> {
                    member.update(request , passwordEncoder); // note 새 비밀번호를 암호화 -> 파라미터를 추가해준다.
                    return MemberUpdateResponse.of(true, member);
                })
                .orElseThrow(() -> new NoSuchElementException("아이디 또는 비밀번호가 일치하지 않습니다."));
    }
}
