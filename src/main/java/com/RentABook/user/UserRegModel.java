package com.RentABook.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_Reg")
public class UserRegModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private byte[] image;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;


    @Column(name = "landMark")
    private String landMark;

    @Column(name = "DOB")
    private String DOB;

    @Column(name = "aadhaarNo")
    private String aadhaarNo;

    @Column(name = "aadhaarImage",columnDefinition = "bytea")
    private byte[] aadhaarImage;

    @Column(name = "paymentId")
    private String paymentId;

    @Column(name = "Status")
    private boolean Status;

}
