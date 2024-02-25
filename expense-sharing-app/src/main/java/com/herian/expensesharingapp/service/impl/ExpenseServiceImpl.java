package com.herian.expensesharingapp.service.impl;

import com.herian.expensesharingapp.dto.DebtDto;
import com.herian.expensesharingapp.dto.ExpenseDto;
import com.herian.expensesharingapp.entity.Debt;
import com.herian.expensesharingapp.entity.Expense;
import com.herian.expensesharingapp.repository.ExpenseRepository;
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
    private final EntityMapper entityMapper;

    @Override
    public ExpenseDto getExpenseById(Long id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isEmpty()){
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
}
