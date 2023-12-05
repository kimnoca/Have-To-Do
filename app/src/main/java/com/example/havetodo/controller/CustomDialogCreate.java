package com.example.havetodo.controller;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.room.Room;

import com.example.havetodo.R;
import com.example.havetodo.model.AppDatabase;
import com.example.havetodo.model.TODO;

import java.util.Calendar;
import java.util.Date;

public class CustomDialogCreate extends Dialog {

    String title;
    OnDialogListener listener;
    String content;
    Date atDate;

    EditText editTitle;
    EditText editContent;
    TimePicker timePicker;

    Button saveBtn;


    public CustomDialogCreate(Context context, final int position, int authorID) {
        super(context);
        setContentView(R.layout.activity_custom_dialog);


        editTitle = findViewById(R.id.inputTitle);
        editContent = findViewById(R.id.inputContent);
        timePicker = findViewById(R.id.timePicker);
        saveBtn = findViewById(R.id.saveButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();

                int hour, minute, second;

                hour = timePicker.getHour();
                minute = timePicker.getMinute();
                second = 0;

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, second);

                TODO updatedTodo = new TODO();// Assuming ID is set elsewhere
                updatedTodo.todoTitle = editTitle.getText().toString();
                updatedTodo.todoContent = editContent.getText().toString();
                updatedTodo.atDate = calendar.getTime();
                updatedTodo.authorID = authorID;

                // Save to database
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = Room.databaseBuilder(getContext(),
                                AppDatabase.class, "database-name").build();
                        db.todoDao().insert(updatedTodo);
                    }
                }).start();

                // Notify the listener if necessary
                if (listener != null) {
                    listener.onFinish(updatedTodo, position);
                }

                dismiss();
            }
        });


    }

    public void setDialogListener(OnDialogListener listener) {
        this.listener = listener;
    }
}