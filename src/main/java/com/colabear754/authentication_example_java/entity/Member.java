package com.colabear754.authentication_example_java.entity;

import com.colabear754.authentication_example_java.DTO.sign_up.request.SignUpRequest;
import com.colabear754.authentication_example_java.common.MemberType;
import com.colabear754.authentication_example_java.DTO.member.request.MemberUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Member extends Base{
    @Column(nullable = false, unique = true)
    private String account;
    @Column(nullable = false)
    private String password;
    private String name;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private MemberType type;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void update(MemberUpdateRequest newMember, PasswordEncoder encoder) {
        this.password = encoder.encode(newMember.getNewPassword());
        this.name = newMember.getName();
        this.age = newMember.getAge();
    }

    public static Member from(SignUpRequest request , PasswordEncoder encoder) {
        return Member.builder()
                .account(request.getAccount())
                .password(encoder.encode(request.getPassword()))
                .name(request.getName())
                .age(request.getAge())
                .type(MemberType.USER)
                .build();
    }
}
