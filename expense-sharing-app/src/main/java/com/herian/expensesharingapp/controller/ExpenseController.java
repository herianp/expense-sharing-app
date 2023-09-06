package com.herian.expensesharingapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    @GetMapping("/")
    public ResponseEntity<String> getExpenses(){
        return ResponseEntity.status(200).body("Expenses");
    }

}
