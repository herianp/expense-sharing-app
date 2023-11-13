package com.herian.expensesharingapp.controller;

import com.herian.expensesharingapp.dto.PersonDto;
import com.herian.expensesharingapp.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/person")
public class PersonController {

    private final Logger logger = LoggerFactory.getLogger(PersonController.class);
    @Autowired
    private PersonService personService;

    @GetMapping("{email}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable String email) {
        logger.info("Success call:" + email);
        PersonDto personDto = personService.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(personDto);
    }
}
