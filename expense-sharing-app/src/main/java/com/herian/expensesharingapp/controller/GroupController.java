package com.herian.expensesharingapp.controller;

import com.herian.expensesharingapp.dto.GroupDto;
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
    public ResponseEntity<GroupDto> getGroup(@PathVariable String name) {
        logger.info("Success call: GET api/group/" + name);
        GroupDto groupDto = groupService.getGroupByName(name);
        logger.info("GroupDto: " + groupDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(groupDto);
    }

    @PostMapping("/")
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupDto groupDto) {
        logger.info("Success call: POST api/group");
        GroupDto outputGroupDto = groupService.createGroup(groupDto);
        if (outputGroupDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(outputGroupDto);
    }
}
