package GDG.whatssue.domain.user.dto;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class modifiyRequestDto {
    private String userName;
    private String userPhone;
    private String Role;
}


