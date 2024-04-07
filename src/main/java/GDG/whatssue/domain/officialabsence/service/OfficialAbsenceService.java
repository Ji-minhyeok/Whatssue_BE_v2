package GDG.whatssue.domain.officialabsence.service;

import static GDG.whatssue.global.common.AttendanceType.ABSENCE;
import static GDG.whatssue.global.common.AttendanceType.OFFICIAL_ABSENCE;

import GDG.whatssue.domain.attendance.entity.ScheduleAttendanceResult;
import GDG.whatssue.domain.attendance.repository.ScheduleAttendanceResultRepository;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceAddRequestDto;
import GDG.whatssue.domain.officialabsence.dto.OfficialAbsenceGetRequestDto;
import GDG.whatssue.domain.officialabsence.entity.OfficialAbsenceRequest;
import GDG.whatssue.domain.officialabsence.repository.OfficialAbsenceRequestRepository;
import GDG.whatssue.domain.schedule.entity.Schedule;
import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OfficialAbsenceService {
    private final ScheduleRepository scheduleRepository;
    private final OfficialAbsenceRequestRepository officialAbsenceRequestRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ScheduleAttendanceResultRepository scheduleAttendanceResultRepository;
    private static final Logger logger = LoggerFactory.getLogger(OfficialAbsenceService.class);

    public void createOfficialAbsenceRequest(Long scheduleId, OfficialAbsenceAddRequestDto officialAbsenceAddRequestDto) { //공결 신청 생성
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);

        //예외처리
        Schedule schedule = optionalSchedule.orElseThrow(() ->
            new NoSuchElementException("No schedule found for scheduleId: " + scheduleId));


        ClubMember clubMember = clubMemberRepository.findById(officialAbsenceAddRequestDto.getClubMemberId())
            .orElseThrow(() -> new NoSuchElementException("No club member found for memberId: " + officialAbsenceAddRequestDto.getClubMemberId()));
        String officialAbsenceContent = officialAbsenceAddRequestDto.getOfficialAbsenceContent();

        ClubMember clubMemberEntity = clubMemberRepository.findById(clubMember.getId()).get();

        OfficialAbsenceRequest officialAbsenceRequest = OfficialAbsenceRequest.builder()
            .clubMember(clubMemberEntity)
            .schedule(schedule)
            .officialAbsenceContent(officialAbsenceContent)
            .isChecked(false)
            .build();

        officialAbsenceRequestRepository.save(officialAbsenceRequest);

    }

    public List<OfficialAbsenceGetRequestDto> getAllOfficialAbsenceRequests() { //공결 신청 현황, 내역 List 조회
        /**모든 현황**/
        List<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findAll();
        return officialAbsenceRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public List<OfficialAbsenceGetRequestDto> getOfficialAbsenceRequests() { //공결 신청 현황 List 조회
        List<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findByIsChecked(false);
        /**isChecked false 필터링(수락대기중)**/
        return officialAbsenceRequests.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    public List<OfficialAbsenceGetRequestDto> getDoneOfficialAbsenceRequests() { //공결 신청 내역 List 조회
        /**isChecked true 필터링(수락 승인 or거절 완료)**/
        List<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findByIsChecked(true);
        return officialAbsenceRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public OfficialAbsenceGetRequestDto getOfficialAbsenceRequestDetail(Long officialAbsenceId){
        /**특정 단일 공결 신청 조회**/
        OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findById(officialAbsenceId).get();
        if (officialAbsenceRequest == null) {
            throw new NoSuchElementException("해당하는 공결 요청이 존재하지 않습니다.");
        }
        OfficialAbsenceGetRequestDto officialAbsenceGetRequestDto = OfficialAbsenceGetRequestDto.builder()
                .id(officialAbsenceRequest.getId())
                .clubMemberId(officialAbsenceRequest.getClubMember().getId())
                .scheduleId(officialAbsenceRequest.getSchedule().getId())
                .officialAbsenceContent(officialAbsenceRequest.getOfficialAbsenceContent())
                .build();

        return officialAbsenceGetRequestDto;
    }

    public List<OfficialAbsenceGetRequestDto> getMyDoneOfficialAbsenceRequests(Long clubMemberId) { //내 공결 신청 내역 List 조회
        // clubMemberId 유효성 검사
        if (clubMemberId == null || clubMemberId <= 0) {
            throw new IllegalArgumentException("Invalid club member ID: " + clubMemberId);
        }
        //isChecked true 필터링(수락 승인 or거절 완료)
        List<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findByClubMemberIdAndIsChecked(clubMemberId, true);
        // 공결 신청 내역이 없는 경우
        if (officialAbsenceRequests.isEmpty()) {
            throw new NoSuchElementException("해당하는 공결 요청이 존재하지 않습니다.");
        }
        return officialAbsenceRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public List<OfficialAbsenceGetRequestDto> getMyOfficialAbsenceRequests(Long clubMemberId) { //내 공결 신청 내역 List 조회
        // clubMemberId 유효성 검사
        if (clubMemberId == null || clubMemberId <= 0) {
            throw new IllegalArgumentException("Invalid club member ID: " + clubMemberId);
        }
        List<OfficialAbsenceRequest> officialAbsenceRequests = officialAbsenceRequestRepository.findByClubMemberIdAndIsChecked(clubMemberId, false);
        // 공결 신청 내역이 없는 경우
        if (officialAbsenceRequests.isEmpty()) {
            throw new NoSuchElementException("해당하는 공결 요청이 존재하지 않습니다.");
        }
        return officialAbsenceRequests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    private OfficialAbsenceGetRequestDto convertToDto(OfficialAbsenceRequest officialAbsenceRequest) {
        /**isChecked DTO에 필요해? 확인 필요**/
        return OfficialAbsenceGetRequestDto.builder()
            .id(officialAbsenceRequest.getId())
            .clubMemberId(officialAbsenceRequest.getClubMember().getId())
            .scheduleId(officialAbsenceRequest.getSchedule().getId())
            .officialAbsenceContent(officialAbsenceRequest.getOfficialAbsenceContent())
            .build();
    }
    @Transactional
    public void deleteOfficialAbsences(Long officialAbsenceId,Long clubMemberId) {
        // clubMemberId 유효성 검사
        if (clubMemberId == null || clubMemberId <= 0) {
            throw new IllegalArgumentException("Invalid club member ID: " + clubMemberId);
        }
        OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findById(officialAbsenceId).get();
        if(officialAbsenceRequest == null){
            throw new NoSuchElementException("해당하는 공결 요청이 존재하지 않습니다.");
        }
        officialAbsenceRequestRepository.delete(officialAbsenceRequest);
    }


    @Transactional
    public void acceptResponse(Long officialAbsenceId) {
        try {
            OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findById(officialAbsenceId)
                .orElseThrow(() -> new NoSuchElementException("officialAbsenceId에 해당하는 공결신청이 없습니다.: " + officialAbsenceId));

            Schedule schedule = scheduleRepository.findById(officialAbsenceRequest.getSchedule().getId())
                .orElseThrow(() -> new NoSuchElementException("scheduleId에 해당하는 일정이 없습니다.: " + officialAbsenceRequest.getSchedule().getId()));

            ClubMember clubMember = clubMemberRepository.findById(officialAbsenceRequest.getClubMember().getId())
                .orElseThrow(() -> new NoSuchElementException("clubMemberId에 해당하는 멤버가 없습니다.: " + officialAbsenceRequest.getClubMember().getId()));

            Long scheduleId = schedule.getId();
            Long clubMemberId = clubMember.getId();

            ScheduleAttendanceResult scheduleAttendanceResult = scheduleAttendanceResultRepository
                .findByScheduleIdAndClubMemberId(scheduleId, clubMemberId);

            if (scheduleAttendanceResult != null) {
                //공결 수락
                scheduleAttendanceResult.setAttendanceType(OFFICIAL_ABSENCE);
                logger.warn("AttendanceType changed.");
                // isChecked 값을 true로 설정
                officialAbsenceRequest.setChecked(true);
                logger.warn("isChecked true.");
            } else {
                logger.warn("No ScheduleAttendanceResult found for scheduleId: {} and clubMemberId: {}", scheduleId, clubMemberId);
            }
        } catch (NoSuchElementException e) {
            logger.warn("Error processing acceptResponse: {}", e.getMessage());
        }
    }
    @Transactional
    public void denyResponse(Long officialAbsenceId) { // 공결 신청 거절
        try {
            OfficialAbsenceRequest officialAbsenceRequest = officialAbsenceRequestRepository.findById(officialAbsenceId)
                .orElseThrow(() -> new NoSuchElementException("No OfficialAbsenceRequest found for officialAbsenceId: " + officialAbsenceId));

            Schedule schedule = scheduleRepository.findById(officialAbsenceRequest.getSchedule().getId())
                .orElseThrow(() -> new NoSuchElementException("No Schedule found for scheduleId: " + officialAbsenceRequest.getSchedule().getId()));

            ClubMember clubMember = clubMemberRepository.findById(officialAbsenceRequest.getClubMember().getId())
                .orElseThrow(() -> new NoSuchElementException("No ClubMember found for clubMemberId: " + officialAbsenceRequest.getClubMember().getId()));

            Long scheduleId = schedule.getId();
            Long clubMemberId = clubMember.getId();

            ScheduleAttendanceResult scheduleAttendanceResult = scheduleAttendanceResultRepository
                .findByScheduleIdAndClubMemberId(scheduleId, clubMemberId);

            if (scheduleAttendanceResult != null) {
                //공결 거부
                scheduleAttendanceResult.setAttendanceType(ABSENCE);
                logger.warn("AttendanceType changed.");
                // isChecked 값을 true로 설정
                officialAbsenceRequest.setChecked(true);
                logger.warn("isChecked true.");
            } else {
                logger.warn("No ScheduleAttendanceResult found for scheduleId: {} and clubMemberId: {}", scheduleId, clubMemberId);
            }
        } catch (NoSuchElementException e) {
            logger.warn("Error processing acceptResponse: {}", e.getMessage());
        }
    }
}