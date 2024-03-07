package GDG.whatssue.domain.user.controller;

import GDG.whatssue.domain.user.dto.UserDto;
//import GDG.whatssue.domain.user.entity.PrincipalDetails;
//import GDG.whatssue.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
//    private final UserService userService;
//    @PostMapping("/signUp")
//    @Operation(summary = "회원가입")
//    public ResponseEntity signUp(UserDto userDto) {
//        userService.signUp(userDto);
//        return ResponseEntity.status(200).body("회원가입 성공");
//    }
    @Secured("ROLE_MANAGER")
    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "매니저 페이지입니다.";
    }
//    public ResponseEntity login() {
//        return ResponseEntity.status(200).body(userService.loadUserByUsername());
//    }
    @GetMapping({ "", "/" })
    @Operation(summary = "메인 페이지")
    public String index() {
        return "index";
    }

//    @GetMapping("/currentUser")
//    public ResponseEntity currentUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
//        UserDto userDto = userService.getUserInfo(principalDetails);
//        return ResponseEntity.status(200).body(userDto);
//    }
}
