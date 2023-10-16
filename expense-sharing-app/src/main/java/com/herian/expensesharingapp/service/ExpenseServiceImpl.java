package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.dto.DebtDto;
import com.herian.expensesharingapp.dto.ExpenseDto;
import com.herian.expensesharingapp.entity.Debt;
import com.herian.expensesharingapp.entity.Expense;
import com.herian.expensesharingapp.repository.ExpenseRepository;
import com.herian.expensesharingapp.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService{

    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    PersonRepository personRepository;

    @Override
    public ExpenseDto getExpenseById(Long id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty()){
            throw new RuntimeException("Expense with ID:" + id + " does not EXISTS!..");
        }
        return mapExpenseToExpenseDto(expense.get());
    }

    @Override
    public List<ExpenseDto> getExpenseListByPersonId(Long person_id) {
        List<Expense> expenseList = expenseRepository.findExpenseListByPersonId(person_id);
        return expenseList.stream().map(this::mapExpenseToExpenseDto).collect(Collectors.toList());

    }

    @Override
    public ExpenseDto createExpense(ExpenseDto expenseDto) {
        Expense expense = mapExpenseDtoToExpense(expenseDto);
        expense.setCreatedAt(LocalDateTime.now());
        Expense savedExpense = expenseRepository.save(expense);
        return mapExpenseToExpenseDto(savedExpense);
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
