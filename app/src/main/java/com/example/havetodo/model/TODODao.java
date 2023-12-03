package com.example.havetodo.model;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TODODao {
    @Insert
    void insert(TODO todo);

    @Delete
    void delete(TODO todo);

    @Update
    void update(TODO todo);

    @Query("SELECT * FROM TODO WHERE author_id =:authorId")
    List<TODO> getAllTODOByAuthorID(int authorId);
}
