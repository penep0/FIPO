package springproject.financeproject.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import springproject.financeproject.domain.RefreshToken;
import springproject.financeproject.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import springproject.financeproject.repository.RefreshTokenRepository;

import java.io.IOException;
import java.util.List;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.frontend-url}")
    private String frontendUrl;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2LoginSuccessHandler(JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository)  {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        // 토큰 발급
        String accessToken = jwtTokenProvider.createToken(email, List.of("ROLE_USER"));
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        // DB 저장 (💡 이거 중요)
        refreshTokenRepository.save(new RefreshToken(email, refreshToken));

        // 응답 방식 (예시 1: 쿼리파라미터)
        response.sendRedirect(frontendUrl + "/login/success?accessToken=" + accessToken + "&refreshToken=" + refreshToken);
    }
}
