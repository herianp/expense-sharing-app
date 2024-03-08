package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.dto.PersonDto;
import com.herian.expensesharingapp.dto.PersonFriendDto;
import com.herian.expensesharingapp.entity.PersonFriend;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PersonService {

    PersonDto findOneByEmail(String email);

    PersonDto createPerson(PersonDto personDto);

    PersonFriendDto createFriend(String email);

    void deleteFriend(String email);

    PersonDto getFriendByEmail(String email);

    List<PersonDto> findPersonListByGroupName(String groupName);
}
