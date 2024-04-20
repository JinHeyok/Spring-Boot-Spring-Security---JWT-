package com.colabear754.authentication_example_java.entity;

import com.colabear754.authentication_example_java.common.MemberType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@Entity
@Table(name = "member")
@Comment("회원 테이블")
@ToString
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유 인덱스")
    @Column(name = "m_Id")
    private Long Id;

    @Column(name = "m_account" , nullable = false, unique = true)
    @Comment("회원의 아이디")
    private String account;

    @Column(name = "m_password" , nullable = false)
    @Comment("회원의 비밀번호")
    private String password;

    @Column(name = "m_name")
    @Comment("회원의 이름")
    private String name;

    @Column(name = "m_age")
    @Comment("회원의 나이")
    private Integer age;

    @Column(name = "m_type")
    @Enumerated(EnumType.STRING)
    @Comment("회원의 권한 타입")
    private MemberType type;

}
