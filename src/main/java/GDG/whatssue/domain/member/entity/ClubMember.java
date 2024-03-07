package GDG.whatssue.domain.member.entity;

import GDG.whatssue.domain.attendance.entity.MemberAttendanceResult;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.domain.user.entity.User;
import GDG.whatssue.global.common.BaseEntity;
import GDG.whatssue.global.common.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToOne(mappedBy = "clubMember")
    private MemberAttendanceResult memberAttendanceResult;

    @OneToMany(mappedBy = "clubMember")
    private List<OfficialAbsenceRequest> OfficialAbsenceRequestList;

}
