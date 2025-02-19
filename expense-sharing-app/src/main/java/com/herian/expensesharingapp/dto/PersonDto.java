package com.herian.expensesharingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private List<DebtDto> debtList;
    private List<ExpenseDto> expenseList;
    private List<GroupDto> groupList;
    private Set<PersonFriendDto> personFriends;
}
