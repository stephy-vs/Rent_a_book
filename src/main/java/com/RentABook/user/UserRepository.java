package com.RentABook.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserRegModel,Integer>
{

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
}
