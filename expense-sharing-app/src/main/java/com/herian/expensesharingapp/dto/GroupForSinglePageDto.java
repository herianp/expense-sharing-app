package com.herian.expensesharingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupForSinglePageDto {
    private Long id;
    private String name;
    private String description;
    private Long groupOwnerId;
    private LocalDateTime createdAt;
    private List<String> personNames;
    private List<String> personEmails;
    private List<ExpenseDto> expenseList;
    private List<DebtDto> debtList;
}
