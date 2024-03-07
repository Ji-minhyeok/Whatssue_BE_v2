package GDG.whatssue.domain.user.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDto {
    private String userNick;
    private String userPw;
    private String userEmail;
    private String userName;
    private String userPhone;
}
