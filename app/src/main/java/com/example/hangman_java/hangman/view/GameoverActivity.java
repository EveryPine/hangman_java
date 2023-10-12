package com.example.hangman_java.hangman.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.hangman_java.databinding.ActivityGameoverBinding;
import com.example.hangman_java.base.BaseActivity;
import com.example.hangman_java.main.view.MainActivity;

public class GameoverActivity extends BaseActivity {
    private ActivityGameoverBinding binding = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameoverBinding.inflate(getLayoutInflater());

        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setContentView(binding.getRoot());
    }
    @Override
    public void initUi() throws Exception {
        setButtonListener();
    }

    private void setButtonListener(){
        binding.gotoMain.setOnClickListener(view -> {
            Intent nextIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(nextIntent);
        });

        binding.gotoGame.setOnClickListener(view -> {

            if (getIntent().hasExtra("difficulty")){
                Intent nextIntent = new Intent(getApplicationContext(), HangmanActivity.class);
                nextIntent.putExtra("difficulty", getIntent().getIntExtra("difficulty", -1));
                startActivity(nextIntent);
            } else {
                Log.e("MyTAG", "GameoverActicity에서 difficulty 변수에 잘못된 값이 참조되었습니다.");
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
        }
    }

    // 뒤로가기 기능 삭제
    @Override
    public void onBackPressed(){
        // super.onBackPressed();
    }
}
