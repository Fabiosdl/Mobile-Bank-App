package com.fabiolima.mobilebankapp.controller;

import com.fabiolima.mobilebankapp.entities.User;
import com.fabiolima.mobilebankapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //use RestController to test using postman. For websites use //Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public List<User> showAllUsers(){
        return userService.findAllUsers();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by id", description = "Retrieves user by its id")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "User not found")
    public User showUser(@PathVariable int userId){
        return userService.findUserByUserId(userId);
    }

    // create new user and userdetails
    @PostMapping()
    @Operation(summary = "Create new user")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public User createNewUser(@RequestBody User theUser){
        theUser.setId(0);// to guarantee that it will not overwrite any existing user
        return userService.saveUser(theUser);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user by its id")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "403", description = "Id in the body doesn't match id in the parameter")
    @ApiResponse(responseCode = "404", description = "User not found")
    public User updateUser(@PathVariable int userId,
                           @RequestBody User theUser){
        return userService.updateUserByUserId(userId,theUser);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user by id")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "User not found")
    public User deleteUser(@PathVariable int userId){
        return userService.deleteUserByUserId(userId);
    }
}