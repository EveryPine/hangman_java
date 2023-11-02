package com.example.hangman_java.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.hangman_java.R;

public class BgmService extends Service {
    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.test_bgm);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            Log.d("MyTAG", "배경음악이 시작됨");
        }
        this.setBackgroundVolume(intent.getIntExtra("progress", 10));
        return super.onStartCommand(intent, flags, startId);
    }

    public void setBackgroundVolume(int progress){
        float output = progress / 10.0f;
        mediaPlayer.setVolume(output, output);
        Log.d("MyTAG", "배경음악 볼륨 조절됨 (progress: " + progress + ")");
    }

    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.stop();
    }
}
