package com.example.hangman_java.main.view;

import static com.example.hangman_java.main.viewmodel.MainViewModel.DELAY_TIME;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseActivity;
import com.example.hangman_java.databinding.ActivityMainBinding;
import com.example.hangman_java.debug.view.DebugActivity;
import com.example.hangman_java.game.view.GameActivity;
import com.example.hangman_java.main.viewmodel.MainViewModel;
import com.example.hangman_java.music.BgmService;
import com.example.hangman_java.music.SfxManager;
import com.example.hangman_java.record.view.RecordActivity;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding mainBinding = null;
    private MainViewModel mainViewModel = null;
    private final Handler handler = new Handler();
    private Animation blinkAnim;
    private SfxManager sfxManager;
    public static MainActivity mainActivity;
    public static SoundPool soundPool;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        soundPool = new SoundPool.Builder().build();
        sfxManager = new SfxManager(this, soundPool);

        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setContentView(mainBinding.getRoot());
    }
    @Override
    public void initUi() throws Exception {
        mainViewModel.setUserInfo();
        blinkAnim = AnimationUtils.loadAnimation(this, R.anim.blink_animation);
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
            sfxManager.playSound("sys_button");
            Intent intent = new Intent(this, DebugActivity.class);
            intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
        mainBinding.btnGamestart.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            view.startAnimation(blinkAnim);
            handler.postDelayed(() -> {
                Intent intent = new Intent(this, GameActivity.class);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }, DELAY_TIME);
        });

        mainBinding.btnRecord.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            view.startAnimation(blinkAnim);
            handler.postDelayed(() -> {
                Intent intent = new Intent(this, RecordActivity.class);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }, DELAY_TIME);
        });

        mainBinding.btnSetting.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            view.startAnimation(blinkAnim);
            handler.postDelayed(() -> {
                FragmentManager fm = getSupportFragmentManager();
                SettingDialog settingDialog = new SettingDialog();
                settingDialog.show(fm, "test");
            }, DELAY_TIME);
        });

        mainBinding.btnExit.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            view.startAnimation(blinkAnim);
            handler.postDelayed(() -> {
                FragmentManager fm = getSupportFragmentManager();
                CheckEndDialog chkEndDialog = new CheckEndDialog();
                chkEndDialog.show(fm, "test");
            }, DELAY_TIME);
        });
    }

    private void serviceStart(){
        Intent intent = new Intent(this, BgmService.class);
        intent.putExtra("progress", !mainViewModel.getBgmMuted() ? mainViewModel.getBgmVolume() : 0);
        startService(intent);
    }

    private void serviceStop(){
        Intent intent = new Intent(this, BgmService.class);
        stopService(intent);
    }
}