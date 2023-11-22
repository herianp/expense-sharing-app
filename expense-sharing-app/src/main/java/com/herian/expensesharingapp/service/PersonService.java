package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.dto.PersonDto;
import org.springframework.stereotype.Service;

@Service
public interface PersonService {

    PersonDto findOneByEmail(String email);

    PersonDto createPerson(PersonDto personDto);
}
