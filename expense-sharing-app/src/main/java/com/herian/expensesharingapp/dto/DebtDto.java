package com.herian.expensesharingapp.dto;

import com.herian.expensesharingapp.entity.Person;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebtDto {
    private Long id;
    private Long amount;
    private LocalDateTime createdAt;
    private String description;
    private LocalDateTime dueDate;
    private Long personIdToPayBack;
    private String personNameToPayBack;
    private Long personId;
    private String status;
}
