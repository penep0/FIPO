package springproject.financeproject.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    private String email; // 사용자 이메일 (ID로 사용)

    @Column(nullable = false)
    private String token;
}