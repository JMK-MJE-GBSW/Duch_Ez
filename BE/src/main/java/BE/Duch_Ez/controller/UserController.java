package BE.Duch_Ez.controller;

import BE.Duch_Ez.dto.RegisterDto;
import BE.Duch_Ez.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 회원가입 정보 제출
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            return ResponseEntity.badRequest().body("입력 값이 유효하지 않습니다.");
        }

        if (!registerDto.getPassword().equals(registerDto.getPassword_check())) {
            return ResponseEntity.badRequest().body("패스워드가 일치하지 않습니다.");
        }

        try {
            userService.IsUnique(
                    registerDto.getUsername(),
                    registerDto.getEmail()
            );

            userService.ComplateRegister(
                    registerDto.getCode()
            );
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 이메일 인증 코드 전송
    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody RegisterDto registerDto) {
        try {
            userService.SendCode(
                    registerDto.getEmail(),
                    registerDto.getUsername(),
                    registerDto.getPassword()
            );


            return ResponseEntity.ok("이메일로 인증 코드를 보냈습니다. 10분 내로 입력해주세요.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이메일 전송 실패: " + e.getMessage());
        }
    }

}
