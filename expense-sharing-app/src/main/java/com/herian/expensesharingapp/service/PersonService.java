package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.dto.LoginDto;
import com.herian.expensesharingapp.dto.PersonDto;
import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PersonService {

    PersonDto findByEmail(String email);

    PersonDto createPerson(PersonDto personDto);

    void loginPerson(LoginDto loginDto);
}
