package com.herian.expensesharingapp.config.filter;

import org.springframework.security.core.GrantedAuthority;

public class DeleteAuthority implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return null;
    }
}
