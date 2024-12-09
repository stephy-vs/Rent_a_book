package com.RentABook.OTP;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
@Table(name = "otpDetails")
public class OTPDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "otp")
    private Long otp;

    @Column(name = "generatedTime")
    private LocalTime generatedTime;

    @Column(name = "isVerified")
    private boolean isVerified;
}
