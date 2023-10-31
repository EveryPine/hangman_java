package com.example.hangman_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class selectLevel extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onButtonClicked(View view){
        int level;
        if (view.getId() == R.id.easy) {
            level = 3;
        } else if (view.getId() == R.id.normal) {
            level = 4;
        } else {
            level = 5;
        }
        Intent intent = new Intent(this, gameView.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }
}