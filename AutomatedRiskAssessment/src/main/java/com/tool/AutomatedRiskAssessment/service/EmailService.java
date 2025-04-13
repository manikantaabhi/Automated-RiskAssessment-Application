package com.tool.AutomatedRiskAssessment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final ConcurrentHashMap<String, OtpData> otpStorage = new ConcurrentHashMap<>();
    private static final SecureRandom random = new SecureRandom();
    private static final long OTP_EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(5);  // OTP expires in 5 minutes

    // Generate and store OTP
    public String generateOtp(String email) {
        String otp = String.format("%06d", random.nextInt(1000000));
        otpStorage.put(email, new OtpData(otp, System.currentTimeMillis()));
        return otp;
    }


    public void sendOtpEmail(String toEmail, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Your OTP for Login");
        helper.setText("Your One-Time Password (OTP) is: <b>" + otp + "</b>", true);

        mailSender.send(message);
    }


    public void sendEmail(String toEmail, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(text, true);
        mailSender.send(message);
    }


    public boolean validateOtp(String email, String otp) {
        OtpData storedOtp = otpStorage.get(email);
        if (storedOtp != null && storedOtp.getOtp().equals(otp)) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - storedOtp.getTimestamp() <= OTP_EXPIRATION_TIME) {
                otpStorage.remove(email);  // OTP is valid, remove it after use
                return true;
            } else {
                otpStorage.remove(email);  // OTP expired
            }
        }
        return false;
    }


    private static class OtpData {
        private final String otp;
        private final long timestamp;

        public OtpData(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }

        public String getOtp() {
            return otp;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}