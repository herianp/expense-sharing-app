package com.herian.expensesharingapp.service.mapper;

import com.herian.expensesharingapp.dto.*;
import com.herian.expensesharingapp.entity.*;
import com.herian.expensesharingapp.entity.Role;
import com.herian.expensesharingapp.enums.StatusOfAction;
import com.herian.expensesharingapp.repository.*;
import com.herian.expensesharingapp.service.impl.PersonServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private DebtRepository debtRepository;

    @Autowired
    private GroupRepository groupRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

    public PersonFriendDto mapPersonFriendToPersonFriendDto(PersonFriend personFriend) {
        Optional<Person> friend = personRepository.findOneByEmail(personFriend.getFriendEmail());
        if (friend.isEmpty()) {
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
        if (group.getDebtList() != null) {
            groupDto.setExpenseIds(group.getDebtList().stream().map(Debt::getId).collect(Collectors.toList()));
        }
        return groupDto;
    }

    public GroupForSinglePageDto mapGroupToGroupForSinglePageDto(Group group) {
        GroupForSinglePageDto groupDto = new GroupForSinglePageDto();
        groupDto.setId(group.getId());
        groupDto.setName(group.getName());
        groupDto.setCreatedAt(group.getCreatedAt());
        groupDto.setGroupOwnerId(group.getGroupOwnerId());
        if (group.getDescription() != null) {
            groupDto.setDescription(group.getDescription());
        }
        if (group.getPersonList() != null) {
            groupDto.setPersonNames(group.getPersonList().stream().map(Person::getMy_username).collect(Collectors.toList()));
            groupDto.setPersonEmails(group.getPersonList().stream().map(Person::getEmail).collect(Collectors.toList()));
        }
        if (group.getExpenseList() != null) {
            groupDto.setExpenseList(group.getExpenseList().stream().map(this::mapExpenseToExpenseDto).collect(Collectors.toList()));
        }
        if (group.getDebtList() != null) {
            groupDto.setDebtList(group.getDebtList().stream().map(this::mapDebtToDebtDto).collect(Collectors.toList()));
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
                    .collect(Collectors.toList()));
        }
        if (groupDto.getExpenseIds() != null) {
            group.setExpenseList(groupDto.getExpenseIds()
                    .stream()
                    .map(id -> expenseRepository.findById(id).get())
                    .collect(Collectors.toList()));
        }
        if (groupDto.getDebtIds() != null) {
            group.setDebtList(groupDto.getDebtIds()
                    .stream()
                    .map(id -> debtRepository.findById(id).get())
                    .collect(Collectors.toList()));
        }
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
        debtDto.setId(debt.getId());
        debtDto.setAmount(debt.getAmount());
        debtDto.setDescription(debt.getDescription());
        debtDto.setCreatedAt(debt.getCreatedAt());
        debtDto.setDueDate(debt.getDueDate());
        debtDto.setPersonIdToPayBack(debt.getPersonIdToPayBack());
        debtDto.setPersonNameToPayBack(personRepository.findById(debt.getPersonIdToPayBack()).get().getMy_username());
        debtDto.setPersonId(debt.getPerson().getId());
        if(debt.getStatus() != null) {
            debtDto.setStatus(debt.getStatus().toString());
        } else {
            debtDto.setStatus("ACTIVE");
        }
        return debtDto;
    }

    public Debt mapDebtDtoToDebt(DebtDto debtDto) {
        Debt debt = new Debt();
        debt.setAmount(debtDto.getAmount());
        if (debtDto.getCreatedAt() != null) {
            debt.setCreatedAt(debtDto.getCreatedAt());
        }
        if (debtDto.getDueDate() != null) {
            debt.setDueDate(debtDto.getDueDate());
        }
        debt.setDescription(debtDto.getDescription());
        debt.setPersonIdToPayBack(debtDto.getPersonIdToPayBack());
        Person person = personRepository.findById(debtDto.getPersonId()).get();
        debt.setPerson(person);
        if(debtDto.getStatus() != null) {
            debt.setStatus(StatusOfAction.fromString(debtDto.getStatus()));
        } else {
            debt.setStatus(StatusOfAction.fromString("ACTIVE"));
        }
        return debt;
    }

    public Expense mapExpenseDtoToExpense(ExpenseDto expenseDto) {
        Expense expense = new Expense();
        expense.setAmount(expenseDto.getAmount());
        if (expenseDto.getCreatedAt() != null) {
            expense.setCreatedAt(expenseDto.getCreatedAt());
        }
        if (expenseDto.getDueDate() != null) {
            expense.setDueDate(expenseDto.getDueDate());
        }
        expense.setDescription(expenseDto.getDescription());
        if(expenseDto.getStatus() != null) {
            expense.setStatus(StatusOfAction.fromString(expenseDto.getStatus()));
        } else {
            expense.setStatus(StatusOfAction.fromString("ACTIVE"));
        }
        expense.setPerson(personRepository.findById(expenseDto.getPersonIdWhoHasToBePayed()).get());
        if (expenseDto.getGroupId() != null) {
            Optional<Group> group = groupRepository.findById(expenseDto.getGroupId());
            if (group.isEmpty()){
                throw new RuntimeException("Expense creating error: group with this id does not exists.");
            } else expense.setGroup(group.get());
        } else {
            expense.setPersonIdWhoIsPay(expenseDto.getPersonIdWhoIsPay());
        }
        return expense;
    }

    public ExpenseDto mapExpenseToExpenseDto(Expense expense) {
        ExpenseDto expenseDto = new ExpenseDto();
        if (expense.getPersonIdWhoIsPay() != null){
            Person friend = personRepository.findById(expense.getPersonIdWhoIsPay()).orElseThrow(() -> new EntityNotFoundException("Person not found"));
            expenseDto.setPersonNameWhoIsPay(friend.getMy_username());
        }
        expenseDto.setId(expense.getId());
        expenseDto.setAmount(expense.getAmount());
        expenseDto.setCreatedAt(expense.getCreatedAt());
        expenseDto.setDescription(expense.getDescription());
        expenseDto.setDueDate(expense.getDueDate());

        if(expense.getStatus() != null) {
            expenseDto.setStatus(expense.getStatus().toString());
        } else {
            expenseDto.setStatus("ACTIVE");
        }

        if (expense.getGroup() == null) {
            expenseDto.setPersonIdWhoIsPay(expense.getPersonIdWhoIsPay());
        } else {
            expenseDto.setGroupId(expense.getGroup().getId());
        }

        if (expense.getPerson() != null) {
            expenseDto.setPersonIdWhoHasToBePayed(expense.getPerson().getId());
            expenseDto.setPersonNameWhoHasToBePayed(expense.getPerson().getMy_username());
        }
        return expenseDto;
    }
}
