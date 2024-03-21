package GDG.whatssue.domain.club.controller;

import GDG.whatssue.domain.club.dto.ClubCreateRequest;
import GDG.whatssue.domain.club.service.ClubService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 모임 생성 : [POST] - /api/clubs
 * 모임 삭제 : [DELETE] - /api/clubs/{clubId}
 */
@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubServiceImpl;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity createClub(
        @RequestPart("request") ClubCreateRequest request,
        @RequestPart("profileImage") MultipartFile profileImage
        ) throws IOException {

        //user id 받아오기 & 예외처리 TODO
        long userId = 1L;

        //Validation 및 예외처리 TODO
        Long clubId = clubServiceImpl.createClub(userId, request, profileImage);

        return ResponseEntity.status(HttpStatus.OK).body(clubId);
    }

//    @DeleteMapping
//    public ResponseEntity deleteClub() {
//        //TODO
//        return null;
//    }
}