package com.emailDemo.springemailclient.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.emailDemo.springemailclient.dto.UserDto;
import com.emailDemo.springemailclient.entity.Otp;
import com.emailDemo.springemailclient.entity.User;
import com.emailDemo.springemailclient.repository.OtpRepo;
import com.emailDemo.springemailclient.repository.UserRepo;
import com.emailDemo.springemailclient.util.OtpGenerator;

import jakarta.mail.MessagingException;

@Service
@EnableScheduling
public class UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private OtpRepo otpRepo;

	@Autowired
	private User user;

	@Autowired
	private JavaMailSender mailSender;

	String otp1 = OtpGenerator.generateOtp();

	String sendOtpMail;

	public User createUsers(UserDto userDto) {
		User user = new User();
		String verificationCode = UUID.randomUUID().toString();
		user.setVerificationcode(verificationCode);
		user.setVerified(false);
		user.setEmail(userDto.getEmail());
		user.setUsername(userDto.getUsername());
		user.setPassword(userDto.getPassword());
		userRepo.save(user);
		return user;

	}

	public Otp createOtp(int userId) {
		User user = new User();
		user.setId(userId);
		Otp o = new Otp();
		o.setOtp(otp1);
		o.setOtpGeneratedTime(LocalDateTime.now());
		o.setUser(user);
		return otpRepo.save(o);
	}

	public String verifyAccount(String email, String otp2) {
		User user = userRepo.findByEmail(email);
		Otp verifyotp = otpRepo.findByUserId(user.getId());

		if (verifyotp != null && verifyotp.getOtp() != null && verifyotp.getOtp().equals(otp2)) {
			LocalDateTime otpGeneratedTime = verifyotp.getOtpGeneratedTime();
			LocalDateTime now = LocalDateTime.now();
			long seconds = ChronoUnit.SECONDS.between(otpGeneratedTime, now);
			// sendVerificationCode(user.getVerificationcode());
			if (seconds < (1 * 60)) {
				user.setVerified(true);
				userRepo.save(user);
				return "OTP verified. You can log in.";
			}
		}

		return "Please regenerate OTP and try again.";
	}

	public void sendOtp(String otp) {
		// String toemail = user.getEmail();
		User user = userRepo.findByEmail(sendOtpMail);

		if (user != null) {
			Otp verifyotp = otpRepo.findByUserId(user.getId());
			String otp2 = verifyotp.getOtp();

			sendOtpMail = user.getEmail();
			// Email = toemail;
			if (sendOtpMail != null) {
				SimpleMailMessage message = new SimpleMailMessage();
				message.setTo(sendOtpMail);
				message.setSubject("Verification Code");
				message.setText("Click this link for verification: http://localhost:8080/verify-account/" + sendOtpMail
						+ "/" + otp2);

				mailSender.send(message);
				System.out.println("Verification mail sent");
			}
		}
	}

	public String verifyAccountBySendingEmail(String email) {
		sendOtpMail = email;
		User user = userRepo.findByEmail(email);

		if (user != null) {

			Otp verifyotp = otpRepo.findByUserId(user.getId());
			String otp3 = verifyotp.getOtp();

			if (verifyotp != null && verifyotp.getOtp() != null && verifyotp.getOtp().equals(otp3)) {
				// System.out.println("verify");
				sendOtp(otp3);
				LocalDateTime otpGeneratedTime = verifyotp.getOtpGeneratedTime();
				LocalDateTime now = LocalDateTime.now();
				long seconds = ChronoUnit.SECONDS.between(otpGeneratedTime, now);

				if (seconds < (1 * 60)) {
					user.setVerified(true);
					userRepo.save(user);
					return "OTP verified. You can log in.";
				}
			}
		}

		return "Please regenerate OTP and try again.";
	}

	public String regenerateOtp(String sendOtpMail) {
		User user = userRepo.findByEmail(sendOtpMail);
		Otp verifyotp = otpRepo.findByUserId(user.getId());
		String otp4 = OtpGenerator.generateOtp();
		verifyotp.setOtp(otp4);
		verifyotp.setOtpGeneratedTime(LocalDateTime.now());
		otpRepo.save(verifyotp);
		System.out.println("new otp");
		// sendOtp(otp4);
		verifyAccountBySendingEmail(sendOtpMail);
		return "Email sent... please verify account within 1 minute";
	}

}
