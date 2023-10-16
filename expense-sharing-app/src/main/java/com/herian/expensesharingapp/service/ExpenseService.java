package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.dto.DebtDto;
import com.herian.expensesharingapp.dto.ExpenseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExpenseService {

    ExpenseDto getExpenseById(Long id);
    List<ExpenseDto> getExpenseListByPersonId(Long person_id);
    ExpenseDto createExpense(ExpenseDto expenseDto);
}
