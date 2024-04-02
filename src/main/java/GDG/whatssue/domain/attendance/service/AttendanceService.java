package GDG.whatssue.domain.attendance.service;

import GDG.whatssue.domain.attendance.dto.AttendanceNumResponseDto;
import GDG.whatssue.domain.attendance.dto.ScheduleAttendanceMemberDto;
import GDG.whatssue.domain.attendance.dto.ScheduleAttendanceRequestDto;
import GDG.whatssue.domain.attendance.dto.ScheduleDto;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.global.common.AttendanceType;
import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.attendance.repository.ScheduleAttendanceResultRepository;
import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private static Map<Long, Map<Long, Integer>> attendanceNumMap = new HashMap<>();
    private final ScheduleAttendanceResultRepository scheduleAttendanceResultRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ScheduleRepository scheduleRepository;

    public AttendanceNumResponseDto openAttendance(Long clubId, Long scheduleId) throws Exception {
        Random random = new Random();
        int randomInt = random.nextInt(1, 1001);
        if (attendanceNumMap.containsKey(clubId)) {
            if (attendanceNumMap.get(clubId).containsKey(scheduleId))
                throw new Exception("이미 출석이 진행중입니다.");
        } else {
            attendanceNumMap.put(clubId, new HashMap<>());
        }
        Map<Long, Integer> innerMap = attendanceNumMap.get(clubId);
        innerMap.put(scheduleId, randomInt);
        randomInt = attendanceNumMap.get(clubId).get(scheduleId);
        AttendanceNumResponseDto attendanceNumResponseDto = AttendanceNumResponseDto.builder()
                .AttendanceNum(randomInt)
                .clubId(clubId)
                .scheduleId(scheduleId)
                .build();
        return attendanceNumResponseDto;
    }

    public List<ScheduleDto> currentAttendanceList(Long clubId) {
        List<ScheduleDto> scheduleIdList = new ArrayList<>();
        if (attendanceNumMap.containsKey(clubId)) {
            for (Long scheduleId : attendanceNumMap.get(clubId).keySet()) {
                Schedule schedule = scheduleRepository.findById(scheduleId).get();
                ScheduleDto dto = ScheduleDto.builder()
                        .scheduleId(schedule.getId())
                        .scheduleName(schedule.getScheduleName())
                        .scheduleContent(schedule.getScheduleContent())
                        .scheduleDateTime(schedule.getScheduleDateTime())
                        .isChecked(schedule.isChecked())
                        .build();
                scheduleIdList.add(dto);
            }
        }
        return scheduleIdList;
    }
    /*Delete 시에 결석자 명단을 업로드해야할까?*/
    public void deleteAttendance(Long clubId, Long scheduleId) throws Exception {
        if (attendanceNumMap.containsKey(clubId)) {
            if (attendanceNumMap.get(clubId).containsKey(scheduleId))
                attendanceNumMap.get(clubId).remove(scheduleId);
            else
                throw new Exception("출석이 진행중이지 않습니다.");
        } else {
            throw new Exception("출석이 진행중이지 않습니다.");
        }
    }
    public List<ScheduleAttendanceMemberDto> getAttendanceList(Long scheduleId, Long clubId) throws Exception {
        List<ScheduleAttendanceResult> attendanceList;
        attendanceList = scheduleAttendanceResultRepository.findByScheduleId(scheduleId);

        if (attendanceList.isEmpty()) throw new Exception("출석한 멤버가 존재하지 않습니다.");
        List<ScheduleAttendanceMemberDto> attendedMembers = (List<ScheduleAttendanceMemberDto>) attendanceList.stream().map(m -> {
            if (m.getAttendanceType().toString().equals("ATTENDANCE")) {
                return ScheduleAttendanceMemberDto.builder()
                        .clubId(clubId)
                        .scheduleId(scheduleId)
                        .clubMemberId(m.getClubMember().getId())
                        .attendanceType(m.getAttendanceType())
                        .build();
            } else return null;
        });
        return attendedMembers;
    }
    public void doAttendance(Long clubId, Long schduleId, ScheduleAttendanceRequestDto requestDto) throws Exception{

        int attendanceNum = attendanceNumMap.get(clubId).get(schduleId);
        int inputValue = requestDto.getAttendanceNum();
        if (attendanceNum == inputValue) {
            ScheduleAttendanceResult scheduleAttendanceResult = ScheduleAttendanceResult.builder()
                    .clubMember(clubMemberRepository.findById(requestDto.getClubMemberId()).get())
                    .schedule(scheduleRepository.findById(schduleId).get())
                    .attendanceType(AttendanceType.ATTENDANCE)
                    .build();
            scheduleAttendanceResultRepository.save(scheduleAttendanceResult);
        }else throw new Exception("출석번호가 일치하지 않습니다.다시 시도해 주세요");
    }
}