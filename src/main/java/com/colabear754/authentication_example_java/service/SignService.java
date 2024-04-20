package com.colabear754.authentication_example_java.service;

import com.colabear754.authentication_example_java.DTO.AbstractDTO;
import com.colabear754.authentication_example_java.DTO.Request.SignInRequest;
import com.colabear754.authentication_example_java.DTO.Response.SignInResponse;
import com.colabear754.authentication_example_java.DTO.Request.SignUpRequest;
import com.colabear754.authentication_example_java.common.ExceptionMessage;
import com.colabear754.authentication_example_java.entity.MemberEntity;
import com.colabear754.authentication_example_java.handler.BadRequestException;
import com.colabear754.authentication_example_java.mapper.MemberMapper;
import com.colabear754.authentication_example_java.repository.MemberRepository;
import com.colabear754.authentication_example_java.JWT.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SignService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;    // 추가
    private final TokenProvider tokenProvider; // note token 발급을 위한 추가
    private final MemberMapper memberMapper;

    @Transactional
    public AbstractDTO registerMember(SignUpRequest request) {
        MemberEntity member = (MemberEntity) memberMapper.convertRequestTOEntity(request);
        memberRepository.save(member);
        return memberMapper.fromEntity(member);
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest request) {
        MemberEntity member = memberRepository.findByAccount(request.getAccount())
                .filter(it -> encoder.matches(request.getPassword(), it.getPassword()))// note 암호화된 비밀번호라 비교하도록 수정
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.MEMBER_USER_NOT_FOUND.getMessage()));
        String token = tokenProvider.createToken(member.getAccount());// 토큰 생성
        return new SignInResponse(member.getName(), member.getType(), token);
    }
}
