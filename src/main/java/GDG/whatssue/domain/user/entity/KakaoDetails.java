package GDG.whatssue.domain.user.entity;

import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class KakaoDetails implements OAuth2User{

    private User user;
    private String registrationId;
    private Map<String,Object> attributes;
    private Collection<? extends GrantedAuthority> authorities;
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        for (ClubMember clubMember : clubMemberList) {
//            authorities.add((GrantedAuthority) () -> {
//                Long clubId = clubMember.getClub().getId();
//                Role role = clubMember.getRole();
//                System.out.println("ROLE_" + clubId + role);
//                return "ROLE_" + clubId + role;
//            });
//        }
        return authorities;
    }

    @Override
    public String getName() {
        return attributes.get("id").toString();
    }
}
