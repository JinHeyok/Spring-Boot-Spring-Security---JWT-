package com.colabear754.authentication_example_java.security;

import com.colabear754.authentication_example_java.JWT.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity // note  JWT 토큰 Filter 추가
@RequiredArgsConstructor // note - final이나 @NonNull 인 필드 값만 파라미터로 받는 생성자를 추가한다. - final: 값이 할당되면 더 이상 변경 할 수 없다
public class SecurityConfig {

    // note 허용할 URL 목록을 배열로 분리
    private final String[] allowedURL = {
            "/",
            "/api/docs/**",
            "/v3/**",
            "/sign-up",
            "/sign-in",
            "/zoom/**"
    };

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    // 인증 매니저에 대한 빈을 정의한다.
    // 인증 매니저는 사용자의 인증을 처리하는 역할을 한다.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
        // AuthenticationConfiguration : 스프링 시큐리티에서 제공하는 인증 구성 객체
        // getAuthenticationManager()로 인증 매니저를 가져온다.
    }

    // Spring Security CORS Configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .headers(headers -> headers.frameOptions().sameOrigin()) // note H2 Console 허용
                .authorizeHttpRequests(request ->
                        request.requestMatchers(allowedURL).permitAll() // note requestMatchers의 인자로 전달된 URL은 모두에게 허용 (접근 가능)
                                .anyRequest().authenticated() // note 그 외의 모든 요청은 인증 필요

                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // note Session은 사용하지 않으므로 STATELESS 사용
                )
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                // note JWT Filter 적용
                .build();
    }

    @Bean
    // 비밀번호 인코더에 대한 빈을 정의한다.
    // 이 빈은 비밀번호를 인코딩하고 검증하는데 사용된다.
    // 비밀번호를 안전하게 저장하기 위해 사용된다.
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
        // 스프링 시큐리티에서 제공하는 비밀번호 인코더중 하나이다.
        // BCcypt 해서 알고리즘을 사용하여 비밀번호를 암호화한다.
        // 이 알고리즘은 안전한 해시 함수로 알려져 있으며, 해시된 비밀번호는 복호화 할 수 없다.
    }

}
