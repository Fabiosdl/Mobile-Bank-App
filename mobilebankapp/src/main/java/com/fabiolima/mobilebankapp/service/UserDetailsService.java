package com.fabiolima.mobilebankapp.service;

import com.fabiolima.mobilebankapp.entities.User;
import com.fabiolima.mobilebankapp.entities.UserDetails;

public interface UserDetailsService {

    UserDetails saveDetails(UserDetails userDetails);
    UserDetails createUserDetailsAndAddToUser(int userId, UserDetails userDetails);
    UserDetails findUserDetailsByUserId(int userId);
    UserDetails updateUserDetails(int userId, UserDetails theUserDetails);
    UserDetails deleteUserDetailsById(int userId);
}
