package gdg.baekya.hackathon.member.domain;

import gdg.baekya.hackathon.member.domain.Member;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Getter
public class PrincipalDetails implements OAuth2User, Serializable {

    private final Member member;

    // 첫 번째 생성자: Member만 받는 생성자
    public PrincipalDetails(Member member) {
        this.member = member;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getRoles().stream().map(o -> new SimpleGrantedAuthority(
                o.getRole()
        )).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        // OAuth2User의 고유 식별자 (예: 이메일)
        return member.getEmail();
    }

    public String getUsername() {
        return member.getUsername();
    }

    public Long getId() {
        return member.getId();
    }
}
