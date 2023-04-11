package shop.mtcoding.securityapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

        // 2. iframe 거부
        http.headers().frameOptions().disable();

        // 3. cors 재설정
        http.cors().configurationSource(configurationSource());

        // 4. jSessionId 사용 거부 (STATELESS 정책)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 5. form 로긴 해제
        http.formLogin().disable();

        // 6. httpBasic 정책 해제
        http.httpBasic().disable();

        // XSS (lucy 필터)

        // 3. Form 로그인 설정
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

        // 4. 인증, 권한 필터 설정
        http.authorizeRequests((authorize) -> {
            authorize.antMatchers("/user/**").authenticated() // /user url은 인증, 권한 검사 필요
                    .antMatchers("/manager/**").access("hasRole('ADMIN') or hasRole('MANAGER')")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().permitAll(); // 그 외의 모든 요청은 인증, 권한 검사 필요 없음.
        });

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        System.out.println("디버그 : configurationSource cors 설정이 SecurityFilterChain에 등록됨");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*"); // 모든 헤더를 허용
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (프론트 앤드 IP만 허용 react)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization"); // 옛날에는 디폴트 였다. 지금은 아닙니다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
