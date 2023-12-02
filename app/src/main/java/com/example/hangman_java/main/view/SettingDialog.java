package com.example.hangman_java.main.view;

import static com.example.hangman_java.main.view.MainActivity.soundPool;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseDialog;
import com.example.hangman_java.base.Event;
import com.example.hangman_java.base.EventObserver;
import com.example.hangman_java.databinding.DialogSettingBinding;
import com.example.hangman_java.game.viewmodel.GameViewModel;
import com.example.hangman_java.main.viewmodel.MainViewModel;
import com.example.hangman_java.music.SfxManager;

public class SettingDialog extends BaseDialog {
    private DialogSettingBinding settingBinding = null;
    private MainActivity parentActivity;
    private MainViewModel mainViewModel = null;
    private SeekBar sbBgmVolume, sbEftVolume;
    private CheckBox chkBgmMuted, chkEftMuted;
    private SfxManager sfxManager;

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ){
        settingBinding = DialogSettingBinding.inflate(inflater, container, false);
        parentActivity = (MainActivity) requireActivity();
        sfxManager = new SfxManager(parentActivity, soundPool);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initUi();
        return settingBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(parentActivity).get(MainViewModel.class);
        mainViewModel.setUserInfo();
        chkBgmMuted.setChecked(mainViewModel.getBgmMuted());
        chkEftMuted.setChecked(mainViewModel.getEftMuted());
        sbBgmVolume.setProgress(mainViewModel.getBgmVolume());
        sbEftVolume.setProgress(mainViewModel.getEftVolume());
    }

    @Override
    public void initUi() {
        sbBgmVolume = settingBinding.sbBackgroundVolume;
        sbEftVolume = settingBinding.sbEffectVolume;
        chkBgmMuted = settingBinding.chkIsBackgroundMuted;
        chkEftMuted = settingBinding.chkIsEffectMuted;
        setView();
    }

    @Override
    public void setView(){
        // 음량 조절관련 뷰 세팅
        sbBgmVolume.setOnSeekBarChangeListener(new SeekBarListener());
        sbEftVolume.setOnSeekBarChangeListener(new SeekBarListener());
        chkBgmMuted.setOnCheckedChangeListener(new IsMutedListener());
        chkEftMuted.setOnCheckedChangeListener(new IsMutedListener());
        settingBinding.btnComplete.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            dismiss();
        });
    }

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener{
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.equals(sbBgmVolume) && !mainViewModel.getBgmMuted()){
                Log.d("MyTAG", "배경음 변경됨 (progress: " + progress + ")");
                mainViewModel.setBackgroundVolume(parentActivity, progress);
            }
            else if (!mainViewModel.getEftMuted()) {
                Log.d("MyTAG", "효과음 변경됨 (progress: " + progress + ")");
                mainViewModel.setEffectVolume(progress);
            }
            mainViewModel.updateUserInfo();
            parentActivity.serviceStart();
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { sfxManager.playSound("sys_button");}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    private class IsMutedListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
            sfxManager.playSound("sys_button");
            // isChecked >> true: 음소거됨, false: 음소거 되지 않음
            // 음소거 아닌 상태
            if (!isChecked) {
                if (checkBox.equals(chkBgmMuted)) {
                    Log.d("MyTAG", "배경음 볼륨 조절 실행.. (볼륨: " + mainViewModel.getBgmVolume() + ")");
                    mainViewModel.setBackgroundVolume(parentActivity, mainViewModel.getBgmVolume());
                    sbBgmVolume.setEnabled(true);
                }
                else {
                    Log.d("MyTAG", "효과음 볼륨 조절 실행.. (볼륨: " + mainViewModel.getEftVolume() + ")");
                    mainViewModel.setEffectVolume(mainViewModel.getBgmVolume());
                    sbEftVolume.setEnabled(true);
                }
                // 음소거 상태
            } else {
                if (checkBox.equals(chkBgmMuted)) {
                    Log.d("MyTAG", "배경음 음소거 실행..");
                    mainViewModel.setBgmVolumeMuted(parentActivity);
                    sbBgmVolume.setEnabled(false);
                }
                else {
                    Log.d("MyTAG", "효과음 음소거 실행..");
                    mainViewModel.setEffectVolumeMuted();
                    sbEftVolume.setEnabled(false);
                }
            }
            mainViewModel.updateUserInfo();
            parentActivity.serviceStart();
        }
    }
}
