package com.example.hangman_java.hangman.view;

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
import com.example.hangman_java.databinding.DialogPauseBinding;
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;
import com.example.hangman_java.main.view.MainActivity;
import com.example.hangman_java.music.SfxManager;

public class PauseDialog extends BaseDialog {
    private DialogPauseBinding pauseBinding;
    private HangmanViewModel hangmanViewModel;
    private Button btnResume, btnRestart, btnGoMain;
    private SfxManager sfxManager;

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ){
        pauseBinding = DialogPauseBinding.inflate(inflater, container, false);
        sfxManager = new SfxManager(requireContext(), soundPool);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return pauseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        hangmanViewModel = new ViewModelProvider(requireActivity()).get(HangmanViewModel.class);
        initUi();
    }

    @Override
    public void initUi() {
        setView();
    }

    @Override
    public void setView() {
        btnResume = pauseBinding.btnResume;
        btnRestart = pauseBinding.btnRestart;
        btnGoMain = pauseBinding.btnGoMain;

        btnResume.setOnClickListener(btn -> {
            sfxManager.playSound("sys_button");
            hangmanViewModel.restartTimer();
            dismiss();
        });
        btnRestart.setOnClickListener(btn -> {
            sfxManager.playSound("sys_button");
            Intent intent = new Intent(requireActivity(), HangmanActivity.class);
            try {
                intent.putExtra("difficulty", hangmanViewModel.getIntDifficulty());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            startActivity(intent);
            requireActivity().finish(); // 행맨 액티비티 종료
        });
        btnGoMain.setOnClickListener(btn -> {
            sfxManager.playSound("sys_button");
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent); // 메인 화면으로 이동
            requireActivity().finish(); // 행맨 액티비티 종료
        });
    }
}
