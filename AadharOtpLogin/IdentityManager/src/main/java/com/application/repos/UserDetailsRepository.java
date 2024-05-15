package com.application.repos;

import com.application.entities.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    UserDetails findByMobileNumber(String mobileNumber);
    UserDetails findByAadharNumber(String aadharNumber);
}
