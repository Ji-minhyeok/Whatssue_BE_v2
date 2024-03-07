package GDG.whatssue.domain.schedule.dto;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.schedule.entity.Schedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AddScheduleRequest {

    @NotBlank
    private String scheduleName;
    @NotBlank
    private String scheduleContent;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime scheduleDateTime;

    public Schedule toEntity(Club club) {
        return Schedule.builder()
            .club(club)
            .scheduleName(scheduleName)
            .scheduleContent(scheduleContent)
            .scheduleDateTime(scheduleDateTime)
            .isChecked(false).build();
    }
}
