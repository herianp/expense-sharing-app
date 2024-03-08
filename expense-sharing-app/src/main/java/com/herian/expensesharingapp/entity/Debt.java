package com.herian.expensesharingapp.entity;

import com.herian.expensesharingapp.enums.StatusOfAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;
    private LocalDateTime createdAt;
    private String description;
    private LocalDateTime dueDate;
    private Long personIdToPayBack;

    @ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToOne
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @Enumerated(EnumType.STRING)
    private StatusOfAction status;

    public void setExpense(Expense expense) {
        if (expense == null) {
            if (this.expense != null) {this.expense.setDebt(null);}
        } else { expense.setDebt(this);}
        this.expense = expense;
    }
}
