package com.herian.expensesharingapp.service.mapper;

import com.herian.expensesharingapp.dto.*;
import com.herian.expensesharingapp.entity.*;
import com.herian.expensesharingapp.repository.DebtRepository;
import com.herian.expensesharingapp.repository.PersonFriendRepository;
import com.herian.expensesharingapp.repository.PersonRepository;
import com.herian.expensesharingapp.service.impl.DebtServiceImpl;
import com.herian.expensesharingapp.service.impl.ExpenseServiceImpl;
import com.herian.expensesharingapp.service.impl.PersonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntityMapper {

    @Autowired
    private PersonRepository personRepository;


    private final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

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
                .map(this::mapDebtToDebtDto)
                .collect(Collectors.toList()));

        personDto.setExpenseList(person.getExpenseList()
                .stream()
                .map(this::mapExpenseToExpenseDto)
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

    public DebtDto mapDebtToDebtDto(Debt debt) {
        DebtDto debtDto = new DebtDto();
        debtDto.setAmount(debt.getAmount());
        debtDto.setDescription(debt.getDescription());
        debtDto.setCreatedAt(debt.getCreatedAt());
        debtDto.setDueDate(debt.getDueDate());
        debtDto.setPersonIdToPayBack(debt.getPersonIdToPayBack());
        debtDto.setPersonId(debt.getPerson().getId());
        return debtDto;
    }

    public Debt mapDebtDtoToDebt(DebtDto debtDto) {
        Debt debt = new Debt();
        debt.setAmount(debtDto.getAmount());
        debt.setDescription(debtDto.getDescription());
        debt.setCreatedAt(debtDto.getCreatedAt());
        debt.setDueDate(debtDto.getDueDate());
        debt.setPersonIdToPayBack(debtDto.getPersonIdToPayBack());
        Person person = personRepository.findById(debtDto.getPersonId()).get();
        debt.setPerson(person);
        return debt;
    }

    public Expense mapExpenseDtoToExpense(ExpenseDto expenseDto) {
        Expense expense = new Expense();
        expense.setAmount(expenseDto.getAmount());
        expense.setCreatedAt(expenseDto.getCreatedAt());
        expense.setDescription(expenseDto.getDescription());
        expense.setPerson(personRepository.findById(expenseDto.getPersonId()).get());
        return expense;
    }

    public ExpenseDto mapExpenseToExpenseDto(Expense expense) {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setAmount(expense.getAmount());
        expenseDto.setCreatedAt(expense.getCreatedAt());
        expenseDto.setDescription(expense.getDescription());
        expenseDto.setPersonId(expense.getPerson().getId());
        return expenseDto;
    }
}
