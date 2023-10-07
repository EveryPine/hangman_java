package com.example.hangman_java.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hangman_java.databinding.DialogCheckendBinding;
import com.example.hangman_java.ui.base.BaseDialog;

public class CheckEndDialog extends BaseDialog {
    private DialogCheckendBinding chkEndBinding = null;

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ){
        chkEndBinding = DialogCheckendBinding.inflate(inflater, container, false);
        initUi();
        return chkEndBinding.getRoot();
    }

    @Override
    public void initUi() {
        setButton();
    }

    private void setButton(){
        chkEndBinding.imgBtnYes.setOnClickListener(view -> {
            exitApplication();
        });

        chkEndBinding.imgBtnNo.setOnClickListener(view -> {
            dismiss();
        });
    }

    private void exitApplication(){
        requireActivity().finish();
    }
}
