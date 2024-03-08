package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.dto.GroupDto;
import com.herian.expensesharingapp.dto.GroupForSinglePageDto;
import com.herian.expensesharingapp.dto.PersonDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface GroupService {

    GroupDto getGroupByName(String name);
    GroupDto createGroup(String name, Long personId);

    GroupForSinglePageDto getGroupForSinglePageDtoById(Long id);
    GroupForSinglePageDto getGroupForSinglePageDtoByName(String groupName);

    @Transactional
    void deleteGroup(String groupName, Long personId);

    @Transactional
    PersonDto addMemberToGroup(String groupName, String memberEmail);

    @Transactional
    Long deleteMemberFromGroup(String groupName, String memberEmail);

    void closeAndCalculateGroup(String groupName, Long personId);
}
