package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.dto.PersonDto;
import com.herian.expensesharingapp.dto.PersonFriendDto;
import com.herian.expensesharingapp.entity.PersonFriend;
import org.springframework.stereotype.Service;

@Service
public interface PersonService {

    PersonDto findOneByEmail(String email);

    PersonDto createPerson(PersonDto personDto);

    PersonFriendDto createFriend(String email);

}
