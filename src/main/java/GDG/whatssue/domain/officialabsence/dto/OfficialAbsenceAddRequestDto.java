package GDG.whatssue.domain.officialabsence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OfficialAbsenceAddRequestDto {

    private Long clubMemberId;
    private Long scheduleId;
    private String officialAbsenceContent;

}
