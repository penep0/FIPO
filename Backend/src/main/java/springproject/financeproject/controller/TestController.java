package springproject.financeproject.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TestController {

    @GetMapping("/test/secure")
    public String securedEndpoint(Authentication authentication) {
        return "Hello, " + authentication.getName() + "! You are authenticated.";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html 렌더링
    }
}