package com.yj.schedule.domain.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginRequestDto  {
    private String username;
    private String password;
}
