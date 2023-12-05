package com.example.hangman_java.memory.view;

import static com.example.hangman_java.main.view.MainActivity.soundPool;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.BaseDialog;
import com.example.hangman_java.databinding.DialogMemorypauseBinding;
import com.example.hangman_java.databinding.DialogPauseBinding;
import com.example.hangman_java.databinding.DialogPauseBinding;
import com.example.hangman_java.hangman.view.HangmanActivity;
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;
import com.example.hangman_java.main.view.MainActivity;
import com.example.hangman_java.memory.viewmodel.MemoryViewModel;
import com.example.hangman_java.music.SfxManager;

public class MemoryPauseDialog extends BaseDialog {

    private DialogMemorypauseBinding memoryDialogPauseBinding;
    private MemoryViewModel memoryViewModel;
    private Button btnResume, btnGoMain;
    private SfxManager sfxManager;

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ){
        memoryDialogPauseBinding = DialogMemorypauseBinding.inflate(inflater, container, false);
        sfxManager = new SfxManager(requireContext(), soundPool);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(false);
        return memoryDialogPauseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        memoryViewModel = new ViewModelProvider(requireActivity()).get(MemoryViewModel.class);
        initUi();
    }

    @Override
    public void initUi() {
        setView();
    }

    @Override
    public void setView() {
        btnResume = memoryDialogPauseBinding.btnResume;

        btnGoMain = memoryDialogPauseBinding.btnGoMain;

        btnResume.setOnClickListener(btn -> {
            sfxManager.playSound("sys_button");
            memoryViewModel.set_onDialog(true);
            dismiss();
        });
        btnGoMain.setOnClickListener(btn -> {
            sfxManager.playSound("sys_button");
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent); // 메인 화면으로 이동
            requireActivity().finish(); // 메모리 액티비티 종료
        });
    }
}
