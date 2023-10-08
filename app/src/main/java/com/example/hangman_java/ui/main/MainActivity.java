package com.example.hangman_java.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.example.hangman_java.databinding.ActivityMainBinding;
import com.example.hangman_java.ui.base.BaseActivity;
import com.example.hangman_java.ui.game.SetGameActivity;
import com.example.hangman_java.ui.record.RecordActivity;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding mainBinding = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setButton();
    }

    private void setButton(){
        mainBinding.imgBtnGamestart.setOnClickListener(view -> {
            // Intent intent = new Intent(getApplicationContext(), SetGameActivity.class);
            // startActivity(intent);
        });

        mainBinding.imgBtnRecord.setOnClickListener(view -> {
            // Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
            // startActivity(intent);
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
}