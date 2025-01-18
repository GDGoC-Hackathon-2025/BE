package gdg.baekya.hackathon.member.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    OAUTH_FIRST_JOIN ("OAUTH_FIRST_JOIN"),
    USER ("USER"),
    ADMIN ("ADMIN"),
    CROWD("CROWD");

    private final String role;

    @JsonValue
    public String getRole() {
        return role;
    }
    }
