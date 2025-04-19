package com.jovana.notesbynona.service;

import com.jovana.notesbynona.model.login.LoginRequest;
import com.jovana.notesbynona.model.login.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    LoginResponse refresh(String refreshToken);

    void logout(String refreshToken);
}
