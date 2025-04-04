package springproject.financeproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springproject.financeproject.domain.User;
import springproject.financeproject.jwt.JwtTokenProvider;
import springproject.financeproject.repository.UserRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 🔹 내 정보 조회
    @GetMapping("/load")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {
        String token = extractToken(request);
        String email = jwtTokenProvider.getUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        return ResponseEntity.ok(user);
    }

    // 토큰 추출 유틸
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("토큰 없음");
        }
        return header.substring(7);
    }
}
