package GDG.whatssue.domain.club.service.impl;

import static GDG.whatssue.global.common.FileConst.*;

import GDG.whatssue.domain.club.dto.ClubCreateRequest;
import GDG.whatssue.domain.club.dto.ClubCreateResponse;
import GDG.whatssue.domain.club.dto.GetClubInfoResponse;
import GDG.whatssue.domain.club.dto.GetJoinClubListResponse;
import GDG.whatssue.domain.club.dto.UpdateClubInfoRequest;
import GDG.whatssue.domain.club.entity.Club;
import GDG.whatssue.domain.club.exception.ClubErrorCode;
import GDG.whatssue.domain.club.repository.ClubRepository;
import GDG.whatssue.domain.club.service.ClubService;
import GDG.whatssue.domain.file.entity.UploadFile;
import GDG.whatssue.domain.file.repository.FileRepository;
import GDG.whatssue.domain.file.service.FileUploadService;
import GDG.whatssue.domain.member.entity.ClubMember;
import GDG.whatssue.domain.member.repository.ClubMemberRepository;
import GDG.whatssue.domain.user.repository.UserRepository;
import GDG.whatssue.domain.member.entity.Role;
import GDG.whatssue.global.error.CommonException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final FileRepository fileRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;

    @Override
    public List<GetJoinClubListResponse> getJoinClubList(Long userId) {
        List<ClubMember> clubMembers = clubMemberRepository.findByUser_UserId(userId);
        if (clubMembers.isEmpty()) {
            return new ArrayList<>();
        }

        List<GetJoinClubListResponse> responseList = clubMembers.stream()
            .map(c -> entityToJoinClubListResponse(c))
            .collect(Collectors.toList());

        return responseList;
    }

    @Override
    public ClubCreateResponse createClub(Long userId, ClubCreateRequest requestDto, MultipartFile profileImage) throws IOException {
        //초대코드 추가하여 클럽 생성
        Club savedClub = clubRepository.save(createDtoToClubEntity(requestDto));

        if (profileImage != null) {
            saveProfileImage(profileImage, savedClub);
        }

        //클럽을 생성한 멤버(관리자)로 추가
        //따로 메서드로 뺄것 TODO
        clubMemberRepository.save(
            ClubMember.builder()
            .club(savedClub)
            .user(userRepository.findById(userId).get())
            .role(Role.MANAGER)
            .isFirstVisit(true).build());

        return ClubCreateResponse.builder()
            .clubId(savedClub.getId()).build();
    }

    @Override
    public void updateClubInfo(Long clubId, UpdateClubInfoRequest requestDto, MultipartFile profileImage) throws IOException {
        Club club = getClub(clubId);

        club.updateClubInfo(requestDto);
        clubRepository.save(club);

        if (club.getProfileImage() != null) { //기존 프사 있는 경우 삭제
            deleteProfileImage(club);
        }

        if (profileImage != null) { //헤더가 있는 경우 업로드 -> 사진 유지 또는 변경
            saveProfileImage(profileImage, club);
        }
    }

    @Override
    @Transactional
    public void updateClubPrivateStatus(Long clubId) {
        Club club = getClub(clubId);

        club.updateIsPrivate();
    }

    @Override
    public GetClubInfoResponse getClubInfo(Long clubId) {
        Club club = getClub(clubId);

        String storeFileName = "";
        UploadFile profileImage = club.getProfileImage();

        if (profileImage != null) {
            storeFileName = profileImage.getStoreFileName();
        }

        if (profileImage == null) {
            storeFileName = CLUB_PROFILE_IMAGE_DIRNAME + DEFAULT_IMAGE_NAME;
        }

        String fullPath = fileUploadService.getFullPath(storeFileName);

        return GetClubInfoResponse.builder()
            .clubName(club.getClubName())
            .clubIntro(club.getClubIntro())
            .contactMeans(club.getContactMeans())
            .namePolicy(club.getNamePolicy())
            .privateCode(club.getPrivateCode())
            .profileImage(fullPath)
            .isPrivate(club.isPrivate()).build();
    }

    public GetJoinClubListResponse entityToJoinClubListResponse(ClubMember clubMember) {
        Club club = clubMember.getClub();

        String storeFileName = "";
        UploadFile profileImage = club.getProfileImage();

        if (profileImage != null) {
            storeFileName = profileImage.getStoreFileName();
        }

        if (profileImage == null) {
            storeFileName = CLUB_PROFILE_IMAGE_DIRNAME + DEFAULT_IMAGE_NAME;
        }

        String fullPath = fileUploadService.getFullPath(storeFileName);

        return GetJoinClubListResponse.builder()
            .clubId(club.getId())
            .clubName(club.getClubName())
            .profileImage(fullPath)
            .createdAt(clubMember.getCreateAt())
            .role(clubMember.getRole()).build();
    }


    @Override
    @Transactional
    public void updateClubCode(Long clubId) {
        Club club = getClub(clubId);

        club.createNewPrivateCode();
    }

    @Override
    public boolean isClubExist(Long clubId) {
        clubRepository.findById(clubId)
            .orElseThrow(()-> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));

        return true;
    }

    @Transactional
    public void saveProfileImage(MultipartFile profileImage, Club savedClub) throws IOException {
        String originalFileName = profileImage.getOriginalFilename();
        //profileImage fileRepository 및 s3에 저장 처리
        String storeFileName = fileUploadService.saveFile(profileImage, CLUB_PROFILE_IMAGE_DIRNAME);

        fileRepository.save(UploadFile.builder()
            .club(savedClub)
            .uploadFileName(originalFileName)
            .storeFileName(storeFileName).build());
    }

    public void deleteProfileImage(Club club) {
        fileUploadService.deleteFile(club.getProfileImage().getStoreFileName()); //s3에서 삭제
        fileRepository.deleteById(club.getProfileImage().getId()); //레포에서 삭제
    }

    public Club createDtoToClubEntity(ClubCreateRequest requestDto) {
        Club club = requestDto.toEntity();
        club.createNewPrivateCode();

        return club;
    }

    public Club getClub(Long clubId) {
        return clubRepository.findById(clubId).orElseThrow(()
            -> new CommonException(ClubErrorCode.CLUB_NOT_FOUND_ERROR));
    }
}

