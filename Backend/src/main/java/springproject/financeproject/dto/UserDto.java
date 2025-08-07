package springproject.financeproject.dto;

import springproject.financeproject.domain.User;

public record UserDto(String email, String userName, Long money) {
    public UserDto(User user) {
        this(user.getEmail(), user.getUserName(), user.getMoney());
    }
}
