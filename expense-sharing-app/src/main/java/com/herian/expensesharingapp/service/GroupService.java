package com.herian.expensesharingapp.service;

import com.herian.expensesharingapp.dto.GroupDto;
import org.springframework.stereotype.Service;

@Service
public interface GroupService {

    GroupDto getGroupByName(String name);
    GroupDto createGroup(GroupDto groupDto);
}
