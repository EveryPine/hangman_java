package com.example.hangman_java.main.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hangman_java.databinding.DialogCheckendBinding;
import com.example.hangman_java.base.BaseDialog;

public class CheckEndDialog extends BaseDialog {
    private DialogCheckendBinding chkEndBinding = null;

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ){
        chkEndBinding = DialogCheckendBinding.inflate(inflater, container, false);
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
            android.os.Process.killProcess(android.os.Process.myPid());
        });

        chkEndBinding.imgBtnNo.setOnClickListener(view -> {
            dismiss();
        });
    }

    private void exitApplication(){
        requireActivity().finish();
    }
}
