package com.colabear754.authentication_example_java.service;

import com.colabear754.authentication_example_java.DTO.AbstractDTO;
import com.colabear754.authentication_example_java.DTO.StateDTO;
import com.colabear754.authentication_example_java.DTO.sign_in.request.SignInRequest;
import com.colabear754.authentication_example_java.DTO.sign_in.response.SignInResponse;
import com.colabear754.authentication_example_java.DTO.sign_up.request.SignUpRequest;
import com.colabear754.authentication_example_java.entity.MemberEntity;
import com.colabear754.authentication_example_java.mapper.MemberMapper;
import com.colabear754.authentication_example_java.repository.MemberRepository;
import com.colabear754.authentication_example_java.JWT.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
        MemberEntity member = memberMapper.convertRequestTOEntity(request);
        try {
            memberRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        return new StateDTO(true);
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest request) {
        MemberEntity member = memberRepository.findByAccount(request.getAccount())
                .filter(it -> encoder.matches(request.getPassword(), it.getPassword()))// note 암호화된 비밀번호라 비교하도록 수정
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
        String token = tokenProvider.createToken(member.getAccount());// 토큰 생성
        return new SignInResponse(member.getName(), member.getType(), token);
    }
}
