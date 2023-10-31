package com.example.hangman_java.main.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseDialog;
import com.example.hangman_java.databinding.DialogSettingBinding;
import com.example.hangman_java.game.viewmodel.GameViewModel;
import com.example.hangman_java.main.viewmodel.MainViewModel;

public class SettingDialog extends BaseDialog {
    private DialogSettingBinding settingBinding = null;
    private MainViewModel mainViewModel = null;
    private SeekBar sbBgmVolume, sbEftVolume;
    private CheckBox chkBgmMuted, chkEftVolume;

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ){
        settingBinding = DialogSettingBinding.inflate(inflater, container, false);
        initUi();
        return settingBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        sbBgmVolume.setProgress(mainViewModel.getBackgroundVolume());
        sbEftVolume.setProgress(mainViewModel.getEffectVolume());
        Log.d("MyTAG", "isBgmMuted? : "+ mainViewModel.isBgmMuted());
        chkBgmMuted.setChecked(mainViewModel.isBgmMuted());
        chkBgmMuted.setChecked(mainViewModel.isEftMuted());
    }

    @Override
    public void initUi() {
        sbBgmVolume = settingBinding.sbBackgroundVolume;
        sbEftVolume = settingBinding.sbEffectVolume;
        chkBgmMuted = settingBinding.chkIsBackgroundMuted;
        chkEftVolume = settingBinding.chkIsEffectMuted;
        setView();
    }

    @Override
    public void setView(){
        // 음량 조절관련 뷰 세팅
        sbBgmVolume.setOnSeekBarChangeListener(new SeekBarListener());
        sbEftVolume.setOnSeekBarChangeListener(new SeekBarListener());
        chkBgmMuted.setOnCheckedChangeListener(new IsMutedListener());
        chkEftVolume.setOnCheckedChangeListener(new IsMutedListener());
        settingBinding.btnComplete.setOnClickListener(view -> dismiss());
    }

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener{
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.equals(sbBgmVolume)){
                mainViewModel.setBackgroundVolume(requireContext(), progress);
            }
            else {
                Log.d("MyTAG", "효과음 변경됨 (progress: " + progress + ")");
                mainViewModel.setEffectVolume(progress);
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    private class IsMutedListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
            // isChecked >> true: 음소거됨, false: 음소거 되지 않음
            // 음소거 아닌 상태
            if (!isChecked) {
                if (checkBox.equals(chkBgmMuted)) {
                    mainViewModel.setBackgroundVolume(requireContext(), mainViewModel.getBackgroundVolume());
                    sbBgmVolume.setEnabled(true);
                }
                else {
                    mainViewModel.setEffectVolume(mainViewModel.getBackgroundVolume());
                    sbEftVolume.setEnabled(true);
                }
                // 음소거 상태
            } else {
                if (checkBox.equals(chkBgmMuted)) {
                    mainViewModel.setBgmVolumeMuted(requireContext());
                    sbBgmVolume.setEnabled(false);
                }
                else {
                    mainViewModel.setEffectVolumeMuted(requireContext());
                    sbEftVolume.setEnabled(false);
                }
            }
        }
    }
}
