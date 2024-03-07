package GDG.whatssue.domain.memberJoin.dto;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.user.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
