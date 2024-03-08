package com.herian.expensesharingapp.controller;

import com.herian.expensesharingapp.dto.DebtDto;
import com.herian.expensesharingapp.service.DebtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger LOGGER = LoggerFactory.getLogger(DebtController.class);

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
        //TODO Jak vytvorit dluh bez pritele?? jeste vymyslet
//        DebtDto debtDto_output = debtService.createDebt(debtDto);
        DebtDto debtDto_output = debtService.createDebtWithExpense(debtDto);
        return ResponseEntity.status(HttpStatus.OK).body(debtDto_output);
    }

    @PostMapping("/withExpense")
    public ResponseEntity<DebtDto> createDebtWithExpense(@RequestBody DebtDto debtDto){
        DebtDto debtDto_output = debtService.createDebtWithExpense(debtDto);
        return ResponseEntity.status(HttpStatus.OK).body(debtDto_output);
    }

    @PostMapping("/statusPending/{debtId}")
    public ResponseEntity<DebtDto> setDebtStatusToPendingAndExpenseStatusToPendingAsWell(@PathVariable Long debtId){
        LOGGER.info("Status is changing");
        DebtDto debtDto = debtService.setDebtAndExpenseStatusToPending(debtId);
        LOGGER.info("Status is changed");
        return ResponseEntity.status(HttpStatus.OK).body(debtDto);
    }

    @PostMapping("/statusActive/{debtId}")
    public ResponseEntity<DebtDto> setDebtStatusToActiveAndExpenseStatusToActiveAsWell(@PathVariable Long debtId){
        DebtDto debtDto = debtService.setDebtAndExpenseStatusToActive(debtId);
        return ResponseEntity.status(HttpStatus.OK).body(debtDto);
    }

    @PostMapping("/statusDone/{debtId}")
    public ResponseEntity<String> setDebtStatusToDoneAndExpenseStatusToDoneWell(@PathVariable Long debtId){
        debtService.setDebtAndExpenseStatusToDone(debtId);
        return ResponseEntity.status(HttpStatus.OK).body("Debt and expense are DONE and deleted.");
    }
}
