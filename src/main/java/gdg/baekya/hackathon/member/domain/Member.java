package gdg.baekya.hackathon.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Member implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    private String nickname;

    @Column
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();


    private LocalDateTime createdAt;

    // 생성자
    public static Member of(String email, String username, String phoneNumber) {
        List<Role> roles = new ArrayList<>();

        Member member = Member.builder()
                .username(username)
                .email(email)
                .phoneNumber(phoneNumber)
                .roles(roles)
                .createdAt(LocalDateTime.now())
                .build();

        member.addMemberRole(Role.OAUTH_FIRST_JOIN);
        member.addMemberRole(Role.USER);
        return member;
    }


    // ROLE 관련 로직
    public void addMemberRole(Role role) {
        if (!this.roles.contains(role)) { // 중복 방지
            this.roles.add(role);
        }
    }

    public void removeMemberRole(Role role) {
        this.roles.remove(role); // Role 객체를 직접 삭제
    }

    // 이메일 추가 로직
    public void changeEmail(String email) {
        this.email = email;
    }

    // 유저 이름 추가 로직
    public void changeUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }


}