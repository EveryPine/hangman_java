package com.example.hangman_java.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hangman_java.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity{
    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        initUi();
        setContentView(binding.getRoot());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
        }
    }

    @Override
    void initUi() {
        setButton();
    }

    private void setButton(){
        binding.btnSetEasy.setOnClickListener(new BtnOnClickListener());
        binding.btnSetNormal.setOnClickListener(new BtnOnClickListener());
        binding.btnSetHard.setOnClickListener(new BtnOnClickListener());
    }

    class BtnOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            if (binding.btnSetEasy.equals(view)) {
                intent.putExtra("difficulty", 1);
            } else if (binding.btnSetNormal.equals(view)) {
                intent.putExtra("difficulty", 2);
            } else if (binding.btnSetHard.equals(view)) {
                intent.putExtra("difficulty", 3);
            }
            startActivity(intent);
        }
    }
}
