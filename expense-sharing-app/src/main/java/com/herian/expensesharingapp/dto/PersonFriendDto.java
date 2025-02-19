package com.herian.expensesharingapp.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonFriendDto {

    private Long id;
    private Long personId;
    private Long friendId;
    private String username;
    private String friendEmail;
}
