package com.herian.expensesharingapp.service.impl;

import com.herian.expensesharingapp.dto.auth.AuthenticationRequest;
import com.herian.expensesharingapp.dto.auth.AuthenticationResponse;
import com.herian.expensesharingapp.dto.auth.RegisterRequest;
import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.entity.Role;
import com.herian.expensesharingapp.repository.PersonRepository;
import com.herian.expensesharingapp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var person = Person.builder()
                .my_username(request.getUserName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .expenseList(new ArrayList<>())
                .debtList(new ArrayList<>())
                .groupList(new ArrayList<>())
                .build();
        personRepository.save(person);
        var jwtToken = jwtService.generateToken(person);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var person = personRepository.findOneByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(person);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
