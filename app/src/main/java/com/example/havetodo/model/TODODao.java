package com.example.havetodo.model;


import androidx.lifecycle.LiveData;
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

    @Query("SELECT * FROM TODO WHERE todoID= :todoID")
    List<TODO> getTOTOByTodoID(int todoID);

    @Query("UPDATE TODO SET is_activated = :isActivated WHERE todoID = :alarmId")
    void updateAlarmActivated(int alarmId, boolean isActivated);
    // 시간의 조건을 지금 시간이랑 같으면 ~ 하면 계속 나오지 않을까?
    @Query("SELECT * FROM TODO WHERE is_activated = 0 and  author_id =:authorId ORDER BY at_date ASC LIMIT 1")
    List<TODO> getNextAlarm(int authorId);
}
