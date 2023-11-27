package com.example.hangman_java.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public abstract class BaseDialog extends DialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    public abstract void initUi();

    public abstract void setView();
}
