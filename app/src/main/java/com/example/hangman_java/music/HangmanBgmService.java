package com.example.hangman_java.music;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseService;

public class HangmanBgmService extends BaseService {
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.hangman_bgm);
        mediaPlayer.setLooping(true);
        Log.d("HangmanBgmService", "서비스가 생성됨");
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId){
        if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            Log.d("HangmanBgmService", "배경음악이 시작됨");
        } else if (intent.getBooleanExtra("isPaused", false)){
            mediaPlayer.pause();
            Log.d("HangmanBgmService", "배경음악이 일시정지됨");
        }
        setBgmVolume();
        return super.onStartCommand(intent, flags, startId);
    }

    public float setBgmVolume(){
        float streamVolume = super.setBgmVolume();
        mediaPlayer.setVolume(streamVolume, streamVolume);
        Log.d("HangmanBgmService", "배경음악 볼륨 조절됨 (progress: " + streamVolume + ")");
        return streamVolume;
    }

    public void onDestroy(){
        mediaPlayer.stop();
        stopSelf();
        Log.d("HangmanBgmService", "bgm 서비스가 종료됨");
        super.onDestroy();
    }
}
