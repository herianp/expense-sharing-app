package com.herian.expensesharingapp.service.impl;

import com.herian.expensesharingapp.dto.ExpenseDto;
import com.herian.expensesharingapp.dto.GroupDto;
import com.herian.expensesharingapp.dto.GroupForSinglePageDto;
import com.herian.expensesharingapp.dto.PersonDto;
import com.herian.expensesharingapp.entity.Debt;
import com.herian.expensesharingapp.entity.Expense;
import com.herian.expensesharingapp.entity.Group;
import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.repository.GroupRepository;
import com.herian.expensesharingapp.repository.PersonRepository;
import com.herian.expensesharingapp.service.ExpenseService;
import com.herian.expensesharingapp.service.GroupService;
import com.herian.expensesharingapp.service.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.abs;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;

    private final EntityMapper entityMapper;
    private final PersonRepository personRepository;
    private final ExpenseService expenseService;

    @Override
    public GroupDto getGroupByName(String name) {
        Optional<Group> group = groupRepository.findGroupByName(name);
        if (group.isEmpty()) {
            throw new RuntimeException("Group with name: " + name + "does not exists!");
        }
        return entityMapper.mapGroupToGroupDto(group.get());
    }

    @Override
    public GroupDto createGroup(String name, Long personId) {
        if (name.equals("") || personId == null) {
            return null;
        }
        Person owner = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Creating group was stopped, because person id is wrong!"));
        Group group = new Group();
        group.setName(name);
        group.setGroupOwnerId(owner.getId());
        group.setCreatedAt(LocalDateTime.now());
        group.setPersonList(List.of(owner));
        group.setExpenseList(new ArrayList<>());
        owner.getGroupList().add(group);
        logger.info("raw OWNER id: " + group.getGroupOwnerId());
        Group savedGroup = groupRepository.save(group);
        logger.info("Saved OWNER id: " + savedGroup.getGroupOwnerId());
        return entityMapper.mapGroupToGroupDto(savedGroup);
    }

    @Override
    public GroupForSinglePageDto getGroupForSinglePageDtoById(Long id) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isEmpty()) {
            throw new RuntimeException("Group with id: " + id + "does not exists!");
        }
        return entityMapper.mapGroupToGroupForSinglePageDto(group.get());
    }

    @Override
    public GroupForSinglePageDto getGroupForSinglePageDtoByName(String groupName) {
        Optional<Group> group = groupRepository.findGroupByName(groupName);
        if (group.isEmpty()) {
            throw new RuntimeException("Group with name: " + groupName + "does not exists!");
        }
        return entityMapper.mapGroupToGroupForSinglePageDto(group.get());
    }

    @Override
    public void deleteGroup(String groupName, Long personId) {
        Optional<Person> person = personRepository.findById(personId);
        Optional<Group> group = groupRepository.findGroupByName(groupName);
        checkIfGroupAndPersonExists(group, person);

        if (!Objects.equals(person.get().getId(), group.get().getGroupOwnerId())) {
            throw new RuntimeException("Person " + person.get().getMy_username()
                    + " is not an owner of group with name: " + groupName);
        } else {
            List<Person> persons = group.get().getPersonList();
            for (Person per : persons) {
                if (!per.getGroupList().isEmpty()) {
                    per.getGroupList().remove(group.get());
                }
                personRepository.save(per);
            }
            groupRepository.delete(group.get());
        }
    }

    @Override
    public PersonDto addMemberToGroup(String groupName, String memberEmail) {
        Optional<Person> member = personRepository.findOneByEmail(memberEmail);
        Optional<Group> group = groupRepository.findGroupByName(groupName);
        checkIfGroupAndPersonExists(group, member);

        //save person to group
        List<Person> personList = group.get().getPersonList();
        if (personList.contains(member.get())) {
            throw new RuntimeException("Group with name: " + groupName + " already has person with email: " + memberEmail);
        }
        personList.add(member.get());
        group.get().setPersonList(personList);
        //save group to person
        List<Group> groupList = member.get().getGroupList();
        groupList.add(group.get());
        member.get().setGroupList(groupList);

        groupRepository.save(group.get());
        return entityMapper.mapPersonToPersonDto(member.get());
    }

    @Override
    public Long deleteMemberFromGroup(String groupName, String memberEmail) {
        Optional<Person> member = personRepository.findOneByEmail(memberEmail);
        Optional<Group> group = groupRepository.findGroupByName(groupName);
        checkIfGroupAndPersonExists(group, member);

        List<Person> personList = group.get().getPersonList();
        if (!personList.contains(member.get())) {
            throw new RuntimeException("Group with name: " + groupName + " does not has person with email: " + memberEmail);
        }
        personList.remove(member.get());
        group.get().setPersonList(personList);
        //save group to person
        List<Group> groupList = member.get().getGroupList();
        groupList.remove(group.get());
        member.get().setGroupList(groupList);

        groupRepository.save(group.get());
        return member.get().getId();
    }

    @Override
    public void closeAndCalculateGroup(String groupName, Long personId) {
        Optional<Group> group = groupRepository.findGroupByName(groupName);
        if (group.isEmpty()) {
            throw new RuntimeException("Group does not exists!");
        } else if (group.get().getExpenseList().size() <= 0) {
            throw new RuntimeException("Group does not have any expenses!");
        }
        HashMap<String, Float> expensesByName = new HashMap<String, Float>();
        HashMap<String, Float> debtsByName = new HashMap<String, Float>();

        // Pro kazdou osobu vytvorit zaznam s (jmeno : suma)
        for (Person person : group.get().getPersonList()) {
            expensesByName.put(person.getMy_username(), 0f);
            debtsByName.put(person.getMy_username(), 0f);
        }
        List<Expense> expenseList = group.get().getExpenseList();
        List<Debt> debtList = group.get().getDebtList();

        for (Expense expense : expenseList) {
            String username = expense.getPerson().getMy_username();
            Float currentExpenseSum = expensesByName.get(username) + expense.getAmount().floatValue();
            expensesByName.put(username, currentExpenseSum);
        }

        System.out.println("Expenses: ");
        System.out.println("Počet osob ve skupině: " + group.get().getPersonList().size());
        expensesByName.forEach((key, value) -> {
            System.out.println("key:" + key + ", value:" + value);
        });

        for (Debt debt : debtList) {
            String username = debt.getPerson().getMy_username();
            Float currentDebtSum = expensesByName.get(username) + debt.getAmount().floatValue();
            expensesByName.put(username, currentDebtSum);
        }

        //celkova suma vydaju
        float expenseValueSum = expensesByName.values().stream()
                .reduce(0f, Float::sum);
        // procentovy dil, ktery kazdej ma zaplatit
        float percentageShare = (100f / group.get().getPersonList().size()) / 100;

        System.out.println("Percentage: " + percentageShare);
        System.out.println("expenseValueSum: " + expenseValueSum);

        expensesByName.forEach((key, value) -> {
            if (value > 0 && expenseValueSum != 0f) {
                expensesByName.put(key, (value / expenseValueSum) - percentageShare);
            } else {
                expensesByName.put(key, 0f - percentageShare);
            }
        });

        System.out.println("PROCENTO: Expenses:");
        expensesByName.forEach((key, value) -> {
            System.out.println("key:" + key + ", value:" + value);
        });

        while (expensesByName.size() >= 2) {

            Map.Entry<String, Float> maxEntry = expensesByName.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new NoSuchElementException("Map is empty"));

            Map.Entry<String, Float> minEntry = expensesByName.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow(() -> new NoSuchElementException("Map is empty"));

            System.out.println("Maximum: " + maxEntry.getValue() + "; Minimum: " + minEntry.getValue());

            if (Math.abs(maxEntry.getValue()) > Math.abs(minEntry.getValue())) {
                System.out.println("Vytvoří se dluh " + Math.abs(minEntry.getValue() * expenseValueSum) + " od: " + minEntry.getKey() + " pro " + maxEntry.getKey());
                createExpenseWithDebt(minEntry.getKey(), maxEntry.getKey(), Math.abs(minEntry.getValue() * expenseValueSum), group.get());
                if ((maxEntry.getValue() + minEntry.getValue()) == 0) {
                    expensesByName.remove(maxEntry.getKey());
                } else {
                    expensesByName.put(maxEntry.getKey(), (maxEntry.getValue() + minEntry.getValue()));
                }
                expensesByName.remove(minEntry.getKey());
            } else {
                System.out.println("Vytvoří se dluh " + maxEntry.getValue() * expenseValueSum + " od: " + minEntry.getKey() + " pro " + maxEntry.getKey());
                createExpenseWithDebt(minEntry.getKey(), maxEntry.getKey(), maxEntry.getValue() * expenseValueSum, group.get());
                if ((minEntry.getValue() + maxEntry.getValue()) == 0) {
                    expensesByName.remove(minEntry.getKey());
                } else {
                    expensesByName.put(minEntry.getKey(), (minEntry.getValue() + maxEntry.getValue()));
                }
                expensesByName.remove(maxEntry.getKey());
            }
            System.out.println("---");
            expensesByName.forEach((key, value) -> {
                System.out.println("key:" + key + ", value:" + value);
            });
            System.out.println("---");
        }
        if (expensesByName.size() > 0) {
            expensesByName.forEach((key, value) -> {
                System.out.println("key:" + key + ", value:" + value);
            });
        } else {
            System.out.println("The list is empty");
        }
        //TODO Delete group
        deleteGroup(groupName, personId);
    }

    public void checkIfGroupAndPersonExists(Optional<Group> group, Optional<Person> person) {
        if (group.isEmpty()) {
            throw new RuntimeException("Member does not exists!");
        }
        if (person.isEmpty()) {
            throw new RuntimeException("Group does not exists!");
        }
    }

    public void createExpenseWithDebt(String personUsernameFrom, String personUsernameTo, float amount, Group group){
        Optional<Person> personFrom = group.getPersonList().stream().filter(person -> person.getMy_username().equals(personUsernameFrom)).findFirst();
        Optional<Person> personTo = group.getPersonList().stream().filter(person -> person.getMy_username().equals(personUsernameTo)).findFirst();
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setAmount((long)amount);
        expenseDto.setDescription("Skupina: " + group.getName());
        expenseDto.setPersonIdWhoIsPay(personFrom.get().getId());
        expenseDto.setPersonIdWhoHasToBePayed(personTo.get().getId());
        expenseService.createExpenseWithDebt(expenseDto);
    }
}
