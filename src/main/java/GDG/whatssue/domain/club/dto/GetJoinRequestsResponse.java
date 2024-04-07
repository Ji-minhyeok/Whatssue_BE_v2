package GDG.whatssue.domain.club.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetJoinRequestsResponse {

    private String clubName;
    private LocalDateTime createdAt;

    @Builder
    public GetJoinRequestsResponse(String clubName, LocalDateTime createdAt) {
        this.clubName = clubName;
        this.createdAt = createdAt;
    }
}
