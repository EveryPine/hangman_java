package com.example.hangman_java.main.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hangman_java.base.BaseViewModel;
import com.example.hangman_java.music.BgmService;

public class MainViewModel extends BaseViewModel {
    private final MutableLiveData<Integer> _effectVolume = new MutableLiveData<>(10);
    private final MutableLiveData<Integer> _backgroundVolume = new MutableLiveData<>(10);
    private final MutableLiveData<Boolean> _bgmMuted = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> _eftMuted = new MutableLiveData<>(false);

    public void setBackgroundVolume(Context context, int progress){
        Intent intent = new Intent(context, BgmService.class);
        intent.putExtra("progress", progress);
        context.startService(intent);
        _bgmMuted.setValue(false);
        _backgroundVolume.setValue(progress);
    }

    public void setBgmVolumeMuted(Context context){
        Intent intent = new Intent(context, BgmService.class);
        intent.putExtra("progress", 0);
        context.startService(intent);
        Log.d("MyTAG", "bgmMuted 변경전 : " + _bgmMuted.getValue());
        _bgmMuted.setValue(true);
        Log.d("MyTAG", "bgmMuted 변경됨 : " + _bgmMuted.getValue());
    }

    public void setEffectVolume(int progress){
        _eftMuted.setValue(false);
        _effectVolume.setValue(progress);
    }
    public void setEffectVolumeMuted(Context context){
        _eftMuted.setValue(true);
        Intent intent = new Intent(context, BgmService.class);
        intent.putExtra("progress", 0);
        context.startService(intent);
    }

    public int getBackgroundVolume(){
        return _backgroundVolume.getValue();
    }

    public int getEffectVolume(){
        return _effectVolume.getValue();
    }

    public boolean isBgmMuted() { return _bgmMuted.getValue(); }
    public boolean isEftMuted() { return _eftMuted.getValue(); }
}
