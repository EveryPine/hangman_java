package com.example.hangman_java.hangman.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

public class ResultDialog extends BaseDialog {
    private DialogResultBinding resultBinding;
    private HangmanActivity parentActivity;
    private HangmanViewModel hangmanViewModel;

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ){
        resultBinding = DialogResultBinding.inflate(inflater, container, false);
        parentActivity = (HangmanActivity) requireActivity();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return resultBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        hangmanViewModel = new ViewModelProvider(parentActivity).get(HangmanViewModel.class);
        initUi();
    }

    @Override
    public void initUi() {
        setView();
    }


    public void setView() {
        int prevBestScore = hangmanViewModel.getBestScore(); // 이전 최고 기록
        int finalScore = hangmanViewModel.getGameScore(); // 현재 달성한 기록

        resultBinding.btnGoMain.setOnClickListener(btn -> {
            Intent intent = new Intent(parentActivity, MainActivity.class);
            parentActivity.serviceStop();
            parentActivity.finish(); // 행맨 액티비티 종료
            startActivity(intent); // 메인 화면으로 이동
        });

        resultBinding.btnRestart.setOnClickListener(btn -> {
            Intent intent = new Intent(parentActivity, HangmanActivity.class);
            try {
                intent.putExtra("difficulty", hangmanViewModel.getIntDifficulty());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            parentActivity.serviceStop();
            parentActivity.finish(); // 행맨 액티비티 종료
            startActivity(intent);
        });

        resultBinding.tvFinalScore.setText("최종 점수 : " + finalScore);

        if (finalScore <= prevBestScore){
            resultBinding.tvBestScore.setText("최고 점수 : " + prevBestScore);
            resultBinding.tvNoticeBest.setVisibility(View.INVISIBLE);
        } else {
            resultBinding.tvBestScore.setText("최고 점수 : " + finalScore);
        }
    }
}
