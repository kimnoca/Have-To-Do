package com.example.havetodo.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.havetodo.R;
import com.example.havetodo.model.AppDatabase;
import com.example.havetodo.model.User;
import com.example.havetodo.model.UserDao;

public class SingUpActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText nameInput;
    private EditText passwordInput;
    private EditText passwordCheckInput;
    private Button singUpButton;

    private AppDatabase appDB = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sing_up_activity);
        // init widget
        emailInput = findViewById(R.id.emailEditText);
        nameInput = findViewById(R.id.userNameEditText);
        passwordInput = findViewById(R.id.passwordEditText);
        passwordCheckInput = findViewById(R.id.passwordCheckEditText);
        singUpButton = findViewById(R.id.singUpButton);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        UserDao userDao = db.userDao();

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFieldsForEmptyValues();
            }
        });

        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFieldsForEmptyValues();
            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFieldsForEmptyValues();
            }
        });

        passwordCheckInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkFieldsForEmptyValues();
            }
        });

        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String name = nameInput.getText().toString();
                String password = passwordInput.getText().toString();
                User user = new User();
                user.userEmail = email;
                user.userName = name;
                user.userPassword = password;
                userDao.insert(user);

                Intent intent = new Intent(SingUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void checkFieldsForEmptyValues() {
        String email = emailInput.getText().toString();
        String name = nameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String passwordCheck = passwordCheckInput.getText().toString();

        // 모든 필드가 비어 있지 않으면 버튼을 활성화합니다.
        singUpButton.setEnabled(!email.isEmpty() && !name.isEmpty() &&
                !password.isEmpty() && !passwordCheck.isEmpty());
    }
}