package com.herian.expensesharingapp.service.impl;

import com.herian.expensesharingapp.dto.GroupDto;
import com.herian.expensesharingapp.entity.Group;
import com.herian.expensesharingapp.repository.GroupRepository;
import com.herian.expensesharingapp.service.GroupService;
import com.herian.expensesharingapp.service.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    private final EntityMapper entityMapper;

    @Override
    public GroupDto getGroupByName(String name) {
        Optional<Group> group = groupRepository.findGroupByName(name);
        if (group.isEmpty()) {
            throw new RuntimeException("Group with name: " + name + "does not exists!");
        }
        return entityMapper.mapGroupToGroupDto(group.get());
    }

    @Override
    public GroupDto createGroup(GroupDto groupDto) {
        Group group;
        if (groupDto.getName() == null){
            group = entityMapper.mapGroupDtoToGroup(groupDto);
            group.setCreatedAt(LocalDateTime.now());
            Group savedGroup = groupRepository.save(group);
            return entityMapper.mapGroupToGroupDto(savedGroup);
        }
        return null;
    }
}
