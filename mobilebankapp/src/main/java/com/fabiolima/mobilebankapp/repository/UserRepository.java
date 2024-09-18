package com.fabiolima.mobilebankapp.repository;

import com.fabiolima.mobilebankapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

}
