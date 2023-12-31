package com.example.havetodo.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT userPassword from User WHERE userEmail = :userEmail")
    String getPasswordByUserEmail(String userEmail);

    @Query("SELECT userName FROM User WHERE userId = :userID")
    String getUserNameByUserEmail(int userID);

    @Query("SELECT userId FROM User Where userEmail = :userEmail")
    int getUserIDByUserEmail(String userEmail);
}
