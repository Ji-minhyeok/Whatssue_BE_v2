package GDG.whatssue.domain.club.dto;

import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.entity.NamePolicy;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ClubCreateRequest {

    private String clubName; //클럽 이름(필수)
    private String clubIntro; //클럽 소개(필수)
    private Boolean isPrivate; //비공개 여부(필수)
    private String contactMeans; //연락수단
    private NamePolicy namePolicy;

    public Club toEntity() {
        return Club.builder()
            .clubName(this.clubName)
            .clubInfo(this.clubIntro)
            .isPrivate(this.isPrivate)
            .contactMeans(this.contactMeans)
            .namePolicy(this.namePolicy).build();
    }
}
