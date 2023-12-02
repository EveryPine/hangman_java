package com.example.hangman_java.base;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BaseService extends Service {
    public SharedPreferences preferences;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        preferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
    }

    public float setBgmVolume(){
        int volume = preferences.getInt("bgmVolume", 10);
        boolean isMuted = preferences.getBoolean("bgmMuted", false);
        return !isMuted ? (float) (volume * 0.1) : 0f;
    }
}
