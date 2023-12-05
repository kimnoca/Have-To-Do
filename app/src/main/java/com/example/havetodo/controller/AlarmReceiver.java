package com.example.havetodo.controller;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import com.example.havetodo.R;
import com.example.havetodo.model.AppDatabase;
import com.example.havetodo.model.TODO;
import com.example.havetodo.model.TODODao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int alarmId = intent.getIntExtra("alarmId", -1);
        Log.d("TAG", String.valueOf(alarmId) + " 리시버 아이디");
        if (alarmId != -1) {
            // 비동기 작업으로 알람 비활성화 또는 삭제
            // 여기서는 Room Database 인스턴스를 얻어와야 함
            AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "database-name").allowMainThreadQueries().build();
            TODODao todoDao = db.todoDao();  // Room database instance 가져오기
            todoDao.updateAlarmActivated(alarmId, true);
            Log.d("tag", "알람 수신됨");
            showNotification(context, alarmId);
            setNextAlarm(context);
        }
    }

    private void setAlarm(Context context, Date atDate, int todoID) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("alarmId", todoID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, todoID, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long timeInMillis = atDate.getTime();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }

    // 알림 표시
    private void setNextAlarm(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "database-name").allowMainThreadQueries().build();
        TODODao todoDao = db.todoDao();  // Room database instance 가져오기
        Intent intent = new Intent(context, AlarmReceiver.class);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int storedUserId = sharedPreferences.getInt("userId", -1); // 기본값 -1
        int authorID;
        if (storedUserId != -1) {
            authorID = storedUserId;
        } else {
            authorID = intent.getIntExtra("USER_ID", -1);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userId", authorID);
            editor.apply();
        }

        List<TODO> todo = todoDao.getNextAlarm(authorID);

        if (todo != null) {
            Log.d("TAG", "여기?/");
            setAlarm(context, todo.get(0).getAtDate(), todo.get(0).getTodoID());
        }
    }

    private void showNotification(Context context, int alarmId) {
        String channelId = "alarm_notifications";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Alarm Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "database-name").allowMainThreadQueries().build();
        TODODao todoDao = db.todoDao();  // Room database instance 가져오기
        List<TODO> todo = todoDao.getTOTOByTodoID(alarmId);
        String title = todo.get(0).getTodoTitle();
        String content = todo.get(0).getTodoContent();
        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        Log.d("TAG", "진자 확인용");

        notificationManager.notify(alarmId, notification);
    }

}

