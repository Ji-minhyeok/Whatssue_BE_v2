package GDG.whatssue.domain.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    private String userName;
    private String userPhone;
    private String userEmail;
}
