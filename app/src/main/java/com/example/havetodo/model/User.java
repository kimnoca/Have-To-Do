package com.example.havetodo.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Room;

@Entity(tableName = "User")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int userId;
    @ColumnInfo(name = "userName")
    public String userName;
    @ColumnInfo(name = "userEmail")
    public String userEmail;
    @ColumnInfo(name = "userPassword")
    public String userPassword;

//    public User(String email, String name, String password) {
//
//    }

}
