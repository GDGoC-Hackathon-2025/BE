package gdg.baekya.hackathon.security.jwt.filter;

import gdg.baekya.hackathon.security.jwt.provider.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Access Token 추출
        String accessToken = resolveToken(request);

        // Access Token 검증
        if (accessToken != null && tokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken); // 유효한 토큰일 경우 인증 설정
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * SecurityContext에 인증 정보 설정
     * @param accessToken 유효한 Access Token
     */
    private void setAuthentication(String accessToken) {
        try {
            // Access Token을 통해 인증 객체 생성
            Authentication authentication = tokenProvider.getAuthentication(accessToken);

            // 인증 정보를 SecurityContext에 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            log.error("Authentication failed", e); // 인증 실패 시 로깅
            SecurityContextHolder.clearContext(); // 인증 실패 시 컨텍스트 초기화
        }
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // Authorization 쿠키가 없을 경우 null 반환
    }
}
