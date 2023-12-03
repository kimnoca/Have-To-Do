package com.example.havetodo.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, TODO.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract TODODao todoDao();

}