package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.util.OtpGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpGenerator otpGenerator;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Đăng ký tài khoản
    public void registerUser(String email, String password) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Tạo người dùng mới
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setVerified(false);
        userRepository.save(user);

        // Gửi OTP qua email
        String otp = otpGenerator.generateOtp();
        emailService.sendEmail(email, "Your OTP Code", "Your OTP is: " + otp);

        // Lưu OTP (trong thực tế nên lưu vào database hoặc bộ nhớ cache)
        otpGenerator.storeOtp(email, otp);
    }

    // Xác thực OTP
    public void verifyUser(String email, String otp) {
        String storedOtp = otpGenerator.getStoredOtp(email);
        if (storedOtp == null || !storedOtp.equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // Đánh dấu người dùng là đã xác thực
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setVerified(true);
            userRepository.save(user);
        }
    }

    // Đăng nhập
    public boolean login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.isVerified() && passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
