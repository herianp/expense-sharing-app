package com.herian.expensesharingapp.controller;

import com.herian.expensesharingapp.dto.GroupDto;
import com.herian.expensesharingapp.dto.GroupForSinglePageDto;
import com.herian.expensesharingapp.dto.PersonDto;
import com.herian.expensesharingapp.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/group")
public class GroupController {

    private final Logger logger = LoggerFactory.getLogger(PersonController.class);
    @Autowired
    private GroupService groupService;

    @GetMapping("/{name}")
    public ResponseEntity<GroupDto> getGroupByName(@PathVariable String name) {
        logger.info("Success call: GET api/group/" + name);
        GroupDto groupDto = groupService.getGroupByName(name);
        logger.info("GroupDto: " + groupDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(groupDto);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<GroupForSinglePageDto> getGroupById(@PathVariable Long id) {
        logger.info("Success call: GET api/group/" + id);
        GroupForSinglePageDto groupDto = groupService.getGroupForSinglePageDtoById(id);
        return ResponseEntity.status(HttpStatus.OK).body(groupDto);
    }

    @GetMapping("/name/{groupName}")
    public ResponseEntity<GroupForSinglePageDto> getGroupForSinglePageDtoByName(@PathVariable String groupName) {
        GroupForSinglePageDto groupDto = groupService.getGroupForSinglePageDtoByName(groupName);
        return ResponseEntity.status(HttpStatus.OK).body(groupDto);
    }

    @PostMapping("/{groupName}/{personId}")
    public ResponseEntity<GroupDto> createGroup(@PathVariable String groupName, @PathVariable Long personId ) {
        logger.info("Success call: POST api/group");
        GroupDto outputGroupDto = groupService.createGroup(groupName, personId);
        if (outputGroupDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(outputGroupDto);
    }

    @PostMapping("/delete/{groupName}/{personId}")
    public ResponseEntity<String> deleteGroup(@PathVariable String groupName, @PathVariable Long personId ) {
        groupService.deleteGroup(groupName, personId);
        return ResponseEntity.status(HttpStatus.OK).body("Group was deleted");
    }

    @PostMapping("/{groupName}/member/{memberEmail}")
    public ResponseEntity<PersonDto> addMemberToGroup(@PathVariable String groupName, @PathVariable String memberEmail) {
        PersonDto memberDTO= groupService.addMemberToGroup(groupName, memberEmail);
        return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
    }

    @PostMapping("/delete/{groupName}/member/{memberEmail}")
    public ResponseEntity<Long> deleteMemberFromGroup(@PathVariable String groupName, @PathVariable String memberEmail) {
        Long memberId = groupService.deleteMemberFromGroup(groupName, memberEmail);
        return ResponseEntity.status(HttpStatus.OK).body(memberId);
    }

    @PostMapping("/close/{groupName}/person/{personId}")
    public ResponseEntity<String> closeAndCalculateGroup(@PathVariable String groupName, @PathVariable Long personId) {
        groupService.closeAndCalculateGroup(groupName, personId);
        return ResponseEntity.status(HttpStatus.OK).body("Group was calculated and deleted");
    }
}
