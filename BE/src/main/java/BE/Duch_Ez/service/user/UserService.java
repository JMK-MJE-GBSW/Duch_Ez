package BE.Duch_Ez.service.user;


import BE.Duch_Ez.entity.user.UserEntity;
import BE.Duch_Ez.jwt.JwtTokenProvider;
import BE.Duch_Ez.repository.user.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final StringRedisTemplate Redis;
    private final PasswordEncoder passwordEncoder;


    //사용자 입력 중복 검사 로직
    public ResponseEntity<?> IsUnique (String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 등록된 사용자 이름입니다.");
        }

        if (userRepository.existsAllByEmail(email)) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        return ResponseEntity.ok("유효성 검사 완료");
    }

    public void SendCode(String email, String username, String password) {
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("잘못된 이메일 형식입니다.");
        }
        String code = GenerateCode();
        SendContent(email, code);

        Redis.opsForValue().set("verification:" + code, email, 10, TimeUnit.MINUTES); // 인증 코드와 이메일 연결
        Redis.opsForValue().set(email + ":username", username, 10, TimeUnit.MINUTES); // 사용자 이름
        Redis.opsForValue().set(email + ":password", password, 10, TimeUnit.MINUTES); // 비밀번호
    }

    public void ComplateRegister(String code) {
        String email = Redis.opsForValue().get("verification:" + code);
        System.out.println("이메일 : " + email);

        if (email == null) {
            throw new IllegalArgumentException("오류로 이메일을 가져오지 못함");
        }


        // Redis에서 사용자 정보 조회
        String username = Redis.opsForValue().get(email + ":username");
        String password = Redis.opsForValue().get(email + ":password");
        System.out.println("조회된 사용자명: " + username);
        System.out.println("조회된 비밀번호: " + password);

        if (username == null || password == null) {
            throw new IllegalArgumentException("회원가입 정보가 유효하지 않습니다.");
        }

        // DB에 사용자 저장
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화 저장
        userRepository.save(user);

        // Redis 데이터 삭제
        Redis.delete("verification:" + code);  // 인증 코드 삭제
        Redis.delete(email + ":username");
        Redis.delete(email + ":password");
    }
    //사용자의 이매일에 보내지는 화면
    public void SendContent(String email, String code) {
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

    //이메일이 올바른지 확인하는 정규식 로직
    public static boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //보낼 인증번호를 랜덤으로 생성하는 로직
    public String GenerateCode( ) {
        Random rand = new Random();
        return String.format("%06d", rand.nextInt(1000000));

    }

    ///token

    private final JwtTokenProvider jwtTokenProvider;
    public String generateToken(String username, Long userId) {
        return jwtTokenProvider.createToken(username, userId); // ID와 username을 함께 전달
    }


    public boolean TokenCheck(String authorizationHeader) {
        // Authorization 헤더가 비어있거나 Bearer로 시작하지 않으면 유효하지 않음
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return false;
        }

        // "Bearer " 접두사를 제거하여 실제 토큰 추출
        String token = authorizationHeader.substring(7);

        // JwtTokenProvider로 토큰 유효성 검사
        return jwtTokenProvider.validateToken(token);
    }

    public Long extractUserIdFromToken(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return jwtTokenProvider.getUserIdFromToken(token);
    }


}
