package com.herian.expensesharingapp.service.impl;

import com.herian.expensesharingapp.controller.PersonController;
import com.herian.expensesharingapp.dto.GroupDto;
import com.herian.expensesharingapp.dto.LoginDto;
import com.herian.expensesharingapp.dto.PersonDto;
import com.herian.expensesharingapp.dto.PersonFriendDto;
import com.herian.expensesharingapp.entity.*;
import com.herian.expensesharingapp.repository.DebtRepository;
import com.herian.expensesharingapp.repository.PersonFriendRepository;
import com.herian.expensesharingapp.repository.PersonRepository;
import com.herian.expensesharingapp.service.PersonService;
import com.herian.expensesharingapp.service.impl.DebtServiceImpl;
import com.herian.expensesharingapp.service.impl.ExpenseServiceImpl;
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
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private DebtRepository debtRepository;
    @Autowired
    private PersonFriendRepository personFriendRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DebtServiceImpl debtServiceImpl;
    @Autowired
    ExpenseServiceImpl expenseServiceImpl;

    private final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Override
    public PersonDto findOneByEmail(String email) {
        Optional<Person> person = personRepository.findOneByEmail(email);
        if (person.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return mapPersonToPersonDto(person.get());
    }

    @Override
    public PersonDto createPerson(PersonDto personDto) {
        Person person = mapPersonDtoToPersonForCreateNewPerson(personDto);
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
        return mapPersonToPersonDto(savedPerson);
    }

    @Override
    public PersonFriendDto createFriend(String email) {
        Optional<Person> person = personRepository.findOneByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (person.isEmpty()) {
            throw new RuntimeException("Person is not present [Maybe authentication problems -> SecurityContext].");
        }
        Optional<PersonFriend> personFriend = personFriendRepository.findByPersonId(person.get().getId());
        if (personFriend.isEmpty()) {
            PersonFriend newPersonFriend = PersonFriend.builder()
                    .friendEmail(email)
                    .person(person.get())
                    .build();
            PersonFriend savedPersonFriend = personFriendRepository.save(newPersonFriend);
            person.get().getPersonFriends().add(savedPersonFriend);
            LOGGER.info("PersonFriend successfully added");
            return mapPersonFriendToPersonFriendDto(savedPersonFriend);
        } else {
            LOGGER.error("PersonFriend already Exists");
            throw new RuntimeException("Person already exists.");
        }
    }

    public PersonFriendDto mapPersonFriendToPersonFriendDto(PersonFriend personFriend) {
        return PersonFriendDto
                .builder()
                .id(personFriend.getId())
                .personId(personFriend.getPerson().getId())
                .friendEmail(personFriend.getFriendEmail())
                .build();
    }

    public PersonFriend mapPersonFriendDtoToPersonFriend(PersonFriendDto personFriendDto, Person person) {
        return PersonFriend
                .builder()
                .id(personFriendDto.getId())
                .person(person)
                .friendEmail(personFriendDto.getFriendEmail())
                .build();
    }

    public PersonDto mapPersonToPersonDto(Person person) {
        PersonDto personDto = new PersonDto();
        if (person.getId() != null) {
            personDto.setId(person.getId());
        }
        personDto.setUsername(person.getUsername());
        personDto.setPassword(person.getPassword());
        personDto.setEmail(person.getEmail());
        personDto.setRole(person.getRole().toString());

        personDto.setDebtList(person.getDebtList()
                .stream()
                .map(x -> debtServiceImpl.mapDebtToDebtDto(x))
                .collect(Collectors.toList()));

        personDto.setExpenseList(person.getExpenseList()
                .stream()
                .map(x -> expenseServiceImpl.mapExpenseToExpenseDto(x))
                .collect(Collectors.toList()));

        List<GroupDto> groupDtoList = person.getGroupList().stream().map(this::mapGroupToGroupDto).toList();

        personDto.setGroupList(groupDtoList);
        personDto.setPersonFriends(person.getPersonFriends()
                .stream().map(this::mapPersonFriendToPersonFriendDto).collect(Collectors.toSet()));
        return personDto;
    }

    public GroupDto mapGroupToGroupDto(Group group){
        GroupDto groupDto = new GroupDto();
        groupDto.setId(group.getId());
        groupDto.setName(group.getName());
        groupDto.setDescription(group.getDescription());
        groupDto.setCreatedAt(group.getCreatedAt());
        return groupDto;
    }

    public Person mapPersonDtoToPersonForCreateNewPerson(PersonDto personDto) {
        Person person = new Person();
        if (personDto.getId() != null) {
            person.setId(personDto.getId());
        }
        person.setMy_username(personDto.getUsername());
        person.setPassword(personDto.getPassword());
        person.setEmail(personDto.getEmail());
        try {
            person.setRole(Role.valueOf(personDto.getRole().toUpperCase(Locale.ROOT)));
        } catch (RuntimeException e) {
            throw new RuntimeException("Role " + personDto.getRole().toUpperCase(Locale.ROOT) + " does not exist.");
        }
        return person;
    }
}
