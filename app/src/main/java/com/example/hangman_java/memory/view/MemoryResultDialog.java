package com.example.hangman_java.memory.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.BaseDialog;
import com.example.hangman_java.databinding.DialogResultBinding;
import com.example.hangman_java.hangman.view.HangmanActivity;
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;
import com.example.hangman_java.main.view.MainActivity;
import com.example.hangman_java.memory.viewmodel.MemoryViewModel;

public class MemoryResultDialog extends BaseDialog {
    private DialogResultBinding resultBinding;
    private MemoryViewModel memoryViewModel;

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ){
        resultBinding = DialogResultBinding.inflate(inflater, container, false);
        return resultBinding.getRoot();
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


    public void setView() {
        int prevBestScore = memoryViewModel.getBestScore(); // 이전 최고 기록
        int finalScore = memoryViewModel.getScore(); // 현재 달성한 기록

        resultBinding.btnGoMain.setOnClickListener(btn -> {
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent); // 메인 화면으로 이동
            requireActivity().finish(); // 메모리게임 액티비티 종료
        });

        resultBinding.btnRestart.setOnClickListener(btn -> {
            Intent intent = new Intent(requireActivity(), HangmanActivity.class);
            try {
                intent.putExtra("difficulty", memoryViewModel.getDifficulty());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            startActivity(intent);
            requireActivity().finish(); // 행맨 액티비티 종료
        });
        resultBinding.tvFinalScore.setText(Integer.toString(finalScore));

        if (finalScore > prevBestScore){
            resultBinding.tvBestScore.setText(Integer.toString(finalScore));
        } else {
            resultBinding.tvBestScore.setText(Integer.toString(prevBestScore));
            resultBinding.tvNoticeBest.setVisibility(View.INVISIBLE);
        }
    }
}
