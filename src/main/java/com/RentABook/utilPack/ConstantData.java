package com.RentABook.utilPack;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ConstantData {
    public static final String Something_Went_Wrong="Something went wrong";
    public static final String Invalid_Data = "Invalid Data";
    public static final String Data_Not_Found="Data Not Found";


    public Long generateRandomNumber(){
        Random random = new Random();
        Long randomNumber = 100000 + random.nextLong(900000);
        return randomNumber;
    }
}
