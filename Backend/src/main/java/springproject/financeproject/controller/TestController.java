package springproject.financeproject.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/secure")
    public String securedEndpoint(Authentication authentication) {
        return "Hello, " + authentication.getName() + "! You are authenticated.";
    }
}