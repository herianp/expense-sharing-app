package com.herian.expensesharingapp.controller;

import com.herian.expensesharingapp.controller.auth.AuthenticationController;
import com.herian.expensesharingapp.dto.DebtDto;
import com.herian.expensesharingapp.dto.ExpenseDto;
import com.herian.expensesharingapp.service.ExpenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);


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
        LOGGER.info("Expense method: " + expenseDto);
        ExpenseDto expense_output = expenseService.createExpense(expenseDto);
        return ResponseEntity.status(HttpStatus.OK).body(expense_output);
    }

}
