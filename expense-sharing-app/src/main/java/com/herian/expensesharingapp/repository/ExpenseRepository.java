package com.herian.expensesharingapp.repository;

import com.herian.expensesharingapp.entity.Debt;
import com.herian.expensesharingapp.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findById(Long id);

    @Query("SELECT e FROM Expense e WHERE e.person.id = :personId")
    List<Expense> findExpenseListByPersonId(Long personId);
}
