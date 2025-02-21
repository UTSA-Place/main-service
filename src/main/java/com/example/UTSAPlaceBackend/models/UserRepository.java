package com.example.UTSAPlaceBackend.models;

import org.springframework.stereotype.Repository;


@Repository
public class UserRepository {

    public User getUserByUsername(final String username) {
        // TODO: Get user by username(email) from database
        return new User(null, null, null, null, null);
    }

    public User createUser(User user) {
        // TODO: Create the user in the database
        return new User(null, null, null, null, null);
    }
}
