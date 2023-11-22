package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.dto.auth.AuthenticationRequest;
import com.herian.expensesharingapp.dto.auth.AuthenticationResponse;
import com.herian.expensesharingapp.dto.auth.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
