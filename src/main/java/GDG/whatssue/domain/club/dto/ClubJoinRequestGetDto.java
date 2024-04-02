package GDG.whatssue.domain.club.dto;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
@Getter
@AllArgsConstructor
@Builder
public class ClubJoinRequestGetDto {
    private Long id;
    private Club club;
    private User user;
}
