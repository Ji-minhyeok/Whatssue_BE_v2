package GDG.whatssue.domain.member.entity;

import GDG.whatssue.domain.attendance.entity.MemberAttendanceResult;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.global.common.BaseEntity;
import jakarta.persistence.*;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@Setter
public class ClubMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String memberIntro;

    @Column
    private String memberName;

    @Column
    private boolean isEmailPublic;

    @Column
    private boolean isPhonePublic;

    @Column(nullable = false)
    private boolean isFirstVisit;

    @OneToOne(mappedBy = "clubMember",cascade = CascadeType.REMOVE)
    private UploadFile profileImage;

    @OneToOne(mappedBy = "clubMember",cascade = CascadeType.REMOVE)
    private MemberAttendanceResult memberAttendanceResult;

    @OneToMany(mappedBy = "clubMember",cascade = CascadeType.REMOVE)
    private List<OfficialAbsenceRequest> OfficialAbsenceRequestList;

    @Builder
    public ClubMember(Club club, User user, Role role, String memberIntro,
        String memberName, boolean isEmailPublic, boolean isPhonePublic, boolean isFirstVisit) {

        this.club = club;
        this.user = user;
        this.role = role;
        this.memberIntro = memberIntro;
        this.memberName = memberName;
        this.isPhonePublic = isPhonePublic;
        this.isEmailPublic = isEmailPublic;
        this.isFirstVisit = isFirstVisit;
    }
}
