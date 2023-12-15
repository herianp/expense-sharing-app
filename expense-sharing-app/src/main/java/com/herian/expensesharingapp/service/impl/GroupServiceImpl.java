package com.herian.expensesharingapp.service.impl;

import com.herian.expensesharingapp.controller.PersonController;
import com.herian.expensesharingapp.dto.GroupDto;
import com.herian.expensesharingapp.entity.Group;
import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.repository.GroupRepository;
import com.herian.expensesharingapp.repository.PersonRepository;
import com.herian.expensesharingapp.service.GroupService;
import com.herian.expensesharingapp.service.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;

    private final EntityMapper entityMapper;
    private final PersonRepository personRepository;

    @Override
    public GroupDto getGroupByName(String name) {
        Optional<Group> group = groupRepository.findGroupByName(name);
        if (group.isEmpty()) {
            throw new RuntimeException("Group with name: " + name + "does not exists!");
        }
        return entityMapper.mapGroupToGroupDto(group.get());
    }

    @Override
    public GroupDto createGroup(String name, Long personId) {
        if (name.equals("") || personId == null) {
            return null;
        }
        Person owner = personRepository.findById(personId)
                .orElseThrow(() -> new RuntimeException("Creating group was stopped, because person id is wrong!"));
        Group group = new Group();
        group.setName(name);
        group.setGroupOwnerId(owner.getId());
        group.setCreatedAt(LocalDateTime.now());
        group.setPersonList(List.of(owner));
        group.setExpenseList(new ArrayList<>());
        owner.getGroupList().add(group);
        logger.info("raw OWNER id: " + group.getGroupOwnerId());
        Group savedGroup = groupRepository.save(group);
        logger.info("Saved OWNER id: " + savedGroup.getGroupOwnerId());
        return entityMapper.mapGroupToGroupDto(savedGroup);
    }
}
