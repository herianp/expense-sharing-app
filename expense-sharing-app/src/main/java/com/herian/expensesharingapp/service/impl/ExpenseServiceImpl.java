package com.herian.expensesharingapp.service.impl;

import com.herian.expensesharingapp.dto.DebtDto;
import com.herian.expensesharingapp.dto.ExpenseDto;
import com.herian.expensesharingapp.dto.StatusDoneDto;
import com.herian.expensesharingapp.entity.Debt;
import com.herian.expensesharingapp.entity.Expense;
import com.herian.expensesharingapp.entity.Group;
import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.repository.DebtRepository;
import com.herian.expensesharingapp.repository.ExpenseRepository;
import com.herian.expensesharingapp.repository.GroupRepository;
import com.herian.expensesharingapp.repository.PersonRepository;
import com.herian.expensesharingapp.service.ExpenseService;
import com.herian.expensesharingapp.service.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    DebtRepository debtRepository;

    private final EntityMapper entityMapper;

    @Override
    public ExpenseDto getExpenseById(Long id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty()) {
            throw new RuntimeException("Expense with ID:" + id + " does not EXISTS!..");
        }
        return entityMapper.mapExpenseToExpenseDto(expense.get());
    }

    @Override
    public List<ExpenseDto> getExpenseListByPersonId(Long person_id) {
        List<Expense> expenseList = expenseRepository.findExpenseListByPersonId(person_id);
        return expenseList.stream().map(entityMapper::mapExpenseToExpenseDto).collect(Collectors.toList());

    }

    @Override
    public ExpenseDto createExpense(ExpenseDto expenseDto) {
        Expense expense = entityMapper.mapExpenseDtoToExpense(expenseDto);
        expense.setCreatedAt(LocalDateTime.now());
        expense.setDueDate(LocalDateTime.now().plusMonths(1));
        Expense savedExpense = expenseRepository.save(expense);
        return entityMapper.mapExpenseToExpenseDto(savedExpense);
    }

    @Override
    public ExpenseDto createExpenseWithDebt(ExpenseDto expenseDto) {
        Optional<Person> debtOwner = personRepository.findById(expenseDto.getPersonIdWhoIsPay());
        if (debtOwner.isEmpty()) {
            throw new RuntimeException("Debt has no owner..");
        }
        Expense expense = entityMapper.mapExpenseDtoToExpense(expenseDto);
        expense.setCreatedAt(LocalDateTime.now());
        expense.setDueDate(LocalDateTime.now().plusMonths(1));
        //set debt
        Debt debt = new Debt();
        debt.setAmount(expense.getAmount());
        debt.setDescription(expense.getDescription());
        debt.setCreatedAt(expense.getCreatedAt());
        debt.setDueDate(expense.getDueDate());

        debt.setPersonIdToPayBack(expenseDto.getPersonIdWhoHasToBePayed());
        debtOwner.ifPresent(debt::setPerson);
        debt.setExpense(expense);

        Expense savedExpense = expenseRepository.save(expense);
        return entityMapper.mapExpenseToExpenseDto(savedExpense);
    }

    @Override
    public StatusDoneDto setDebtAndExpenseStatusToDone(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() ->
                new RuntimeException("Expense with id: " + expenseId + " does not exist!"));
//        //THIS IS NOW USELLES, BCS I WANT TO DELETE IT, IF IT IS DONE
//        debt.setStatus(StatusOfAction.DONE);
//        Debt saved_debt = debtRepository.save(debt);
//
//        Expense expense = debt.getExpense();
//        expense.setStatus(StatusOfAction.DONE);
//        expenseRepository.save(expense);
        StatusDoneDto statusDoneDto = new StatusDoneDto();
        statusDoneDto.setExpenseId(expenseId);
        statusDoneDto.setDebtId(expense.getDebt().getId());

        deleteExpenseAndDebt(expense);
        return statusDoneDto;
    }

    public void deleteExpenseAndDebt(Expense expense) {
        Debt debt = expense.getDebt();
        expense.setDebt(null); // Disassociate the debt from the expense
        expense.setPerson(null);
        debt.setExpense(null); // If the relationship is bidirectional
        debt.setPerson(null);
        debtRepository.delete(debt); // Delete the debt

        expenseRepository.delete(expense); // Delete the expense
    }
}
