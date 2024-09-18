package com.fabiolima.mobilebankapp.repository;

import com.fabiolima.mobilebankapp.entities.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails,Integer> {
}
