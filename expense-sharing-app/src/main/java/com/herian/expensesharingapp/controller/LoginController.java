package com.herian.expensesharingapp.controller;

import com.herian.expensesharingapp.dto.PersonDto;
import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.repository.PersonRepository;
import com.herian.expensesharingapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    PersonService personService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody PersonDto personDto) {
        personService.createPerson(personDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User is successfully registered!");
    }
}
