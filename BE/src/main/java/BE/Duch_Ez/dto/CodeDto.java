package BE.Duch_Ez.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotEmpty
public class CodeDto {
    @NotEmpty(message = "인증 코드를 입력해주세요")
    private String code;

}
