package com.herian.expensesharingapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @GetMapping("/home")
    public ResponseEntity<String> homepage(){
        return ResponseEntity.ok().body("Home");
    }
}
