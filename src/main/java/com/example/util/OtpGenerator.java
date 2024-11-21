package com.example.util;

import java.security.SecureRandom;

public class OtpGenerator {

    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int OTP_LENGTH = 6;  // Độ dài OTP
    private final SecureRandom random = new SecureRandom();

    // Tạo OTP ngẫu nhiên
    public String generateOtp() {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return otp.toString();
    }

    // Lưu OTP vào bộ nhớ (hoặc có thể lưu vào cache/database)
    public void storeOtp(String email, String otp) {
        // Lưu OTP (ví dụ: trong một bộ nhớ tạm hoặc một service quản lý OTP)
        // Trong thực tế, bạn nên lưu OTP và thời gian hết hạn vào một bộ nhớ tạm như Redis hoặc cơ sở dữ liệu.
        System.out.println("OTP for " + email + ": " + otp);  // Giả lập lưu OTP
    }

    // Lấy OTP đã lưu từ bộ nhớ
    public String getStoredOtp(String email) {
        // Truy xuất OTP đã lưu từ bộ nhớ (hoặc cache)
        // Trong thực tế, cần phải lấy OTP từ bộ nhớ hoặc cơ sở dữ liệu.
        return "123456";  // Trả về một OTP giả để minh họa
    }
}
