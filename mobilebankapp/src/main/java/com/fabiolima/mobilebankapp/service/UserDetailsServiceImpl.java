package com.fabiolima.mobilebankapp.service;

import com.fabiolima.mobilebankapp.entities.User;
import com.fabiolima.mobilebankapp.entities.UserDetails;
import com.fabiolima.mobilebankapp.exceptions.BadRequestException;
import com.fabiolima.mobilebankapp.exceptions.NotFoundException;
import com.fabiolima.mobilebankapp.repository.UserDetailsRepository;
import com.fabiolima.mobilebankapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.awt.dnd.InvalidDnDOperationException;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private UserDetailsRepository userDetailsRepository;
    private UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserDetailsRepository userDetailsRepository,
                                  UserService userService){
        this.userDetailsRepository = userDetailsRepository;
        this.userService = userService;
    }

    @Override
    public UserDetails saveDetails(UserDetails userDetails) {
        return userDetailsRepository.save(userDetails);
    }

    @Override
    @ExceptionHandler
    public UserDetails createUserDetailsAndAddToUser(int userId, UserDetails userDetails) {
        // retrieve user
        User tempUser = userService.findUserByUserId(userId);

        // if the user doesn't have a user details, go ahead and add the new one
        if(tempUser.getUserDetails() == null){
            userDetails.setId(0); // set id to 0 to do not overwrite another details by mistake

            userDetails.setUser(tempUser);// setting user in the object details

            tempUser.setUserDetails(userDetails);// setting details into object user

            //save the user that it will cascade to save the details as well
            User savedUser = userService.saveUser(tempUser);

            return savedUser.getUserDetails();
        }
        // if the user already has a user details, throw http conflict error
        else throw new ResponseStatusException(HttpStatus.CONFLICT,"User already has user details in the records.");
    }

    @Override
    public UserDetails findUserDetailsByUserId(int userId) {
        //retrieve user
        User tempUser = userService.findUserByUserId(userId);

        // retrieve user details id if it exists
        if(tempUser.getUserDetails() == null)
            throw new NotFoundException("Details are not set for this user");

        int detailsId = tempUser.getUserDetails().getId();

        // retrieve user details
        Optional<UserDetails> result = userDetailsRepository.findById(detailsId);
        if(result.isEmpty()) throw new NotFoundException("User details not found");
        else return result.get();
    }

    @Override
    public UserDetails updateUserDetails(int userId, UserDetails userDetails) {

        // retrieve the user by user id
        User tempUser = userService.findUserByUserId(userId);

        // retrieve user details id from user (similar to detailsId from parameter)
        int detailsId = tempUser.getUserDetails().getId();

        // check if the details id in the parameter is the same as in the body
        if(userDetails.getId() != (detailsId))
            throw new BadRequestException("User id in the parameter does not " +
                    "match the user id in the body");

        // set the new UserDetails to the User
        userDetails.setUser(tempUser);

        //add new details to user object
        tempUser.setUserDetails(userDetails);

        // save the user
        User savedUser = userService.saveUser(tempUser);

        //return details from savedUser
        return savedUser.getUserDetails();
    }

    @Override
    public UserDetails deleteUserDetailsById(int userId) {
        // retrieve the user
        User tempUser = userService.findUserByUserId(userId);

        // retrieve user details from retrieved user
        int userDetailsId = tempUser.getUserDetails().getId();

        // retrieve the details
        UserDetails tempDetails = findUserDetailsByUserId(userId);

        // break the connection between the 2 entities
        tempUser.setUserDetails(null);

        // save the user with no details
        userService.saveUser(tempUser);

        // delete the user details
        userDetailsRepository.deleteById(userDetailsId);

        // return deleted details for user confirmation
        return tempDetails;
    }
}