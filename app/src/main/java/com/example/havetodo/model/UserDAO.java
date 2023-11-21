package com.example.havetodo.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDAO {
    @Insert
    void insert(User user);

    @Query("SELECT userPassword from User WHERE userEmail = :userEmail")
    void getPasswordByUserEmail(String userEmail);


}
