package com.example.hangman_java.main.viewmodel;

import static com.example.hangman_java.main.view.MainActivity.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hangman_java.base.AppDatabase;
import com.example.hangman_java.base.BaseViewModel;
import com.example.hangman_java.music.BgmService;

public class MainViewModel extends BaseViewModel {
    private SharedPreferences preferences;
    private final MutableLiveData<Integer> _bgmVolume = new MutableLiveData<>();
    private final MutableLiveData<Integer> _eftVolume = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _bgmMuted = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _eftMuted = new MutableLiveData<>();
    private void getSharedPreferences(){ if (preferences==null) preferences = mainActivity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE); }

    public void setUserInfo(){
        getSharedPreferences();
        Log.d("MyTAG", "setUserInfo: " + preferences.getBoolean("bgmMuted", false) + " " + preferences.getBoolean("eftMuted", false));
        _bgmVolume.setValue(preferences.getInt("bgmVolume", 10));
        _eftVolume.setValue(preferences.getInt("eftVolume", 10));
        _bgmMuted.setValue(preferences.getBoolean("bgmMuted", false));
        _eftMuted.setValue(preferences.getBoolean("eftMuted", false));
        Log.d("MyTAG", "_bgmMuted: " + getBgmMuted());
    }

    public void setBackgroundVolume(Context context, int progress){
        Intent intent = new Intent(context, BgmService.class);
        intent.putExtra("progress", progress);
        context.startService(intent);
        _bgmMuted.setValue(false);
        _bgmVolume.setValue(progress);
    }

    public void setBgmVolumeMuted(Context context){
        Intent intent = new Intent(context, BgmService.class);
        intent.putExtra("progress", 0);
        context.startService(intent);
        _bgmMuted.setValue(true);
    }

    public void setEffectVolume(int progress){
        _eftMuted.setValue(false);
        _eftVolume.setValue(progress);
    }
    public void setEffectVolumeMuted(){
        _eftMuted.setValue(true);
    }

    public int getBgmVolume(){ return _bgmVolume.getValue(); }

    public int getEftVolume(){ return _eftVolume.getValue(); }

    public boolean getBgmMuted() { return _bgmMuted.getValue(); }

    public boolean getEftMuted() { return _eftMuted.getValue(); }

    public void updateUserInfo(){
        getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("bgmVolume", getBgmVolume());
        editor.putInt("eftVolume", getEftVolume());
        editor.putBoolean("bgmMuted", getBgmMuted());
        editor.putBoolean("eftMuted", getEftMuted());
        editor.apply();
        Log.d("MyTAG", "updateUserInfo: " + preferences.getBoolean("bgmMuted", false) + " " + preferences.getBoolean("eftMuted", false));
    }
}
