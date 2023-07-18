package com.emailDemo.springemailclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emailDemo.springemailclient.dto.OtpDto;
import com.emailDemo.springemailclient.dto.UserDto;
import com.emailDemo.springemailclient.entity.User;
import com.emailDemo.springemailclient.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public String signUp(@RequestBody UserDto userDto) {
		User createdUser = userService.createUsers(userDto);
		return "registered successfully....";
	}

	@PostMapping("/otp")
	public String insertOtp(@RequestBody OtpDto otpDto) {
		int userId = otpDto.getUserId();
		userService.createOtp(userId);
		return "otp generated";
	}

	@GetMapping("/verify-account/{email}/{otp}")
	public ResponseEntity<String> verifyAccount(@PathVariable String email, @PathVariable String otp) {
		return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
	}

	@PutMapping("/verify/{email}")
	public String verifyAccountThroughMail(@PathVariable String email) {
		userService.verifyAccountBySendingEmail(email);
		return "check your mail....!";
	}

	@PutMapping("/regenerate-otp/{email}")
	public ResponseEntity<String> regenerateOtp(@PathVariable String email) {
		return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
	}

}
