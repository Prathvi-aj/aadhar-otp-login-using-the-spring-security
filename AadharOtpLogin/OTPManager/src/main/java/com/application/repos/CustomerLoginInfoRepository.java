package com.application.repos;

import com.application.dtos.CustomerLoginInfoDto;
import com.application.entities.CustomerLoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerLoginInfoRepository extends JpaRepository<CustomerLoginInfo,Long> {
    CustomerLoginInfo findByAadharNumber(String aadharNumber);
}
