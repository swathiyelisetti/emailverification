package com.emailDemo.springemailclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emailDemo.springemailclient.entity.Otp;

@Repository
public interface OtpRepo extends JpaRepository<Otp, Integer>{

	

	Otp findByUserId(int id);

}
