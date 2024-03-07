package GDG.whatssue.domain.attendance.repository;

import GDG.whatssue.domain.attendance.entity.MemberAttendanceResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAttendanceResultRepository extends JpaRepository<MemberAttendanceResult, Long> {

}
