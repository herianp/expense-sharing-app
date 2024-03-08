package com.herian.expensesharingapp.service.impl;

import com.herian.expensesharingapp.dto.DebtDto;
import com.herian.expensesharingapp.entity.Debt;
import com.herian.expensesharingapp.entity.Expense;
import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.enums.StatusOfAction;
import com.herian.expensesharingapp.repository.DebtRepository;
import com.herian.expensesharingapp.repository.ExpenseRepository;
import com.herian.expensesharingapp.repository.PersonRepository;
import com.herian.expensesharingapp.service.DebtService;
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
public class DebtServiceImpl implements DebtService {

    @Autowired
    DebtRepository debtRepository;
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    PersonRepository personRepository;

    private final EntityMapper entityMapper;

    @Override
    public DebtDto createDebt(DebtDto debtDto) {
        Debt debt = entityMapper.mapDebtDtoToDebt(debtDto);
        debt.setCreatedAt(LocalDateTime.now());
        debt.setDueDate(LocalDateTime.now().plusMonths(1));
        Debt savedDebt = debtRepository.save(debt);
        return entityMapper.mapDebtToDebtDto(savedDebt);
    }

    @Override
    public DebtDto getDebtById(Long id) {
        Optional<Debt> debtOptional = debtRepository.findById(id);
        if (debtOptional.isEmpty()) {
            throw new RuntimeException("Debt with ID:" + id + " does not EXISTS!..");
        }
        return entityMapper.mapDebtToDebtDto(debtOptional.get());
    }

    @Override
    public List<DebtDto> getDebtListByPersonId(Long personId) {
        List<Debt> debtList = debtRepository.findDebtListByPersonId(personId);
        return debtList.stream().map(entityMapper::mapDebtToDebtDto).collect(Collectors.toList());
    }

    @Override
    public DebtDto createDebtWithExpense(DebtDto debtDto) {
        Optional<Person> expenseOwner = personRepository.findById(debtDto.getPersonIdToPayBack());
        if (expenseOwner.isEmpty()) {
            throw new RuntimeException("Expense has no owner");
        }
        Debt debt = entityMapper.mapDebtDtoToDebt(debtDto);
        debt.setCreatedAt(LocalDateTime.now());
        debt.setDueDate(LocalDateTime.now().plusMonths(1));
        //set expense
        Expense expense = new Expense();
        expense.setAmount(debt.getAmount());
        expense.setDescription(debt.getDescription());
        expense.setCreatedAt(debt.getCreatedAt());
        expense.setDueDate(debt.getDueDate());
        expense.setPersonIdWhoIsPay(debtDto.getPersonId());
        expenseOwner.ifPresent(expense::setPerson);
        expenseRepository.save(expense);
        debt.setExpense(expense);

        Debt savedDebt = debtRepository.save(debt);
        return entityMapper.mapDebtToDebtDto(savedDebt);
    }

    @Override
    public DebtDto setDebtAndExpenseStatusToPending(Long debtId) {
        Debt debt = debtRepository.findById(debtId).orElseThrow(() ->
                new RuntimeException("Debt with id: " + debtId + " does not exist!"));
        debt.setStatus(StatusOfAction.PENDING);
        Debt saved_debt = debtRepository.save(debt);

        Expense expense = debt.getExpense();
        expense.setStatus(StatusOfAction.PENDING);
        expenseRepository.save(expense);
        return entityMapper.mapDebtToDebtDto(saved_debt);
    }

    @Override
    public DebtDto setDebtAndExpenseStatusToActive(Long debtId) {
        Debt debt = debtRepository.findById(debtId).orElseThrow(() ->
                new RuntimeException("Debt with id: " + debtId + " does not exist!"));
        debt.setStatus(StatusOfAction.ACTIVE);
        Debt saved_debt = debtRepository.save(debt);

        Expense expense = debt.getExpense();
        expense.setStatus(StatusOfAction.ACTIVE);
        expenseRepository.save(expense);
        return entityMapper.mapDebtToDebtDto(saved_debt);
    }

    @Override
    public void setDebtAndExpenseStatusToDone(Long debtId) {
        Debt debt = debtRepository.findById(debtId).orElseThrow(() ->
                new RuntimeException("Debt with id: " + debtId + " does not exist!"));
//        //THIS IS NOW USELLES, BCS I WANT TO DELETE IT, IF IT IS DONE
//        debt.setStatus(StatusOfAction.DONE);
//        Debt saved_debt = debtRepository.save(debt);
//
//        Expense expense = debt.getExpense();
//        expense.setStatus(StatusOfAction.DONE);
//        expenseRepository.save(expense);
        deleteExpenseAndDebt(debt);
    }

    public void deleteExpenseAndDebt(Debt debt) {
        Expense expense = debt.getExpense();
        expense.setDebt(null); // Disassociate the debt from the expense
        debt.setExpense(null); // If the relationship is bidirectional
        debtRepository.delete(debt); // Delete the debt

        expenseRepository.delete(expense); // Delete the expense
    }
}
