package point.zzicback.auth.application.dto.command;

public record SignUpCommand(
        String email,
        String password,
        String nickname
) {}
