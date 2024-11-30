package BE.Duch_Ez.dto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginDto {

    @NotEmpty(message = "사용자 이름이 비어있습니다.")
    private String username;

    @NotEmpty(message = "비밀번호가 비어있습니다.")
    private String password;

}
