package com.sky.yb.user.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class JwtResponse {
    private final String jwtToken;

    private JwtResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public static JwtResponse of(String jwtToken) {
        return new JwtResponse(jwtToken);
    }
}
