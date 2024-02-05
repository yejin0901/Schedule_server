package com.yj.schedule.entity;

import jakarta.persistence.*;
import lombok.Getter;
@Entity
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "token")
    private String token;

    protected RefreshToken() {
    }

    public RefreshToken(Long memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }
}