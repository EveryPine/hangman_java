package com.example.hangman_java.music;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseService;

public class MainBgmService extends BaseService {
    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.jingle_bell_bgm);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId){
        if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            Log.d("MyTAG", "배경음악이 시작됨");
        }
        setBgmVolume();
        return super.onStartCommand(intent, flags, startId);
    }

    public float setBgmVolume(){
        float streamVolume = super.setBgmVolume();
        mediaPlayer.setVolume(streamVolume, streamVolume);
        Log.d("MyTAG", "배경음악 볼륨 조절됨 (progress: " + streamVolume + ")");
        return streamVolume;
    }

    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.stop();
        Log.d("MyTAG", "bgm 서비스가 종료됨");
    }
}
