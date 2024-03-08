package com.herian.expensesharingapp.controller;

import com.herian.expensesharingapp.dto.ExpenseDto;
import com.herian.expensesharingapp.dto.StatusDoneDto;
import com.herian.expensesharingapp.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;

    @GetMapping("{id}")
    public ResponseEntity<ExpenseDto> getExpenseById(@PathVariable Long id){
        ExpenseDto expense_output = expenseService.getExpenseById(id);
        return ResponseEntity.status(HttpStatus.OK).body(expense_output);
    }

    @GetMapping("/person/{person_id}")
    public ResponseEntity<List<ExpenseDto>> getExpenseListByPersonId(@PathVariable Long person_id){
        List<ExpenseDto> expense_output = expenseService.getExpenseListByPersonId(person_id);
        return ResponseEntity.status(HttpStatus.OK).body(expense_output);
    }

    @PostMapping()
    public ResponseEntity<ExpenseDto> createExpense(@RequestBody ExpenseDto expenseDto){
        // METODA PRO SKUPINU
        ExpenseDto expense_output = expenseService.createExpense(expenseDto);
        return ResponseEntity.status(HttpStatus.OK).body(expense_output);
    }

    @PostMapping("/withDebt")
    public ResponseEntity<ExpenseDto> createExpenseWithDebt(@RequestBody ExpenseDto expenseDto){
        ExpenseDto expense_output = expenseService.createExpenseWithDebt(expenseDto);
        return ResponseEntity.status(HttpStatus.OK).body(expense_output);
    }

    @PostMapping("/statusDone/{expenseId}")
    public ResponseEntity<StatusDoneDto> setDebtStatusToDoneAndExpenseStatusToDoneWell(@PathVariable Long expenseId){
        StatusDoneDto output = expenseService.setDebtAndExpenseStatusToDone(expenseId);
        return ResponseEntity.status(HttpStatus.OK).body(output);
    }
}
