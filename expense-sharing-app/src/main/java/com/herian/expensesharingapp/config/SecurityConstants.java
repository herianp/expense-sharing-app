package com.herian.expensesharingapp.config;

import org.springframework.beans.factory.annotation.Value;

public class SecurityConstants {
    @Value("${security.jwt.token.secret-key:jzgEQasddfsdfLKDMFasd2390AklmKLASD!#$42440}")
    public static String JWT_KEY;
    public static final String JWT_HEADER = "Authorization";
}
