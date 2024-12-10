package com.RentABook.user;

import com.RentABook.OTP.OTPDetails;
import com.RentABook.OTP.OTPRepo;
import com.RentABook.utilPack.ConstantData;
import com.RentABook.utilPack.EmailServices;
import com.RentABook.utilPack.ErrorService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.IWeavingSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ErrorService errorService;
    @Autowired
    private EmailServices emailServices;
    @Autowired
    private ConstantData constantData;
    @Autowired
    private OTPRepo otpRepo;

    public UserRegModel registerUser(String name, String phoneNumber, String email, String address, String landMark, String DOB,MultipartFile image, String aadhaarNo, MultipartFile aadhaarImage,Double latitude,Double longitude)
            throws IOException {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number already  exists.Please use a different phone number .");

        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists.Please use a different email address .");
        }
        UserRegModel user = new UserRegModel();
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setAddress(address);
        user.setLandMark(landMark);
        user.setDOB(DOB);
        try {
            user.setImage(image.getBytes());
        }catch (IOException e)
        {
            throw new RuntimeException("Failed to store Image",e);
        }
        user.setAadhaarNo(aadhaarNo);
        try {
            user.setAadhaarImage(aadhaarImage.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store Aadhaar image", e);
        }
        user.setLatitude(latitude);
        user.setLongitude(longitude);
        user.setStatus(false);
        user.setPaymentId(null);
        user.setCreatedTime(LocalDateTime.now());
      return userRepository.save(user);

    }
    public String updatePayment(Integer userId,String paymentId)
    {
        UserRegModel userRegModel=userRepository.findById(userId).orElse(null);
        if (userRegModel!=null)
        {
            userRegModel.setPaymentId(paymentId);
            userRegModel.setStatus(true);
            userRepository.save(userRegModel);
            return "Success";
        }
        return "User not found or update failed .";
    }

    public ResponseEntity<?> userLogin(String email) {
        try {
            Optional<UserRegModel> userRegModelOptional = userRepository.findByEmail(email);
            if (userRegModelOptional.isEmpty()){
                return new ResponseEntity<>(ConstantData.Data_Not_Found, HttpStatus.NOT_FOUND);
            }else {
                UserRegModel userRegModel = userRegModelOptional.get();
                String userEmailId = userRegModel.getEmail();
                String subject = "Login One Time Password ";
                Long otp = constantData.generateRandomNumber();
                String body = otp+" This OTP is valid for only next 10 minutes.";
                OTPDetails otpDetails = new OTPDetails();
                otpDetails.setOtp(otp);
                otpDetails.setGeneratedTime(LocalDateTime.now());
                otpDetails.setEmail(userEmailId);
                otpRepo.save(otpDetails);
                emailServices.sendEmail(userEmailId,subject,body);
                return new ResponseEntity<>("Verification code is send to the registered mail Id. email : "+userEmailId,HttpStatus.OK);
            }
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }

    public ResponseEntity<?> otpVerification(String email, Long otp) {
        Optional<OTPDetails> otpDetailsOptional = otpRepo.findByEmail(email);
        if (otpDetailsOptional.isPresent()){
            OTPDetails otpDetails = otpDetailsOptional.get();
            LocalDateTime otpGenTime = otpDetails.getGeneratedTime();
            LocalDateTime currentTime = LocalDateTime.now();

            Duration duration = Duration.between(otpGenTime,currentTime);
            long minutes = duration.toMinutes();
            if (minutes<=10){
                if (otpDetails.getOtp().equals(otp)){
                    otpDetails.setVerified(true);
                    otpRepo.save(otpDetails);
                    return new ResponseEntity<>("OTP verified successfully",HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(ConstantData.Invalid_Data+" OTP",HttpStatus.BAD_REQUEST);
                }
            }else {
                otpDetails.setVerified(false);
                otpRepo.save(otpDetails);
                return new ResponseEntity<>(ConstantData.Invalid_Data+" : OTP expired",HttpStatus.NOT_FOUND);
            }
        }else {
            return new ResponseEntity<>(ConstantData.Invalid_Data+" : emailId",HttpStatus.BAD_REQUEST);
        }
    }
}