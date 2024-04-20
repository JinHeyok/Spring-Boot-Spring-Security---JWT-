package com.colabear754.authentication_example_java.common;

import lombok.AllArgsConstructor;

public enum ExceptionMessage {

    MEMBER_USER_NOT_FOUND("아이디 또는 비밀번호가 일치하지 않습니다."),
    MEMBER_ACCOUNT_ALREADY("이미 사용중인 아이디입니다."),
    ;

    private final String message;
    private ExceptionMessage(String message) {this.message = message;}
    public String getMessage() {return this.message;}


}
