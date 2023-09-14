package com.example.hangman_java.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.hangman_java.databinding.ActivityGameoverBinding;

public class GameoverActivity extends BaseActivity {
    private ActivityGameoverBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    void initUi() throws Exception {
        setButtonListener();
    }

    private void setButtonListener(){
        binding.gotoMain.setOnClickListener(view -> {
            Intent nextIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(nextIntent);
        });

        binding.gotoGame.setOnClickListener(view -> {

            if (getIntent().hasExtra("difficulty")){
                Intent nextIntent = new Intent(getApplicationContext(), GameActivity.class);
                nextIntent.putExtra("difficulty", getIntent().getIntExtra("difficulty", -1));
                startActivity(nextIntent);
            } else {
                Log.e("MyTAG", "GameoverActicity에서 difficulty 변수에 잘못된 값이 참조되었습니다.");
            }
        });
    }

    // 뒤로가기 기능 삭제
    @Override
    public void onBackPressed(){
        // super.onBackPressed();
    }
}
