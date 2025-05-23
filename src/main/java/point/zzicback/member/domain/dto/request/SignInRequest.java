package point.zzicback.member.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import point.zzicback.member.domain.dto.SignInCommand;

@Schema(description = "사용자 로그인 요청 DTO")
public record SignInRequest(

        @Schema(description = "사용자 이메일", example = "user@example.com")
        @NotBlank(message = "이메일은 필수 입력 항목입니다.")
        @Email(message ="{email.valid}")
        String email,

        @Schema(description = "사용자 비밀번호", example = "Strong@123")
        @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
        String password
) {
    public SignInCommand toCommand() {
        return new SignInCommand(email, password);
    }
}
