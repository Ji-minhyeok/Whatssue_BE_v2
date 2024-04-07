package GDG.whatssue.domain.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetScheduleResponse {

    private Long scheduleId;
    private String scheduleName;
    private String scheduleContent;

    @JsonFormat(pattern = "yyyy년 MM월 dd일 HH시 mm분")
    private LocalDateTime scheduleDateTime;

    private boolean isChecked;

    @Builder
    public GetScheduleResponse(Long scheduleId, String scheduleName, String scheduleContent,
        LocalDateTime scheduleDateTime, boolean isChecked) {
        this.scheduleId = scheduleId;
        this.scheduleName = scheduleName;
        this.scheduleContent = scheduleContent;
        this.scheduleDateTime = scheduleDateTime;
        this.isChecked = isChecked;
    }
}
