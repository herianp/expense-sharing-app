package com.herian.expensesharingapp.dto;

import com.herian.expensesharingapp.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {
    private Long amount;
    private LocalDateTime createdAt;
    private String description;
    private Long personId;
    private LocalDateTime dueDate;
    private Long personIdWhoIsPay;
    private String personNameWhoIsPay;
    private Long groupId;
}
