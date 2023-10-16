package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.dto.DebtDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DebtService {

    DebtDto createDebt(DebtDto debtDto);
    DebtDto getDebtById(Long id);

    List<DebtDto> getDebtListByPersonId(Long personId);

}
