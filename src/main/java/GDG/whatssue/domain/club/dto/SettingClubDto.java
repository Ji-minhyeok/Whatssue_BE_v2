package GDG.whatssue.domain.club.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SettingClubDto {
    private String clubName;

    private String clubInfo;

    private String clubCategory;


}
