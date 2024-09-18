package com.fabiolima.mobilebankapp.service;

import com.fabiolima.mobilebankapp.entities.User;
import com.fabiolima.mobilebankapp.exceptions.BadRequestException;
import com.fabiolima.mobilebankapp.exceptions.NotFoundException;
import com.fabiolima.mobilebankapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User tempUser) {
        return userRepository.save(tempUser);
    }

    @Override
    public User findUserByUserId(int userId) {
        Optional<User> result = userRepository.findById(userId);
        if(!result.isPresent()) {
            throw new NotFoundException("User not found");
        }
        else return result.get();
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUserByUserId(int userId,User tempUser) {

        // first check if the user id in the parameter matches the user id in the body.
        if(tempUser.getId() != (userId))
            throw new BadRequestException("User id in the parameter does not " +
                    "match the user id in the body");

        // If the request is correct, find user. check if it exists; throw an error if it doesn't
        findUserByUserId(userId);

        // save the updated user data
        return userRepository.save(tempUser);
    }

    @Override
    public User deleteUserByUserId(int userId) {

        // check if user exists; throw an error if it doesn't
        User tempUser = findUserByUserId(userId);
        // delete user
        userRepository.deleteById(userId);
        return tempUser;
    }
}
