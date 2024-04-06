package GDG.whatssue.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubMemberDto {
    private String memberIntro;
    private String memberName;
    private boolean isEmailPublic;
    private boolean isPhonePublic;
}
