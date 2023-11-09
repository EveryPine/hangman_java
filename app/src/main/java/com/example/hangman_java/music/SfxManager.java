package com.example.hangman_java.music;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;

public class SfxManager {
    private SoundPool mSoundPool; // 사운드풀 객체
    private HashMap<Integer, Integer> mSoundPoolMap; // 효과음 해시맵
    private AudioManager mAudioManager; // 오디오 매니저 객체
    private Context mContext; // 컨텍스트 저장용 객체

    public SfxManager(Context mContext, SoundPool mSoundPool){
        this.mContext = mContext;
        this.mSoundPool = mSoundPool;
        mSoundPoolMap = new HashMap<>();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public void addSound(int idx, int soundId){ mSoundPoolMap.put(idx, mSoundPool.load(mContext, soundId, 1)); }

    public int playSound(int idx){
        int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return mSoundPool.play(mSoundPoolMap.get(idx), streamVolume, streamVolume, 1, 0, 1f);
    }
}
