package gdg.baekya.hackathon.domain.member;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    OAUTH_FIRST_JOIN ("OAUTH_FIRST_JOIN"),
    USER ("USER"),
    ADMIN ("ADMIN"),
    EMPLOYER("EMPLOYER"),
    BANNER ("BANNER"),
    VIP ("VIP"),
    VVIP ("VVIP"),
    SVIP ("SVIP");

    private final String role;

    @JsonValue
    public String getRole() {
        return role;
    }
    }
