package com.herian.expensesharingapp.controller;

import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    PersonRepository personRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Person person) {
        Person savedPerson = null;
        ResponseEntity<String> response = null;
        try {
            savedPerson = personRepository.save(person);
            if (savedPerson.getId() > 0) {
                response = ResponseEntity.status(HttpStatus.CREATED).body("User is successfully registered!");
            }
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An exception occured due to " + e.getMessage());
        }
        return response;
    }
}
