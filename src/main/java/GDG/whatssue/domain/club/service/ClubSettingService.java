package GDG.whatssue.domain.club.service;

import GDG.whatssue.domain.club.dto.SettingClubDto;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

/**예외 처리 안되어 controller 상에서 메소드 실행 후 완료로 표시될 수 있음**/
@Service
@RequiredArgsConstructor
public class ClubSettingService {
    private final ClubRepository clubRepository;
    private static final Logger logger = LoggerFactory.getLogger(ClubSettingService.class);

    public void createClub(SettingClubDto settingClubDto) {//클럽 생성
        String clubCode;
        do {
            clubCode = generateRandomClubCode();
        } while (clubRepository.existsByClubCode(clubCode)); // 중복 체크
        Club club = Club.builder()
                .clubName(settingClubDto.getClubName())
                .clubInfo(settingClubDto.getClubInfo())
                .clubCategory(settingClubDto.getClubCategory())
                .clubCode(clubCode) // 랜덤 클럽 코드 생성
                .build();
        clubRepository.save(club);
    }

    private String generateRandomClubCode() {//클럽 수정
        // 랜덤한 정수를 생성하여 클럽 코드로 사용
        return String.valueOf(new Random().nextInt(90000) + 10000);
    }

    public void modifyClub(Long clubId, SettingClubDto settingClubDto) {
        Optional<Club> optionalClub = clubRepository.findById(clubId);

        if (optionalClub.isPresent()) { //존재 시 수행
            Club club = optionalClub.get();
            club.setClubName(settingClubDto.getClubName());
            club.setClubInfo(settingClubDto.getClubInfo());
            club.setClubCategory(settingClubDto.getClubCategory());

            // 수정된 클럽을 저장
            clubRepository.save(club);
        } else {
            logger.warn("모임을 찾을 수 없습니다.");
        }
    }

    public void deleteClub(Long clubId) {
        Optional<Club> optionalClub = clubRepository.findById(clubId);

        if (optionalClub.isPresent()) { //존재 시 수행
            clubRepository.delete(optionalClub.get());
        } else {
            logger.warn("모임을 찾을 수 없습니다.");
        }
    }
}
