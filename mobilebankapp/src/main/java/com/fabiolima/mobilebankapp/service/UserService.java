package com.fabiolima.mobilebankapp.service;

import com.fabiolima.mobilebankapp.entities.Account;
import com.fabiolima.mobilebankapp.entities.User;
import com.fabiolima.mobilebankapp.entities.UserDetails;

import java.util.List;

public interface UserService {

    User saveUser(User tempUser);
    User findUserByUserId(int userId);
    List<User> findAllUsers();
    User updateUserByUserId(int userId,User tempUser);
    User deleteUserByUserId(int userId);
}
