package com.example.havetodo.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.havetodo.R;
import com.example.havetodo.model.AppDatabase;
import com.example.havetodo.model.TODO;
import com.example.havetodo.model.TODODao;
import com.example.havetodo.model.UserDao;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    RecyclerView todolistView;

    ListAdapter adapter;
    ItemTouchHelper helper;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        todolistView = findViewById(R.id.todoListView);

        button = findViewById(R.id.plusBtn);
        //RecyclerView의 레이아웃 방식을 지정
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        todolistView.setLayoutManager(manager);

        //RecyclerView의 Adapter 세팅
        adapter = new ListAdapter(this);
        todolistView.setAdapter(adapter);

        //ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(todolistView);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        TODODao todoDao = db.todoDao();

        Intent intent = getIntent();
        int authorID = intent.getIntExtra("USER_ID", 1);
        String userEmail = intent.getStringExtra("USER_EMAIL");

        ArrayList<TODO> todos = new ArrayList<>(todoDao.getAllTODOByAuthorID(authorID));

        for (TODO todo : todos) {
            adapter.addItem(todo);
        }
        Log.d("TAG" , String.valueOf(authorID));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TODO newTodo = new TODO();
                CustomDialogCreate dialog = new CustomDialogCreate(MainActivity.this, -1, authorID);

                //다이얼로그 Listener 세팅
                dialog.setDialogListener(new OnDialogListener() {
                    @Override
                    public void onFinish(TODO todo, int position) {
                        if (position == -1) {
                            // 새 항목 추가
                            adapter.addItem(todo);
                            adapter.notifyItemInserted(adapter.getItemCount() - 1);
                        } else {
                            adapter.addItem(todo);
                            adapter.notifyItemChanged(position);
                        }

                    }
                });
                //다이얼로그 띄우기
                dialog.show();
            }
        });

    }

    private void setUpRecyclerView() {
        todolistView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                helper.onDraw(c, parent, state);
            }
        });
    }
}