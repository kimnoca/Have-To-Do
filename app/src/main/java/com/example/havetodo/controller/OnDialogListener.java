package com.example.havetodo.controller;

import com.example.havetodo.model.TODO;

public interface OnDialogListener {
    void onFinish(TODO person, int position);
}
