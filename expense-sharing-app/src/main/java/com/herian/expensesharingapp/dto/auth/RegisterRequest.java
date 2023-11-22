package com.herian.expensesharingapp.dto.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String userName;
    private String email;
    private String password;
}
