package GDG.whatssue.domain.club.dto;

import GDG.whatssue.domain.member.entity.Role;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetJoinClubListResponse {

    public Long clubId;
    public String clubName;
    public String profileImage;
    public LocalDateTime createdAt;
    public Role role;
    //현재 인원 TODO

    @Builder
    public GetJoinClubListResponse(Long clubId, String clubName, String profileImage,
        LocalDateTime createdAt, Role role) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
        this.role = role;
    }
}
