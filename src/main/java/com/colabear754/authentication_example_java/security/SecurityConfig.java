package com.colabear754.authentication_example_java.security;

import com.colabear754.authentication_example_java.service.IUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableMethodSecurity // note  JWT 토큰 Filter 추가
@RequiredArgsConstructor // note - final이나 @NonNull 인 필드 값만 파라미터로 받는 생성자를 추가한다. - final: 값이 할당되면 더 이상 변경 할 수 없다
public class SecurityConfig {

    // note 허용할 URL 목록을 배열로 분리
    private final String[] allowedURL = {
            "/" ,
            "/swagger-ui/**",
            "/v3/**",
            "/sign-up",
            "/sign-in"
    };

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

//    @Autowired
//    private IUserDetailsService userDetails;
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        daoAuthenticationProvider.setUserDetailsService(userDetails);
//        return daoAuthenticationProvider;
//    }
//
//    // authentication manager
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .headers(headers -> headers.frameOptions().sameOrigin()) // note H2 Console 허용
                .authorizeHttpRequests(request ->
                        request.requestMatchers(allowedURL).permitAll() // note requestMatchers의 인자로 전달된 URL은 모두에게 허용 (접근 가능)
                                .requestMatchers(PathRequest.toH2Console()).permitAll() // note H2 콘솔 접속은 모두에게 허용
                                .anyRequest().authenticated() // note 그 외의 모든 요청은 인증 필요

                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // note Session은 사용하지 않으므로 STATELESS 사용
                )
                .addFilterBefore(jwtAuthenticationFilter , BasicAuthenticationFilter.class)
                // note JWT Filter 적용
//                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
