package GDG.whatssue.domain.user.dto;

import GDG.whatssue.domain.member.entity.Role;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDto {
    private Long userId;
    private String userName;
    private String userPhone;
    private String userEmail;
    private Role role;
    private String oauth2Id;
}
