package com.herian.expensesharingapp.service.impl;

import com.herian.expensesharingapp.dto.GroupDto;
import com.herian.expensesharingapp.dto.PersonDto;
import com.herian.expensesharingapp.dto.PersonFriendDto;
import com.herian.expensesharingapp.entity.*;
import com.herian.expensesharingapp.repository.DebtRepository;
import com.herian.expensesharingapp.repository.PersonFriendRepository;
import com.herian.expensesharingapp.repository.PersonRepository;
import com.herian.expensesharingapp.service.PersonService;
import com.herian.expensesharingapp.service.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonFriendRepository personFriendRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final EntityMapper entityMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Override
    public PersonDto findOneByEmail(String email) {
        Optional<Person> person = personRepository.findOneByEmail(email);
        if (person.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return entityMapper.mapPersonToPersonDto(person.get());
    }

    @Override
    public PersonDto createPerson(PersonDto personDto) {
        Person person = entityMapper.mapPersonDtoToPersonForCreateNewPerson(personDto);
        Person savedPerson = null;
        ResponseEntity<String> response = null;
        try {
            String hashPassword = passwordEncoder.encode(person.getPassword());
            person.setPassword(hashPassword);
            person.setDebtList(new ArrayList<Debt>());
            person.setExpenseList(new ArrayList<Expense>());
            person.setGroupList(new ArrayList<Group>());
            savedPerson = personRepository.save(person);
        } catch (Exception e) {
//            TODO EXCEPTION HANDLER??
            throw new RuntimeException("An exception occurred due to " + e.getMessage());
        }
        return entityMapper.mapPersonToPersonDto(savedPerson);
    }

    @Override
    public PersonFriendDto createFriend(String email) {
        Optional<Person> person = personRepository.findOneByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (person.isEmpty()) {
            throw new RuntimeException("Person is not present [Maybe authentication problems -> SecurityContext].");
        }
        Optional<PersonFriend> personFriend = personFriendRepository.findByFriendEmail(email);
        if (personFriend.isEmpty()) {
            Optional<Person> friend = personRepository.findOneByEmail(email);
            if (friend.isEmpty()){
                LOGGER.error("Email address of friend does not Exists");
                throw new RuntimeException("Email address of friend does not Exists");
            }
            PersonFriend newPersonFriend = PersonFriend.builder()
                    .friendEmail(email)
                    .person(person.get())
                    .build();
            PersonFriend savedPersonFriend = personFriendRepository.save(newPersonFriend);
            person.get().getPersonFriends().add(savedPersonFriend);
            LOGGER.info("PersonFriend successfully added");
            return entityMapper.mapPersonFriendToPersonFriendDto(savedPersonFriend);
        } else {
            LOGGER.error("PersonFriend already Exists");
            throw new RuntimeException("PersonFriend already exists.");
        }
    }

    @Override
    public void deleteFriend(String email) {
        Optional<Person> person = personRepository.findOneByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Set<PersonFriend> personFriends = person.get().getPersonFriends();
        for(PersonFriend personFriend : personFriends){
            LOGGER.info(email + " <--> " + personFriend.getFriendEmail());
            if (personFriend.getFriendEmail().equals(email)){
                personFriendRepository.delete(personFriend);
                return;
            }
        }
    }

    @Override
    public PersonDto getFriendByEmail(String email) {
        Optional<Person> person = personRepository.findOneByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Set<PersonFriend> personFriends = person.get().getPersonFriends();
        for(PersonFriend personFriend : personFriends) {
            if (personFriend.getFriendEmail().equals(email)){
                Optional<Person> friend = personRepository.findOneByEmail(email);
                return entityMapper.mapPersonToPersonDto(friend.get());
            }
        }
        throw new RuntimeException("PersonFriend does not exists.");
    }
}
