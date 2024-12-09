package BE.Duch_Ez.dto.user;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RegisterDto {

    @NotEmpty(message = "사용자 이름이 비어있습니다.")
    private String username;

    @NotEmpty(message = "패스워드 창이 비어있습니다.")
    private String password;

    @NotEmpty(message = "패스워드 확인 창이 비어있습니다.")
    private String password_check;

    @NotEmpty(message = "이메일 입력 창이 비어있습니다.")
    @Email
    private String email;

    @NotEmpty(message = "인증 코드를 입력해주세요")
    private String code;

}
