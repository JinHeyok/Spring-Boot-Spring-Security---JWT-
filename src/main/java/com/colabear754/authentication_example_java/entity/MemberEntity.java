package com.colabear754.authentication_example_java.entity;

import com.colabear754.authentication_example_java.DTO.sign_up.request.SignUpRequest;
import com.colabear754.authentication_example_java.common.MemberType;
import com.colabear754.authentication_example_java.DTO.member.request.MemberUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "member")
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "m_account" , nullable = false, unique = true)
    private String account;

    @Column(name = "m_password" , nullable = false)
    private String password;

    @Column(name = "m_name")
    private String name;

    @Column(name = "m_age")
    private Integer age;

    @Column(name = "m_type")
    @Enumerated(EnumType.STRING)
    private MemberType type;

}
