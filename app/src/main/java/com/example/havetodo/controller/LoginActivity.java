package com.example.havetodo.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Insert;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.havetodo.R;
import com.example.havetodo.model.AppDatabase;
import com.example.havetodo.model.UserDao;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    EditText emailInput;
    EditText passwordInput;
    Button loginButton;
    TextView singUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        emailInput = findViewById(R.id.emailEditText);
        passwordInput = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String queryPassword = userDao.getPasswordByUserEmail(email);
                if (!password.equals(queryPassword)) {
                    Toast.makeText(getApplicationContext(),"올바른 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    // login 실패
                } else {
                    int userID = userDao.getUserIDByUserEmail(email);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USER_EMAIL", email);
                    intent.putExtra("USER_ID", userID);
                    startActivity(intent);
                    // login 성공 -> 여기서 user email 이나 user(pk)를 넘겨 줘야함 그래야 개인화 가능
                }
            }
        });

        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SingUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkFieldsForEmptyValues() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        singUpButton.setEnabled(!email.isEmpty() && !password.isEmpty());
    }
}