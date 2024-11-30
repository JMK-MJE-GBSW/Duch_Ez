package BE.Duch_Ez.service;


import BE.Duch_Ez.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public void IsUnique (String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 등록된 사용자 이름입니다.");
        }

        if (userRepository.existsAllByEmail(email)) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        String code = GenerateVerificationCode();
        SendVerificationCode(email, code);


    }

    public String GenerateVerificationCode() {
        Random rand = new Random();
        return String.format("%06d", rand.nextInt(1000000));

    }

    public void SendVerificationCode(String email, String code) {
        String title = "더치 이지 이메일 인증 코드";
        String content = "안녕하세요, 아래 인증 코드를 입력하여 이메일 인증을 완료해주세요 \n 인증 코드 : " + code;

        try {
            MimeMessage Message = mailSender.createMimeMessage();
            MimeMessageHelper Helper = new MimeMessageHelper(Message, true, "UTF-8");
            Helper.setTo(email);
            Helper.setSubject(title);
            Helper.setText(content, true);

            mailSender.send(Message);
        } catch (Exception e) {
            throw new RuntimeException("이메일 전송 실패" + e.getMessage());
        }
    }
}
