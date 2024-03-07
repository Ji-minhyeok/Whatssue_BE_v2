package GDG.whatssue.domain.attendance.entity;

import GDG.whatssue.domain.member.entity.ClubMember;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Getter
@Entity
public class MemberAttendanceResult {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "member_attendance_result_id")
private Long id;

@OneToOne
@JoinColumn(name = "club_id", nullable = false)
private ClubMember clubMember;

@Column(nullable = false)
private int checkCount;

@Column(nullable = false)
private int absentCount;

@Column(nullable = false)
private int officialCount;
}
