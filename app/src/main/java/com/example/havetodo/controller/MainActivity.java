package com.example.havetodo.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.havetodo.R;
import com.example.havetodo.model.AppDatabase;
import com.example.havetodo.model.TODO;
import com.example.havetodo.model.TODODao;
import com.example.havetodo.model.UserDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView todolistView;
    ListAdapter adapter;
    ItemTouchHelper helper;
    Button button;
    TextView nickNameView;
    Button logoutButton;

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

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int storedUserId = sharedPreferences.getInt("userId", -1); // 기본값 -1
        int authorID;
        String userEmail = intent.getStringExtra("USER_EMAIL");
        UserDao userDao = db.userDao();


        Log.d("TAG", "저장된 userID" + storedUserId);
        if (storedUserId != -1) {
            authorID = storedUserId;
        } else {
            authorID = intent.getIntExtra("USER_ID", 1);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userId", authorID);
            editor.apply();
        }
        List<TODO> fetchedTodos = todoDao.getAllTODOByAuthorID(authorID);
        ArrayList<TODO> todos = fetchedTodos != null ? new ArrayList<>(fetchedTodos) : new ArrayList<>();
        if (!todos.isEmpty()) {
            for (TODO todo : todos) {
                adapter.addItem(todo);
            }
        }
        String userNickName = userDao.getUserNameByUserEmail(authorID);

        nickNameView = findViewById(R.id.userNameView);

        nickNameView.setText(userNickName);

        Log.d("TAG", String.valueOf(authorID));
        scheduleNextAlarm();
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
                        scheduleNextAlarm();

                    }
                });
                //다이얼로그 띄우기
                dialog.show();
            }
        });
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply(); // 또는 commit()을 사용
                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }

    private void scheduleNextAlarm() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        TODODao todoDao = db.todoDao();  // Room database instance 가져오기
        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int storedUserId = sharedPreferences.getInt("userId", -1); // 기본값 -1
        int authorID;
        if (storedUserId != -1) {
            authorID = storedUserId;
        } else {
            authorID = intent.getIntExtra("USER_ID", 1);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userId", authorID);
            editor.apply();
        }

        List<TODO> todo = todoDao.getNextAlarm(authorID);

        if (todo != null && !todo.isEmpty()) {
            Log.d("TAG", "여기?/");
            setAlarm(todo.get(0).getAtDate(), todo.get(0).getTodoID());
        }
    }

    private void setAlarm(Date atDate, int todoID) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("alarmId", todoID);
        Intent intent1 = getIntent();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int storedUserId = sharedPreferences.getInt("userId", -1); // 기본값 -1
        int authorID;
        if (storedUserId != -1) {
            authorID = storedUserId;
        } else {
            authorID = intent1.getIntExtra("USER_ID", -1);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userId", authorID);
            editor.apply();
        }
        intent.putExtra("USER_ID", authorID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, todoID, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        long timeInMillis = atDate.getTime();
        Log.d("tag", "alarmID == " + todoID);
        Log.d("tag", String.valueOf(timeInMillis));
        // SDK 버전에 따른 조건 처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
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