package com.example.hangman_java.main.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hangman_java.databinding.DialogSettingBinding;
import com.example.hangman_java.base.BaseDialog;

public class SettingDialog extends BaseDialog {
    private DialogSettingBinding settingBinding = null;

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
    public void initUi() {
        setView();
    }

    @Override
    public void setView(){
        // 음량 조절관련 뷰 세팅 요망
        settingBinding.btnComplete.setOnClickListener(view -> {
            dismiss();
        });
    }

    private void exitApplication(){
        requireActivity().finish();
    }

}
