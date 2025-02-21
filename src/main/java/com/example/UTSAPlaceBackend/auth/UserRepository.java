package com.example.UTSAPlaceBackend.auth;

import com.example.UTSAPlaceBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {

}
