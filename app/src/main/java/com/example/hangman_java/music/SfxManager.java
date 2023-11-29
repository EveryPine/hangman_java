package com.example.hangman_java.music;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.hangman_java.R;
import com.example.hangman_java.main.viewmodel.MainViewModel;

import java.util.HashMap;

public class SfxManager {
    private SoundPool mSoundPool; // 사운드풀 객체
    private HashMap<String, Integer> mSoundPoolMap; // 효과음 해시맵
    private AudioManager mAudioManager; // 오디오 매니저 객체
    private Context mContext; // 컨텍스트 저장용 객체
    private SharedPreferences preferences; // 효과음 음량 설정값 가져오는 용도

    public SfxManager(@NonNull Context mContext, SoundPool mSoundPool){
        this.mContext = mContext;
        this.mSoundPool = mSoundPool;
        mSoundPoolMap = new HashMap<>();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        preferences = mContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        addDefaultSound();
    }

    public void addSound(String soundName, int soundId){ mSoundPoolMap.put(soundName, mSoundPool.load(mContext, soundId, 1)); }

    public void playSound(String soundName){
        int volume = preferences.getInt("eftVolume", 10);
        boolean isMuted = preferences.getBoolean("eftMuted", false);
        float streamVolume = !isMuted ? (float) (volume * 0.1) : 0f;
        mSoundPool.play(mSoundPoolMap.get(soundName), streamVolume, streamVolume, 1, 0, 1f);
    }

    // 기본적으로 사용할 효과음을 미리 넣어둠
    private void addDefaultSound(){
        this.addSound("sys_button", R.raw.button_click);
    }
}
