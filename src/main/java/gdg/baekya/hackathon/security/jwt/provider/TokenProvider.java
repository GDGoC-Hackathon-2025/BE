package gdg.baekya.hackathon.security.jwt.provider;

import gdg.baekya.hackathon.domain.member.Member;
import gdg.baekya.hackathon.domain.member.MemberRepository;
import gdg.baekya.hackathon.security.oauth.domain.PrincipalDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.INVALID_TOKEN;

@RequiredArgsConstructor
@Component
@Log4j2
public class TokenProvider {

    @Value("${jwt.key}")
    private String key;
    private SecretKey secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 7;
    private final MemberRepository memberRepository;

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
    }

    // 1. refresh token 발급
    public void generateRefreshToken(Authentication authentication, String accessToken) {
        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String generateToken(Authentication authentication, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        // 권한 리스트 추출
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)  // 권한을 String으로 변환
                .collect(Collectors.toList());

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", authorities);
        claims.put("user_id", principalDetails.getId());

        return Jwts.builder()
                .setSubject(authentication.getName())// 사용자 이름 (subject)
                .setClaims(claims)
                .setIssuedAt(now)                                // 발급 시간
                .setExpiration(expiredDate)                      // 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS512)   // 서명
                .compact();
        // 토큰 반환
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);  // JWT 클레임 파싱

        // 권한 정보 가져오기
        Object roles = claims.get("roles");  // JWT에서 roles 정보 가져오기
        List<String> authoritiesList = new ArrayList<>();

        if (roles instanceof List) {
            // roles가 List 형식일 때
            authoritiesList = (List<String>) roles;
        } else if (roles instanceof String) {
            // roles가 String 형식일 때 (콤마로 구분된 역할)
            authoritiesList = Arrays.asList(((String) roles).split(","));
        }

        // 권한을 SimpleGrantedAuthority로 변환
        Collection<? extends GrantedAuthority> authorities = authoritiesList.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))  // ROLE_ 접두사 추가
                .collect(Collectors.toList());

        // userId를 Long으로 안전하게 변환
        Long userId = claims.get("user_id", Long.class);

        // 해당 userId로 Member를 조회
        Member member = memberRepository.findById(userId).orElseThrow();
        PrincipalDetails principalDetails = new PrincipalDetails(member);

        // UsernamePasswordAuthenticationToken 반환
        return new UsernamePasswordAuthenticationToken(principalDetails, token, authorities);
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        Claims claims = parseClaims(token);
        return claims.getExpiration().after(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            // JWT 파서를 빌드하고 서명된 토큰을 파싱
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)  // 서명 키를 설정
                    .build()
                    .parseClaimsJws(token)  // 서명된 JWT 토큰을 파싱
                    .getBody();  // Claims 객체 반환
        } catch (ExpiredJwtException e) {
            // 만료된 JWT 토큰의 경우 만료된 Claims 반환
            return e.getClaims();
        } catch (MalformedJwtException e) {
            // 잘못된 JWT 토큰 형식일 경우 예외 처리
            throw new CustomJWTException(INVALID_TOKEN);
        }
    }

}