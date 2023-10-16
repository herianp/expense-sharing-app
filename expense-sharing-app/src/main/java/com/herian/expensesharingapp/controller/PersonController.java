package com.herian.expensesharingapp.controller;

import com.herian.expensesharingapp.dto.PersonDto;
import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("{email}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable String email){
        PersonDto personDto = personService.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(personDto);
    }
}
