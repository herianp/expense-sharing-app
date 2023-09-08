package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person findByEmail(String email){
        return personRepository.findOneByEmail(email);
    }
}
