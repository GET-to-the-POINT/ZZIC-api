package point.zzicback.common.utill;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import point.zzicback.common.properties.JwtProperties;
import point.zzicback.member.domain.Member;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String generateJwtToken(String id, String email, String nickname) {
        Instant now = Instant.now();

        List<String> roles = Collections.singletonList("ROLE_USER");

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(id)
                .claim("email", email)
                .claim("nickname", nickname)
                .claim("scope", String.join(" ", roles))
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.cookie().maxAge(), ChronoUnit.SECONDS))
                .build();

        JwtEncoderParameters parameters = JwtEncoderParameters.from(
                JwsHeader.with(() -> "RS256")
                        .keyId(jwtProperties.keyId())
                        .build(),
                claims
        );

        return jwtEncoder.encode(parameters).getTokenValue();
    }

    public long getMemberId(String jwtToken) {
        try {
            Jwt decodedJwt = jwtDecoder.decode(jwtToken);
            return Long.parseLong(decodedJwt.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }

    public String getCustomerKey(String jwtToken) {
        try {
            Jwt decodedJwt = jwtDecoder.decode(jwtToken);
            return decodedJwt.getSubject();
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }

    public String getEmailFromToken(String jwtToken) {
        try {
            Jwt decodedJwt = jwtDecoder.decode(jwtToken);
            return decodedJwt.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }
}