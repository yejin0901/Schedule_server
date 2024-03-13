package com.yj.schedule.domain.login;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "token")
    private String token;

    public RefreshToken(Long memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }


    public void validateSameToken(String refreshToken) {
        if (!this.token.equals(refreshToken)) {
            throw new IllegalArgumentException();
        }
    }
}