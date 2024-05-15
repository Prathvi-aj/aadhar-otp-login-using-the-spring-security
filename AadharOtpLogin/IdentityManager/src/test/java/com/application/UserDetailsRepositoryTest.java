package com.application;

import com.application.entities.UserDetails;
import com.application.repos.UserDetailsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserDetailsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Test
    void findByMobileNumber_ExistingMobileNumber_ReturnsUserDetails() {
        UserDetails user = new UserDetails();
        user.setMobileNumber("1234567890");
        entityManager.persistAndFlush(user);

        UserDetails foundUser = userDetailsRepository.findByMobileNumber("1234567890");
        assertEquals(user.getMobileNumber(), foundUser.getMobileNumber());
    }

    @Test
    void findByMobileNumber_NonExistingMobileNumber_ReturnsNull() {
        UserDetails foundUser = userDetailsRepository.findByMobileNumber("1234567890");
        assertNull(foundUser);
    }

    @Test
    void findByAadharNumber_ExistingAadharNumber_ReturnsUserDetails() {
        UserDetails user = new UserDetails();
        user.setAadharNumber("12345678901234");
        entityManager.persistAndFlush(user);

        UserDetails foundUser = userDetailsRepository.findByAadharNumber("12345678901234");
        assertEquals(user.getAadharNumber(), foundUser.getAadharNumber());
    }

    @Test
    void findByAadharNumber_NonExistingAadharNumber_ReturnsNull() {
        UserDetails foundUser = userDetailsRepository.findByAadharNumber("12345678901234");
        assertNull(foundUser);
    }
}

