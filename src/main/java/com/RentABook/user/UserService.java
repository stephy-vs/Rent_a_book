package com.RentABook.user;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.IWeavingSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

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

}