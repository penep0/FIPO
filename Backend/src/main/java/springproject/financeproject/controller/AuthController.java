package springproject.financeproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springproject.financeproject.domain.RefreshToken;
import springproject.financeproject.domain.User;
import springproject.financeproject.dto.authDto.LoginRequest;
import springproject.financeproject.dto.authDto.SignupRequest;
import springproject.financeproject.dto.authDto.TokenRefreshRequest;
import springproject.financeproject.jwt.JwtTokenProvider;
import springproject.financeproject.repository.RefreshTokenRepository;
import springproject.financeproject.repository.UserRepository;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthController(UserRepository userRepository,
                          JwtTokenProvider jwtTokenProvider,
                          PasswordEncoder passwordEncoder,
                          RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 불일치");
        }

        String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        // Refresh Token 저장 (덮어쓰기 방식)
        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setEmail(user.getEmail());
        tokenEntity.setToken(refreshToken);
        refreshTokenRepository.save(tokenEntity);

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 암호화 저장
        user.setMoney(100000L);
        user.setRoles(List.of("ROLE_USER"));

        userRepository.save(user);

        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        String requestToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(requestToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token 만료");
        }

        String email = jwtTokenProvider.getUsername(requestToken);
        RefreshToken savedToken = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("저장된 Refresh Token 없음"));

        if (!savedToken.getToken().equals(requestToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 불일치");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        String newAccessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        // 토큰 꺼내기
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Authorization 헤더가 잘못되었습니다.");
        }

        String accessToken = authHeader.substring(7);
        if (!jwtTokenProvider.validateToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
        }

        String email = jwtTokenProvider.getUsername(accessToken);
        refreshTokenRepository.deleteById(email); // Refresh Token 삭제

        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
