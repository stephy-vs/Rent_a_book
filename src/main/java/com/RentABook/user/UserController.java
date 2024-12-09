package com.RentABook.user;

import com.RentABook.utilPack.ConstantData;
import com.RentABook.utilPack.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(path = "user")
public class UserController
{
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ConstantData constantData;
    @Autowired
    private ErrorService errorService;

    @PostMapping(path = "/UserReg")
    public ResponseEntity<?> register( @RequestParam("name") String name,
                                       @RequestParam("phoneNumber") String phoneNumber,
                                       @RequestParam("email") String email,
                                       @RequestParam("address") String address,
                                       @RequestParam("landMark") String landMark,
                                       @RequestParam("DOB") String DOB,
                                       @RequestParam("image")MultipartFile image,
                                       @RequestParam("aadhaarNo") String aadhaarNo,
                                       @RequestParam("aadhaarImage") MultipartFile aadhaarImage,
                                       @RequestParam("latitude")Double latitude,
                                       @RequestParam("longitude")Double longitude)
    {
        try {
            UserRegModel user=userService.registerUser(name, phoneNumber, email, address, landMark, DOB, image, aadhaarNo, aadhaarImage,latitude,longitude);
            return ResponseEntity.ok(user);
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.status(400).body(e.getMessage());

        }
        catch (IOException e)
        {
            return ResponseEntity.status(500).body("Error processing Aadhaar image");
        }
    }
    @PutMapping("/updatePayment")
    public ResponseEntity<String>updatePayment(@RequestParam("userId") Integer userId,
                                               @RequestParam("paymentId")String paymentId)
    {
        String response=userService.updatePayment(userId,paymentId);
        if (response.equals("Success"))
        {
            return ResponseEntity.ok("Payment updated successfully.Status is now true .");
        }else {
            return ResponseEntity.status(404).body(response);
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?>userLogin(@RequestParam String email){
        try {
            return userService.userLogin(email);
        }catch (Exception e){
            return errorService.handlerException(e);
        }

    }

    @PostMapping(path = "/verifyOTP")
    public ResponseEntity<?> otpVerification(@RequestParam String email,@RequestParam Long otp){
        try {
            return userService.otpVerification(email,otp);
        }catch (Exception e){
            return errorService.handlerException(e);
        }
    }
}


