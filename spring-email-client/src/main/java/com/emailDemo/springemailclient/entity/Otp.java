package com.emailDemo.springemailclient.entity;



import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "otp_table")
public class Otp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "DateAndTime")
	private LocalDateTime otpGeneratedTime;

	@Column(name = "otp", nullable = false)
	private String otp;
    @JsonIgnore
	@OneToOne
	private User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getOtpGeneratedTime() {
		return otpGeneratedTime;
	}

	public void setOtpGeneratedTime(LocalDateTime otpGeneratedTime) {
		this.otpGeneratedTime = otpGeneratedTime;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Otp() {
		super();
	}

	public Otp(int id, LocalDateTime otpGeneratedTime, String otp, User user) {
		super();
		this.id = id;
		this.otpGeneratedTime = otpGeneratedTime;
		this.otp = otp;
		this.user = user;
	}

	@Override
	public String toString() {
		return "Otp [id=" + id + ", otpGeneratedTime=" + otpGeneratedTime + ", otp=" + otp + ", user=" + user + "]";
	}
	
	
}
