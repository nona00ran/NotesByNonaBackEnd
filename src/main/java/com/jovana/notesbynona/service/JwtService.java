package com.jovana.notesbynona.service;

import com.jovana.notesbynona.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtService {
    String createToken(User user);

    Claims verifyAndParseToken(String token);

    String createRefreshToken(User user, String hashedFingerprint);
    boolean validateRefreshToken(String token);
    boolean refreshTokenExists(String token);

}
