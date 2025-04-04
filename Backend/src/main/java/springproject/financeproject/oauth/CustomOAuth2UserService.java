package springproject.financeproject.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import springproject.financeproject.domain.User;
import springproject.financeproject.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(request);

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // 🔽 이메일로 사용자 조회
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            // 🔽 없으면 새로 저장 (자동 회원가입)
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUserName(name);
            newUser.setPassword("oauth"); // OAuth 전용 사용자
            newUser.setMoney(0L);
            return userRepository.save(newUser);
        });

        // 🔐 SecurityContext 저장용
        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                oauth2User.getAttributes(),
                "email"
        );
    }
}