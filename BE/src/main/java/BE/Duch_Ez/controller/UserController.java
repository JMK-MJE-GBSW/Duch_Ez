package BE.Duch_Ez.controller;

import BE.Duch_Ez.dto.LoginDto;
import BE.Duch_Ez.dto.RegisterDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {


    @PostMapping("/register")
    public ResponseEntity<?> login(@Valid @RequestBody RegisterDto registerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ResponseEntity.badRequest().body("입력 값이 유효하지 않습니다.");
        }

        if (!registerDto.getPassword().equals(registerDto.getPassword_check())) {
            ResponseEntity.badRequest().body("패스워드가 일치하지 않습니다.");
        }



        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

        }


        return ResponseEntity.ok("로그인 성공");
    }

}
