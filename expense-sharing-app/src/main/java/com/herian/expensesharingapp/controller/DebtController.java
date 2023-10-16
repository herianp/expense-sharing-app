package com.herian.expensesharingapp.controller;

import com.herian.expensesharingapp.dto.DebtDto;
import com.herian.expensesharingapp.service.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/debt")
public class DebtController {

    @Autowired
    DebtService debtService;

    @GetMapping("{id}")
    public ResponseEntity<DebtDto> getDebtById(@PathVariable Long id){
        DebtDto debtDto_output = debtService.getDebtById(id);
        return ResponseEntity.status(HttpStatus.OK).body(debtDto_output);
    }

    @GetMapping("/person/{person_id}")
    public ResponseEntity<List<DebtDto>> getDebtListByPersonId(@PathVariable Long person_id){
        List<DebtDto> debtDto_output = debtService.getDebtListByPersonId(person_id);
        return ResponseEntity.status(HttpStatus.OK).body(debtDto_output);
    }

    @PostMapping()
    public ResponseEntity<DebtDto> createDebt(@RequestBody DebtDto debtDto){
        DebtDto debtDto_output = debtService.createDebt(debtDto);
        return ResponseEntity.status(HttpStatus.OK).body(debtDto_output);
    }
}
