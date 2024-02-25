package com.herian.expensesharingapp.service.mapper;

import com.herian.expensesharingapp.dto.*;
import com.herian.expensesharingapp.entity.*;
import com.herian.expensesharingapp.repository.*;
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

import java.nio.file.attribute.GroupPrincipal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntityMapper {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private GroupRepository groupRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

    public PersonFriendDto mapPersonFriendToPersonFriendDto(PersonFriend personFriend) {
        Optional<Person> friend = personRepository.findOneByEmail(personFriend.getFriendEmail());
        if (friend.isEmpty()){
            LOGGER.error(this.getClass() + ": Person does not exists in DB.");
            throw new RuntimeException("Person does not exists in DB.");
        }
        return PersonFriendDto
                .builder()
                .id(personFriend.getId())
                .personId(personFriend.getPerson().getId())
                .friendEmail(personFriend.getFriendEmail())
                .username(friend.get().getMy_username())
                .friendId(friend.get().getId())
                .build();
    }

    public PersonFriend mapPersonFriendDtoToPersonFriend(PersonFriendDto personFriendDto, Person person) {

        return PersonFriend
                .builder()
                .id(personFriendDto.getId())
                .person(person)
                .friendId(personFriendDto.getFriendId())
                .friendEmail(personFriendDto.getFriendEmail())
                .build();
    }

    public PersonDto mapPersonToPersonDto(Person person) {
        PersonDto personDto = new PersonDto();
        if (person.getId() != null) {
            personDto.setId(person.getId());
        }
        personDto.setUsername(person.getMy_username());
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

    public GroupDto mapGroupToGroupDto(Group group) {
        GroupDto groupDto = new GroupDto();
        groupDto.setId(group.getId());
        groupDto.setName(group.getName());
        groupDto.setCreatedAt(group.getCreatedAt());
        groupDto.setGroupOwnerId(group.getGroupOwnerId());
        if (group.getDescription() != null) {
            groupDto.setDescription(group.getDescription());
        }
        if (group.getPersonList() != null) {
            groupDto.setPersonIds(group.getPersonList().stream().map(Person::getId).collect(Collectors.toList()));
        }
        if (group.getExpenseList() != null) {
            groupDto.setExpenseIds(group.getExpenseList().stream().map(Expense::getId).collect(Collectors.toList()));
        }
        return groupDto;
    }

    public Group mapGroupDtoToGroup(GroupDto groupDto) {
        Group group = new Group();
        group.setId(groupDto.getId());
        group.setName(groupDto.getName());
        group.setCreatedAt(groupDto.getCreatedAt());
        group.setGroupOwnerId(groupDto.getGroupOwnerId());
        if (groupDto.getDescription() != null) {
            group.setDescription(groupDto.getDescription());
        }
        if (groupDto.getPersonIds() != null) {
            group.setPersonList(groupDto.getPersonIds()
                    .stream()
                    .map(id -> personRepository.findById(id).get())
                    .collect(Collectors.toList()));        }
        if (groupDto.getExpenseIds() != null) {
            group.setExpenseList(groupDto.getExpenseIds()
                    .stream()
                    .map(id -> expenseRepository.findById(id).get())
                    .collect(Collectors.toList()));        }
        return group;
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
        debtDto.setPersonNameToPayBack(personRepository.findById(debt.getPersonIdToPayBack()).get().getMy_username());
        debtDto.setPersonId(debt.getPerson().getId());
        return debtDto;
    }

    public Debt mapDebtDtoToDebt(DebtDto debtDto) {
        Debt debt = new Debt();
        debt.setAmount(debtDto.getAmount());
        debt.setDescription(debtDto.getDescription());
        if (debtDto.getCreatedAt() != null) {
            debt.setCreatedAt(debtDto.getCreatedAt());
        }
        if(debtDto.getDueDate() != null) {
            debt.setDueDate(debtDto.getDueDate());
        }
        debt.setPersonIdToPayBack(debtDto.getPersonIdToPayBack());
        Person person = personRepository.findById(debtDto.getPersonId()).get();
        debt.setPerson(person);
        return debt;
    }

    public Expense mapExpenseDtoToExpense(ExpenseDto expenseDto) {
        Expense expense = new Expense();
        expense.setAmount(expenseDto.getAmount());
        if (expenseDto.getCreatedAt() != null) {
            expense.setCreatedAt(expenseDto.getCreatedAt());
        }
        if(expenseDto.getDueDate() != null) {
            expense.setDueDate(expenseDto.getDueDate());
        }
        expense.setDescription(expenseDto.getDescription());
        expense.setPersonIdWhoIsPay(expenseDto.getPersonIdWhoIsPay());
        expense.setPerson(personRepository.findById(expenseDto.getPersonId()).get());
        if (expenseDto.getGroupId() != null){
            expense.setGroup(groupRepository.findById(expenseDto.getGroupId()).get());
        }
        return expense;
    }

    public ExpenseDto mapExpenseToExpenseDto(Expense expense) {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setAmount(expense.getAmount());
        expenseDto.setCreatedAt(expense.getCreatedAt());
        expenseDto.setDescription(expense.getDescription());
        expenseDto.setDueDate(expense.getDueDate());
        expenseDto.setPersonIdWhoIsPay(expense.getPersonIdWhoIsPay());
        expenseDto.setPersonNameWhoIsPay(personRepository.findById(expense.getPersonIdWhoIsPay()).get().getMy_username());
        expenseDto.setPersonId(expense.getPerson().getId());
        if (expense.getGroup() != null){
            expenseDto.setGroupId(expense.getGroup().getId());
        }
        return expenseDto;
    }
}
