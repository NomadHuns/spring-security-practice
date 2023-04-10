package shop.mtcoding.securityapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. CSRF 해제
        http.csrf().disable(); // csrf 공격을 막는 기능을 비활성화 (이유: 포스트맨으로 테스트할 수 있도록 하기 위해서 - CSR 할때 사용)

        // 2. Form 로그인 설정
        http.formLogin()
                .loginPage("/loginForm")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login") // Post + X-WWW-FormUrlEncoded
                // .defaultSuccessUrl("/")
                .successHandler((req, resp, authenication) -> {
                    System.out.println("디버그 : 로그인이 완료되었습니다.");
                    resp.sendRedirect("/");
                })
                .failureHandler((req, resp, ex) -> {
                    System.out.println("디버그 : 로그인 실패 -> " + ex.getMessage());
                });

        // 3. 인증, 권한 필터 설정
        http.authorizeRequests((authorize) -> {
            authorize.antMatchers("/user/**").authenticated() // /user url은 인증, 권한 검사 필요
                    .antMatchers("/manager/**").access("hasRole('ADMIN') or hasRole('MANAGER')")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().permitAll(); // 그 외의 모든 요청은 인증, 권한 검사 필요 없음.
        });

        return http.build();
    }
}
