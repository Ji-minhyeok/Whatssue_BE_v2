package GDG.whatssue.domain.club.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ClubCreateResponse {

    Long clubId;

    @Builder
    public ClubCreateResponse(Long clubId) {
        this.clubId = clubId;
    }
}
