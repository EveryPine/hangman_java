package com.example.hangman_java.game.view;

import static com.example.hangman_java.hangman.view.HangmanActivity.hangmanActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hangman_java.base.BaseActivity;
import com.example.hangman_java.databinding.ActivityResultBinding;
import com.example.hangman_java.hangman.view.HangmanActivity;
import com.example.hangman_java.main.view.MainActivity;

public class ResultActivity extends BaseActivity {
    private ActivityResultBinding resultBinding;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        resultBinding = ActivityResultBinding.inflate(getLayoutInflater());
        initUi();
        setContentView(resultBinding.getRoot());
    }

    @Override
    public void initUi() {
        setView();
    }

    public void setView() {
        resultBinding.btnGoMain.setOnClickListener(btn -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent); // 메인 화면으로 이동
            hangmanActivity.finish(); // 행맨 액티비티 종료
        });

        resultBinding.btnRestart.setOnClickListener(btn -> {
            Intent intent = new Intent(this, HangmanActivity.class);
            hangmanActivity.finish();
            startActivity(intent);
        });
    }
}
