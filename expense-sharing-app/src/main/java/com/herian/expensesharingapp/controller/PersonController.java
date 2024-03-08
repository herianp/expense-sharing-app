package com.herian.expensesharingapp.controller;

import com.herian.expensesharingapp.dto.PersonDto;
import com.herian.expensesharingapp.dto.PersonFriendDto;
import com.herian.expensesharingapp.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/person")
public class PersonController {

    private final Logger logger = LoggerFactory.getLogger(PersonController.class);
    @Autowired
    private PersonService personService;

    @GetMapping("/{email}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable String email) {
        logger.info("Success call: /" + email);
        PersonDto personDto = personService.findOneByEmail(email);
        logger.info("PersonDTO friends: " + personDto.getPersonFriends());
        return ResponseEntity.status(HttpStatus.OK).body(personDto);
    }

    @GetMapping("/group/{groupName}")
    public ResponseEntity<List<PersonDto>> getPersonListByGroupName(@PathVariable String groupName) {
        List<PersonDto> personDtoList = personService.findPersonListByGroupName(groupName);
        return ResponseEntity.status(HttpStatus.OK).body(personDtoList);
    }

    @PostMapping("/friend/{email}")
    public ResponseEntity<PersonFriendDto> createFriend(@PathVariable String email) {
        logger.info("Success call POST: /friend/" + email);
        PersonFriendDto response = personService.createFriend(email);
        logger.info("Good: /friend/" + email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/friend/{email}")
    public ResponseEntity<PersonDto> getFriendByEmail(@PathVariable String email) {
        logger.info("Success call GET: /friend/" + email);
        PersonDto response = personService.getFriendByEmail(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/friend/delete/{email}")
    public ResponseEntity deleteFriend(@PathVariable String email) {
        logger.info("Success call: /friend/delete" + email);
        personService.deleteFriend(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
