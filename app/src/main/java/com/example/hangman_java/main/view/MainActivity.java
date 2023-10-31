package com.example.hangman_java.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.BaseActivity;
import com.example.hangman_java.databinding.ActivityMainBinding;
import com.example.hangman_java.debug.view.DebugActivity;
import com.example.hangman_java.game.view.GameActivity;
import com.example.hangman_java.main.viewmodel.MainViewModel;
import com.example.hangman_java.music.BgmService;
import com.example.hangman_java.record.view.RecordActivity;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding mainBinding = null;
    public static MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setContentView(mainBinding.getRoot());
    }
    @Override
    public void initUi() throws Exception {
        serviceStart();
        setButton();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceStop();
    }

    private void setButton(){
        mainBinding.btnDebug.setOnClickListener(view -> {
            Intent intent = new Intent(this, DebugActivity.class);
            startActivity(intent);
        });
        mainBinding.imgBtnGamestart.setOnClickListener(view -> {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });

        mainBinding.imgBtnRecord.setOnClickListener(view -> {
            Intent intent = new Intent(this, RecordActivity.class);
            startActivity(intent);
        });

        mainBinding.imgBtnSetting.setOnClickListener(view -> {
            FragmentManager fm = getSupportFragmentManager();
            SettingDialog settingDialog = new SettingDialog();
            settingDialog.show(fm, "test");
        });

        mainBinding.imgBtnExit.setOnClickListener(view -> {
            FragmentManager fm = getSupportFragmentManager();
            CheckEndDialog chkEndDialog = new CheckEndDialog();
            chkEndDialog.show(fm, "test");
        });
    }

    private void serviceStart(){
        Intent intent = new Intent(this, BgmService.class);
        intent.putExtra("progress", 10);
        startService(intent);
    }

    private void serviceStop(){
        Intent intent = new Intent(this, BgmService.class);
        stopService(intent);
    }
}