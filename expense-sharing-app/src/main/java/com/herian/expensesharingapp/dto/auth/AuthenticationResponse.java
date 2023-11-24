package com.herian.expensesharingapp.dto.auth;

import com.herian.expensesharingapp.dto.PersonDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private PersonDto personDto;
    private String token;
}
