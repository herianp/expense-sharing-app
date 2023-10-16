package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.dto.DebtDto;
import com.herian.expensesharingapp.entity.Debt;
import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.repository.DebtRepository;
import com.herian.expensesharingapp.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DebtServiceImpl implements DebtService {

    @Autowired
    DebtRepository debtRepository;
    @Autowired
    PersonRepository personRepository;

    @Override
    public DebtDto createDebt(DebtDto debtDto) {
        Debt debt = mapDebtDtoToDebt(debtDto);
        debt.setCreatedAt(LocalDateTime.now());
        Debt savedDebt = debtRepository.save(debt);
        return mapDebtToDebtDto(savedDebt);
    }

    @Override
    public DebtDto getDebtById(Long id) {
        Optional<Debt> debtOptional = debtRepository.findById(id);
        if (debtOptional.isEmpty()) {
            throw new RuntimeException("Debt with ID:" + id + " does not EXISTS!..");
        }
        return mapDebtToDebtDto(debtOptional.get());
    }

    @Override
    public List<DebtDto> getDebtListByPersonId(Long personId) {
        List<Debt> debtList = debtRepository.findDebtListByPersonId(personId);
        return debtList.stream().map(this::mapDebtToDebtDto).collect(Collectors.toList());
    }

    public DebtDto mapDebtToDebtDto(Debt debt) {
        DebtDto debtDto = new DebtDto();
        debtDto.setAmount(debt.getAmount());
        debtDto.setDescription(debt.getDescription());
        debtDto.setCreatedAt(debt.getCreatedAt());
        debtDto.setDueDate(debt.getDueDate());
        debtDto.setPersonIdToPayBack(debt.getPersonIdToPayBack());
        debtDto.setPersonId(debt.getPerson().getId());
        return debtDto;
    }

    public Debt mapDebtDtoToDebt(DebtDto debtDto) {
        Debt debt = new Debt();
        debt.setAmount(debtDto.getAmount());
        debt.setDescription(debtDto.getDescription());
        debt.setCreatedAt(debtDto.getCreatedAt());
        debt.setDueDate(debtDto.getDueDate());
        debt.setPersonIdToPayBack(debtDto.getPersonIdToPayBack());
        Person person = personRepository.findById(debtDto.getPersonId()).get();
        debt.setPerson(person);
        return debt;
    }
}
