package com.herian.expensesharingapp.service.impl;

import com.herian.expensesharingapp.controller.PersonController;
import com.herian.expensesharingapp.dto.LoginDto;
import com.herian.expensesharingapp.dto.PersonDto;
import com.herian.expensesharingapp.entity.Debt;
import com.herian.expensesharingapp.entity.Expense;
import com.herian.expensesharingapp.entity.Group;
import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.repository.DebtRepository;
import com.herian.expensesharingapp.repository.PersonRepository;
import com.herian.expensesharingapp.service.PersonService;
import com.herian.expensesharingapp.service.impl.DebtServiceImpl;
import com.herian.expensesharingapp.service.impl.ExpenseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DebtServiceImpl debtServiceImpl;
    @Autowired
    ExpenseServiceImpl expenseServiceImpl;

    private final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);
    @Override
    public PersonDto findByEmail(String email) {
        Person person = personRepository.findOneByEmail(email);
        return mapPersonToPersonDto(person);
    }

    @Override
    public PersonDto createPerson(PersonDto personDto) {
        Person person = mapPersonDtoToPerson(personDto);
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
    public void loginPerson(LoginDto loginDto) {
        LOGGER.info("Login logic...");
    }

    public PersonDto mapPersonToPersonDto(Person person) {
        PersonDto personDto = new PersonDto();
        if (person.getId()!=null){
            personDto.setId(person.getId());
        }
        personDto.setUsername(person.getUsername());
        personDto.setPassword(person.getPassword());
        personDto.setEmail(person.getEmail());
        personDto.setRole(person.getRole());

        personDto.setDebtList(person.getDebtList()
                .stream()
                .map(x->debtServiceImpl.mapDebtToDebtDto(x))
                .collect(Collectors.toList()));

        personDto.setExpenseList(person.getExpenseList()
                .stream()
                .map(x-> expenseServiceImpl.mapExpenseToExpenseDto(x))
                .collect(Collectors.toList()));

        personDto.setGroupList(person.getGroupList());
        return personDto;
    }

    public Person mapPersonDtoToPerson(PersonDto personDto) {
        Person person = new Person();
        if (personDto.getId()!=null){
            person.setId(personDto.getId());
        }
        person.setUsername(personDto.getUsername());
        person.setPassword(personDto.getPassword());
        person.setEmail(personDto.getEmail());
        person.setRole(personDto.getRole());

        person.setDebtList(personDto.getDebtList()
                .stream()
                .map(x->debtServiceImpl.mapDebtDtoToDebt(x))
                .collect(Collectors.toList()));

        person.setExpenseList(personDto.getExpenseList()
                .stream()
                .map(x->expenseServiceImpl.mapExpenseDtoToExpense(x))
                .collect(Collectors.toList()));

        person.setGroupList(personDto.getGroupList());
        return person;
    }
}
