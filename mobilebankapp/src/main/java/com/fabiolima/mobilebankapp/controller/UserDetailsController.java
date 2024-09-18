package com.fabiolima.mobilebankapp.controller;

import com.fabiolima.mobilebankapp.entities.UserDetails;
import com.fabiolima.mobilebankapp.service.UserDetailsService;
import com.fabiolima.mobilebankapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/details")
public class UserDetailsController {

    private UserService userService;
    private UserDetailsService userDetailsService;

    @Autowired
    public UserDetailsController(UserService userService,
                                 UserDetailsService userDetailsService){
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping()
    @Operation(summary = "Get user's detail", description = "Retrieves a user's detail by user id.")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public UserDetails findUserDetailsByUserId(@PathVariable int userId){

        return userDetailsService.findUserDetailsByUserId(userId);
    }

    @PostMapping()
    @Operation(summary = "Create new user details")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public UserDetails createDetailsByUserId(@PathVariable int userId,
                                             @RequestBody UserDetails userDetails){
        return userDetailsService.createUserDetailsAndAddToUser(userId, userDetails);
    }

    @PutMapping()
    @Operation(summary = "Update user details", description = "Update user details by user id")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "403", description = "Id in the body doesn't match id in the parameter")
    @ApiResponse(responseCode = "404", description = "Details not found")
    public UserDetails updateUserDetailsByUserId(@PathVariable int userId,
                                                 @RequestBody UserDetails userDetails){

        return userDetailsService.updateUserDetails(userId, userDetails);
    }

    @DeleteMapping()
    @Operation(summary = "Delete user details", description = "Delete user details by user id")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Details not found")
    public UserDetails deleteUserDetailsByUserId(@PathVariable int userId){
        return userDetailsService.deleteUserDetailsById(userId);
    }
}