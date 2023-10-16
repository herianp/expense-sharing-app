package com.herian.expensesharingapp.repository;

import com.herian.expensesharingapp.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

    @Query("SELECT d FROM Debt d WHERE d.person.id = :personId")
    List<Debt> findDebtListByPersonId(Long personId);

    Optional<Debt> findById(Long id);
}
