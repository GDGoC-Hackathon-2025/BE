package gdg.baekya.hackathon.config;

import gdg.baekya.hackathon.security.jwt.filter.TokenAuthenticationFilter;
import gdg.baekya.hackathon.security.jwt.handler.OAuth2LoginSuccessHandler;
import gdg.baekya.hackathon.security.jwt.provider.TokenProvider;
import gdg.baekya.hackathon.security.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2LoginSuccessHandler loginSuccessHandler;
    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // ookie를 사용하지 않으면 꺼도 된다. (cookie를 사용할 경우 httpOnly(XSS 방어), sameSite(CSRF 방어)로 방어해야 한다.)
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())

                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable())
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable())
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable())
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.disable())
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.disable())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.disable())

                .authorizeHttpRequests(request -> request.anyRequest().permitAll())


                // oauth2 설정
                .oauth2Login(oauth -> // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                        // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
                        oauth.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                        .userService(oAuth2UserService))
                                // 로그인 성공 시 핸들러
                                .successHandler(loginSuccessHandler)
                )

                // jwt 설정

                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)

//                 인증 예외 핸들링
//                .exceptionHandling(exceptionHandling -> exceptionHandling.disable())

                .build();
    }
}


