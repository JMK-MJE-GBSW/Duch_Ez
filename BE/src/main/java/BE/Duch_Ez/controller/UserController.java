package BE.Duch_Ez.controller;

import BE.Duch_Ez.dto.user.LoginDto;
import BE.Duch_Ez.dto.user.RegisterDto;
import BE.Duch_Ez.entity.user.UserEntity;
import BE.Duch_Ez.repository.user.UserRepository;
import BE.Duch_Ez.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")

public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    @PostMapping("/send-code") //이거 좆같이 비효율적이네;;일단 ㄱㄷ
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


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        try {
            // 사용자 조회
            UserEntity user = userRepository.findByUsername(loginDto.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            // 비밀번호 검증
            if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body("비밀번호가 올바르지 않습니다.");
            }

            // JWT 생성
            String token = userService.generateToken(user.getUsername(), user.getId());

            // 토큰 반환
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인 오류" + e.getMessage());
        }
    }



}
