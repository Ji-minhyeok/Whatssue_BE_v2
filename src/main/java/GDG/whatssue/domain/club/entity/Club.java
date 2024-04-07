package GDG.whatssue.domain.club.entity;


import GDG.whatssue.domain.club.dto.UpdateClubInfoRequest;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.global.common.BaseEntity;
import jakarta.persistence.*;
import java.util.List;

import java.util.UUID;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class Club extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;


    @Column(nullable = false)
    private String clubName;

    @Column(nullable = false)
    private String clubIntro;

    @Column(nullable = false)
    private boolean isPrivate;

    @Column
    private String contactMeans;

    @Column(nullable = false)
    private String privateCode;

    @OneToOne(mappedBy = "club")
    private UploadFile profileImage;

    @OneToMany(mappedBy = "club")
    private List<ClubMember> clubMemberList;

    @OneToMany(mappedBy = "club")
    private List<ClubJoinRequest> clubJoinRequestList;

    @OneToMany(mappedBy = "club")
    private List<Schedule> scheduleList;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NamePolicy namePolicy;

    public void updateIsPrivate() {
        this.isPrivate = !this.isPrivate;
    }

    public void createNewPrivateCode() {
        this.privateCode = UUID.randomUUID().toString().substring(0, 6);
    }

    public void updateClubInfo(UpdateClubInfoRequest requestDto) {
        this.clubName = requestDto.getClubName();
        this.clubIntro = requestDto.getClubIntro();
        this.contactMeans = requestDto.getContactMeans();
    }

    @Builder
    public Club(String clubName, String clubInfo, boolean isPrivate, String contactMeans, NamePolicy namePolicy) {
        this.clubName = clubName;
        this.clubIntro = clubInfo;
        this.isPrivate = isPrivate;
        this.contactMeans = contactMeans;
        this.namePolicy = namePolicy;
    }
}
