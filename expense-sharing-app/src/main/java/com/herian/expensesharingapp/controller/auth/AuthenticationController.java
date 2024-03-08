package com.herian.expensesharingapp.controller.auth;

import com.herian.expensesharingapp.dto.ErrorMessageDto;
import com.herian.expensesharingapp.dto.auth.AuthenticationRequest;
import com.herian.expensesharingapp.dto.auth.AuthenticationResponse;
import com.herian.expensesharingapp.dto.auth.RegisterRequest;
import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.repository.PersonRepository;
import com.herian.expensesharingapp.service.AuthenticationService;
import com.herian.expensesharingapp.service.impl.GroupServiceImpl;
import com.herian.expensesharingapp.service.impl.PersonServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        ErrorMessageDto errorMessageDTO = authenticationService.validateRegistrationRequest(request);
        if (errorMessageDTO.getCode() != 200) {
            return ResponseEntity.status(400).body(errorMessageDTO);
        }
        return ResponseEntity.status(200).body(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse output = authenticationService.authenticate(request);
        return ResponseEntity.ok(output);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.resetPassword(request));
    }
}

