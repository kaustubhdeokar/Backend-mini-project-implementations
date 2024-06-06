package com.bookacab.user.controller;

import com.bookacab.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/update")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/receive")
    public ResponseEntity<List<String>> getLocationUpdate(){
        List<String> location = userService.fetchDriverLocation();
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

}
