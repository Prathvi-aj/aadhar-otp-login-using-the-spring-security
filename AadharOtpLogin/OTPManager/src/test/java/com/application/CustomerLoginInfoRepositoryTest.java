package com.application;

import com.application.entities.CustomerLoginInfo;
import com.application.repos.CustomerLoginInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerLoginInfoRepositoryTest {

    @Autowired
    private CustomerLoginInfoRepository customerLoginInfoRepository;

    @Test
    public void testFindByAadharNumber() {
        String aadharNumber = "123456789012";
        CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();
        customerLoginInfo.setAadharNumber(aadharNumber);
        customerLoginInfoRepository.save(customerLoginInfo);

        CustomerLoginInfo found = customerLoginInfoRepository.findByAadharNumber(aadharNumber);
        assertNotNull(found);
        assertEquals(aadharNumber, found.getAadharNumber());
    }
}

