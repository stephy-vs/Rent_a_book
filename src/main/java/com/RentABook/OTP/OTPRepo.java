package com.RentABook.OTP;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepo extends JpaRepository<OTPDetails,Long> {
    Optional<OTPDetails> findByEmailAndOtp(String email, Long otp);
}
