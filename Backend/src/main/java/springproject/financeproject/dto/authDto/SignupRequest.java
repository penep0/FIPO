package springproject.financeproject.dto.authDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String email;
    private String userName;
    private String password;
}