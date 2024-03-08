package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.dto.DebtDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface DebtService {

    DebtDto createDebt(DebtDto debtDto);

    DebtDto getDebtById(Long id);

    List<DebtDto> getDebtListByPersonId(Long personId);

    //@Transactional slouzi k zruseni transakce, kdyz se ulozi jen jedna z entit
    @Transactional
    DebtDto createDebtWithExpense(DebtDto debtDto);

    @Transactional
    DebtDto setDebtAndExpenseStatusToPending(Long debtId);

    @Transactional
    DebtDto setDebtAndExpenseStatusToActive(Long debtId);

    @Transactional
    void setDebtAndExpenseStatusToDone(Long debtId);
}
