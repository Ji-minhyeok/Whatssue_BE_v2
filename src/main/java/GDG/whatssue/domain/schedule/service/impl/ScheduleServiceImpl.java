package GDG.whatssue.domain.schedule.service.impl;

import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.schedule.dto.AddScheduleRequest;
import GDG.whatssue.domain.schedule.dto.GetScheduleResponse;
import GDG.whatssue.domain.schedule.dto.ModifyScheduleRequest;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.schedule.exception.ScheduleErrorCode;
import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
import GDG.whatssue.domain.schedule.service.ScheduleService;
import GDG.whatssue.global.error.CommonException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * club에 유효한 schedule인지 체크. TODO
 */

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ClubRepository clubRepository;

    @Override
    public void saveSchedule(Long clubId, AddScheduleRequest requestDto) {
        Club club = getClub(clubId);

        Schedule saveSchedule = requestDto.toEntity(club);
        scheduleRepository.save(saveSchedule);
    }

    @Override
    @Transactional
    public void updateSchedule(Long scheduleId, ModifyScheduleRequest requestDto) {
        Schedule schedule = getSchedule(scheduleId);

        schedule.update(requestDto);

        scheduleRepository.save(schedule);
    }

    /**
     * now HardDelete
     * SoftDelete TODO
     */
    @Override
    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    @Override
    public GetScheduleResponse findSchedule(Long scheduleId) {
        Schedule schedule = getSchedule(scheduleId);

        return scheduleToGetScheduleResponse(schedule);
    }

    @Override
    public List<GetScheduleResponse> findScheduleAll(Long clubId) {
        Club findClub = getClub(clubId);

        List<Schedule> scheduleList = findClub.getScheduleList();

        return ScheduleListToResponseDtoList(scheduleList, null, null);
    }

    @Override
    public List<GetScheduleResponse> findScheduleByDay(Long clubId, String date) {
        Club findClub = getClub(clubId);

        List<Schedule> scheduleList = findClub.getScheduleList();

        return ScheduleListToResponseDtoList(scheduleList, "yyyy-MM-dd", date);
    }
    @Override
    public List<GetScheduleResponse> findScheduleByMonth(Long clubId, String date) {
        Club findClub = getClub(clubId);

        List<Schedule> scheduleList = findClub.getScheduleList();

        return ScheduleListToResponseDtoList(scheduleList, "yyyy-MM", date);
    }

    @Override
    public boolean isClubSchedule(Long clubId, Long scheduleId) {
        Schedule schedule = getSchedule(scheduleId);
        if (schedule.getClub().getId() == clubId) {
            return true;
        } else {
            return false;
        }
    }

    public List<GetScheduleResponse> ScheduleListToResponseDtoList(List<Schedule> scheduleList, String pattern, String date) {

        if (pattern==null || date==null) { //전체 조회
            return scheduleList.stream()
                .map(s -> scheduleToGetScheduleResponse(s))
                .collect(Collectors.toList());
        } else { //필터링 조회
        return scheduleList.stream()
            .filter(s-> s.getScheduleDateTime().format(DateTimeFormatter.ofPattern(pattern)).equals(date))
            .map(s -> scheduleToGetScheduleResponse(s))
            .collect(Collectors.toList());
        }
    }

    public GetScheduleResponse scheduleToGetScheduleResponse(Schedule schedule) {
        return GetScheduleResponse.builder()
            .scheduleId(schedule.getId())
            .scheduleName(schedule.getScheduleName())
            .scheduleContent(schedule.getScheduleContent())
            .scheduleDateTime(schedule.getScheduleDateTime())
            .isChecked(schedule.isChecked()).build();
    }

    public Club getClub(Long clubId) {
        return clubRepository.findById(clubId)
            .orElseThrow(() -> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));
    }

    public Schedule getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new CommonException(ScheduleErrorCode.SCHEDULE_NOT_FOUND_ERROR));
    }
}

