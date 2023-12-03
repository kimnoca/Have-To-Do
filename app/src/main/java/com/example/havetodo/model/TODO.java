package com.example.havetodo.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "TODO", foreignKeys = @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "author_id"))
@TypeConverters({Converters.class})
public class TODO {

    public TODO(){}
    public TODO(String todoTitle, String todoContent, Date atDate, int authorID) {
        this.todoTitle = todoTitle;
        this.todoContent = todoContent;
        this.atDate = atDate;
        this.authorID = authorID;
    }

    public int getTodoID() {
        return todoID;
    }
    public String getTodoTitle() {
        return todoTitle;
    }

    public String getTodoContent() {
        return todoContent;
    }

    public Date getAtDate() {
        return atDate;
    }

    public int getAuthorID() {
        return authorID;
    }

    @PrimaryKey(autoGenerate = true)
    public int todoID;
    @ColumnInfo(name = "todoTitle")
    public String todoTitle;
    @ColumnInfo(name = "todoContent")
    public String todoContent;
    @ColumnInfo(name = "at_date")
    public Date atDate;
    @ColumnInfo(name = "author_id")
    public int authorID;

}
