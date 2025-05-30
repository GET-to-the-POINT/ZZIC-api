package point.zzicback.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import point.zzicback.auth.application.AuthService;
import point.zzicback.auth.application.AuthTokenService;
import point.zzicback.auth.application.dto.command.SignInCommand;
import point.zzicback.auth.domain.AuthenticatedMember;
import point.zzicback.auth.presentation.dto.SignInRequest;
import point.zzicback.auth.presentation.dto.SignUpRequest;
import point.zzicback.auth.presentation.mapper.AuthPresentationMapper;

@Tag(name = "인증", description = "회원 인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthTokenService authTokenService;
    private final AuthPresentationMapper authPresentationMapper;

    @Operation(summary = "회원가입 및 로그인", description = "회원가입을 진행하고 즉시 로그인하여 JWT/리프레시 토큰을 쿠키로 발급합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 및 로그인 성공, 쿠키에 토큰 발급")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일")
    @PostMapping("/sign-up")
    public void signUpAndIn(@Valid @RequestBody SignUpRequest request, HttpServletResponse response) {
        authService.signUp(authPresentationMapper.toCommand(request));

        SignInCommand signInCommand = new SignInCommand(request.email(), request.password());
        AuthenticatedMember authenticatedMember = authService.signIn(signInCommand);

        authTokenService.authenticateWithCookies(authenticatedMember, response);
    }

    @Operation(summary = "로그인", description = "로그인을 진행하고 JWT/리프레시 토큰을 쿠키로 발급합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공, 쿠키에 토큰 발급")
    @ApiResponse(responseCode = "401", description = "인증 실패")
    @PostMapping("/sign-in")
    public void signInJson(@Valid @RequestBody SignInRequest request, HttpServletResponse response) {
        AuthenticatedMember authenticatedMember = authService.signIn(authPresentationMapper.toCommand(request));
        authTokenService.authenticateWithCookies(authenticatedMember, response);
    }

    @Operation(summary = "로그아웃", description = "로그아웃 처리 및 토큰 만료")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @PostMapping("/sign-out")
    public void signOut(HttpServletRequest request, HttpServletResponse response) {
        authTokenService.signOut(request, response);
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 사용하여 액세스 토큰 갱신")
    @ApiResponse(responseCode = "200", description = "토큰 갱신 성공")
    @ApiResponse(responseCode = "401", description = "토큰 갱신 실패")
    @GetMapping("/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        boolean refreshed = authTokenService.refreshTokensIfNeeded(request, response);
        if (!refreshed) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
