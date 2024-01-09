package com.colabear754.authentication_example_java.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "authorization"; // note 스웨거에서 사용 할 수 있게 추가



    @Bean
    public OpenAPI swaggerApi() {
        return new OpenAPI()
                // note 스웨거에서 JWT 를 넣기위해 추가
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                // note 스웨거에서 JWT 를 넣기위해 추가
                .info(new Info()
                        .title("스프링시큐리티 + JWT 예제") // note 스웨거 페이지의 제목
                        .description("스프링시큐리티와 JWT를 이용한 사용자 인증 예제입니다.") // note 스웨거 페이지의 제목 밑에 디스크립션
                        .version("1.0.0")); // note ㅍ스웨거의 페이지의 API버전을 나타냄
    }
}
