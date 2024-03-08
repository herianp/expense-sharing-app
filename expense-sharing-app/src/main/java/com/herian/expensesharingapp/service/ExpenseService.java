package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.dto.DebtDto;
import com.herian.expensesharingapp.dto.ExpenseDto;
import com.herian.expensesharingapp.dto.StatusDoneDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface ExpenseService {
    ExpenseDto getExpenseById(Long id);
    List<ExpenseDto> getExpenseListByPersonId(Long person_id);
    @Transactional
    ExpenseDto createExpense(ExpenseDto expenseDto);
    @Transactional
    ExpenseDto createExpenseWithDebt(ExpenseDto expenseDto);

    @Transactional
    StatusDoneDto setDebtAndExpenseStatusToDone(Long expenseId);
}
