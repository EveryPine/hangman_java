package com.example.hangman_java.main.view;

import static com.example.hangman_java.main.view.MainActivity.soundPool;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hangman_java.R;
import com.example.hangman_java.databinding.DialogCheckendBinding;
import com.example.hangman_java.base.BaseDialog;
import com.example.hangman_java.music.SfxManager;

public class CheckEndDialog extends BaseDialog {
    private DialogCheckendBinding chkEndBinding = null;
    private SfxManager sfxManager;

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ){
        chkEndBinding = DialogCheckendBinding.inflate(inflater, container, false);
        sfxManager = new SfxManager(requireContext(), soundPool);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initUi();
        return chkEndBinding.getRoot();
    }

    @Override
    public void initUi() {
        setView();
    }

    public void setView(){
        chkEndBinding.imgBtnYes.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            android.os.Process.killProcess(android.os.Process.myPid());
        });

        chkEndBinding.imgBtnNo.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            dismiss();
        });
    }

    private void exitApplication(){
        requireActivity().finish();
    }
}
