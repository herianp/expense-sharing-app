package com.herian.expensesharingapp.service.impl;

import com.herian.expensesharingapp.dto.ErrorMessageDto;
import com.herian.expensesharingapp.dto.auth.AuthenticationRequest;
import com.herian.expensesharingapp.dto.auth.AuthenticationResponse;
import com.herian.expensesharingapp.dto.auth.RegisterRequest;
import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.entity.Role;
import com.herian.expensesharingapp.repository.PersonRepository;
import com.herian.expensesharingapp.service.AuthenticationService;
import com.herian.expensesharingapp.service.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;

    private final EntityMapper entityMapper;

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
                .personFriends(new HashSet<>())
                .build();
        Person savedPerson = personRepository.save(person);
        var jwtToken = jwtService.generateToken(person);
        return AuthenticationResponse.builder().token(jwtToken).personDto(entityMapper.mapPersonToPersonDto(savedPerson)).build();
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
                .personDto(entityMapper.mapPersonToPersonDto(person))
                .build();
    }

    @Override
    public String resetPassword(AuthenticationRequest request) {
        var person = personRepository.findOneByEmail(request.getEmail())
                .orElseThrow();
        person.setPassword(passwordEncoder.encode(request.getPassword()));
        personRepository.save(person);
        return "Password successfully changed";
    }

    @Override
    public ErrorMessageDto validateRegistrationRequest(RegisterRequest request) {
        ErrorMessageDto errorMessageDTO = new ErrorMessageDto();
        errorMessageDTO.setCode(400);
        if (request.getUserName().isEmpty()) {
            errorMessageDTO.setErrorMessage("Username is empty!");
            return errorMessageDTO;
        }
        if (request.getEmail().isEmpty()) {
            errorMessageDTO.setErrorMessage("Email is empty!");
            return errorMessageDTO;
        }
        if (!isUsernameUnique(request.getUserName())) {
            errorMessageDTO.setErrorMessage("Username is not unique!");
            return errorMessageDTO;
        }
        if (!isEmailUnique(request.getEmail())) {
            errorMessageDTO.setErrorMessage("E-mail is not unique!");
            return errorMessageDTO;
        }
        errorMessageDTO.setCode(200);
        return errorMessageDTO;
    }

    public boolean isEmailUnique(String email) {
        List<Person> persons = personRepository.findAll();
        for (Person p : persons) {
            if (p.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    public boolean isUsernameUnique(String username) {
        List<Person> persons = personRepository.findAll();
        for (Person p : persons) {
            if (p.getMy_username().equals(username)) {
                return false;
            }
        }
        return true;
    }
}
