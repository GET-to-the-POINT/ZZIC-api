package point.zzicback.member.application.dto.command;

public record SignUpCommand(
        String email,
        String password,
        String nickname
) {}
