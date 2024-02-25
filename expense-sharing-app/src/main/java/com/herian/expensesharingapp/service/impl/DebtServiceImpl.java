package com.herian.expensesharingapp.service.impl;

import com.herian.expensesharingapp.dto.DebtDto;
import com.herian.expensesharingapp.entity.Debt;
import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.repository.DebtRepository;
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
}
