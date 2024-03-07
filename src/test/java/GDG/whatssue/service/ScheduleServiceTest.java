//package GDG.whatssue.service;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import GDG.whatssue.domain.schedule.dto.AddScheduleRequest;
//import GDG.whatssue.domain.schedule.dto.GetScheduleResponse;
//import GDG.whatssue.domain.schedule.dto.ModifyScheduleRequest;
//import GDG.whatssue.domain.schedule.service.ScheduleService;
//import GDG.whatssue.domain.club.entity.Club;
//import GDG.whatssue.domain.schedule.entity.Schedule;
//import GDG.whatssue.domain.club.repository.ClubRepository;
//import GDG.whatssue.domain.schedule.repository.ScheduleRepository;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class ScheduleServiceTest {
//
//    @Mock
//    ScheduleRepository scheduleRepository;
//
//    @Mock
//    ClubRepository clubRepository;
//
//    @InjectMocks
//    ScheduleService scheduleService;
//
//    Schedule schedule;
//
//    @BeforeEach
//    void init() {
//        schedule = Schedule.builder()
//            .id(1L)
//            .club(null)
//            .scheduleName("test")
//            .scheduleContent("test")
//            .scheduleDateTime(LocalDateTime.now())
//            .isChecked(false)
//            .attendanceResultList(new ArrayList<>())
//            .officialAbsenceRequestList(new ArrayList<>()).build();
//    }
//
//    @Test
//    @DisplayName("일정 추가 성공")
//    void saveScheduleSuccessTest() {
//        //given
//        AddScheduleRequest requestDto =
//            AddScheduleRequest.builder()
//                .scheduleName("test")
//                .scheduleContent("test")
//                .scheduleDateTime(LocalDateTime.now())
//                .build();
//
//        Club club = new Club(
//            1L,
//            "test",
//            "test",
//            "test",
//            "test",
//            new ArrayList<>(),
//            new ArrayList<>(),
//            new ArrayList<>());
//
//        //stub
//        when(clubRepository.findById(club.getId())).thenReturn(Optional.of(club));
//
//        //when
//        scheduleService.saveSchedule(1L, requestDto);
//
//        //then
//        verify(scheduleRepository).save(any());
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 clubId로 일정 추가 실패")
//    void saveScheduleFailTest() {
//        //given
//        AddScheduleRequest requestDto =
//            AddScheduleRequest.builder()
//                .scheduleName("test")
//                .scheduleContent("test")
//                .scheduleDateTime(LocalDateTime.now()).build();
//
//        //stub
//        when(clubRepository.findById(any())).thenReturn(Optional.empty());
//
//        //when, then
//        assertThrows(NoSuchElementException.class, () -> scheduleService.saveSchedule(1L, requestDto));
//    }
//
//    @Test
//    @DisplayName("일정 조회 성공")
//    void getScheduleSuccessTest() {
//        //given
//        long scheduleId = 1L;
//
//        //stub
//        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
//
//        //when
//        GetScheduleResponse responseDto = scheduleService.findSchedule(scheduleId);
//
//        //then
//        assertThat(schedule.getScheduleName()).isEqualTo(responseDto.getScheduleName());
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 일정 id로 일정 조회 실패")
//    void getScheduleFailTest() {
//        //given
//        long scheduleId = 1L;
//
//        //stub
//        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());
//
//        //when, then
//        assertThrows(NoSuchElementException.class, () -> scheduleService.findSchedule(scheduleId));
//    }
//
//    @Test
//    @DisplayName("일정 수정 성공")
//    void updateScheduleSuccessTest() {
//        //given
//        long scheduleId = 1L;
//        ModifyScheduleRequest requestDto = ModifyScheduleRequest.builder()
//            .scheduleName("modify")
//            .scheduleContent("modify")
//            .scheduleDateTime(LocalDateTime.now()).build();
//
//        //stub
//        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
//
//        //when
//        scheduleService.updateSchedule(scheduleId, requestDto);
//
//        //then
//        verify(scheduleRepository).save(any());
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 일정 id로 수정 실패")
//    void updateScheduleFailTest() {
//        //given
//        long scheduleId = 1L;
//        ModifyScheduleRequest requestDto = ModifyScheduleRequest.builder()
//            .scheduleName("modify")
//            .scheduleContent("modify")
//            .scheduleDateTime(LocalDateTime.now()).build();
//
//        //stub
//        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());
//
//        //when, then
//        assertThrows(NoSuchElementException.class,
//            () -> scheduleService.updateSchedule(scheduleId, requestDto));
//
//    }
//
//    @Test
//    @DisplayName("일정 삭제 성공")
//    void deleteScheduleSuccessTest() {
//        //given
//        long scheduleId = 1L;
//
//        //stub
//        doNothing().when(scheduleRepository).deleteById(any());
//
//        //when
//        scheduleService.deleteSchedule(scheduleId);
//
//        //then
//        verify(scheduleRepository).deleteById(scheduleId);
//    }
//
//}