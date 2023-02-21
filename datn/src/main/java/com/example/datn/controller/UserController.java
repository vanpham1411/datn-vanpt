package com.example.datn.controller;

import com.example.datn.domain.entity.User;
import com.example.datn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/User/Login")
    public ResponseEntity login(@RequestBody User user) {
        return ResponseEntity.ok(new ResponseBody(
                userService.login(user),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

    @PostMapping("/User")
    public ResponseEntity register(@RequestBody User user) {
        int res =userService.signup(user);
        if( res == 0) {
            return ResponseEntity.ok(new ResponseBody(
                    0,
                    ResponseBody.Status.FAILED,
                    ResponseBody.Code.CLIENT_ERROR
            ));
        }
        return ResponseEntity.ok(new ResponseBody(
                1,
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

    @PutMapping("/User/UpdateUser")
    public ResponseEntity update(@RequestBody User user) {
        return ResponseEntity.ok(new ResponseBody(
                userService.updateUser(user),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }
}
