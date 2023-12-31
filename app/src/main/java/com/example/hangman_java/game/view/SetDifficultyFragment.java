package com.example.hangman_java.game.view;

import static com.example.hangman_java.main.view.MainActivity.mainActivity;
import static com.example.hangman_java.main.view.MainActivity.soundPool;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.databinding.FragmentSetdifficultyBinding;
import com.example.hangman_java.game.viewmodel.GameViewModel;
import com.example.hangman_java.hangman.view.HangmanActivity;
import com.example.hangman_java.card.view.CardActivity;
import com.example.hangman_java.memory.view.MemoryActivity;
import com.example.hangman_java.music.SfxManager;

import java.util.Objects;

public class SetDifficultyFragment extends BaseFragment{
    private FragmentSetdifficultyBinding setdifficultyBinding;
    private GameViewModel gameViewModel;
    private LinearLayout btnEasy, btnNormal, btnHard;
    private SfxManager sfxManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        setdifficultyBinding = FragmentSetdifficultyBinding.inflate(inflater, container, false);
        sfxManager = new SfxManager(requireContext(), soundPool);
        return setdifficultyBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void initUi() throws Exception {
        setView();
    }

    private void setView(){
        btnEasy = setdifficultyBinding.btnEasy;
        btnNormal = setdifficultyBinding.btnNormal;
        btnHard = setdifficultyBinding.btnHard;

        setdifficultyBinding.btnEasy.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            gameViewModel.setDifficulty(0);
            setLayoutImage("easy");
        });
        setdifficultyBinding.btnNormal.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            gameViewModel.setDifficulty(1);
            setLayoutImage("normal");
        });
        setdifficultyBinding.btnHard.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            gameViewModel.setDifficulty(2);
            setLayoutImage("hard");
        });

        setdifficultyBinding.btnGamestart.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            Intent intent = null;
            String game = gameViewModel.getSelectedGame();
            int difficulty = gameViewModel.getDifficulty();
            if (difficulty==-1){
                // 난이도 미설정 오류 메세지 출력
                return;
            }
            switch (game){
                case "card" -> intent = new Intent(getActivity(), CardActivity.class);
                case "hangman" -> intent = new Intent(getActivity(), HangmanActivity.class);
                case "memory" -> intent = new Intent(getActivity(), MemoryActivity.class);
            }
            if (intent!=null){
                intent.putExtra("difficulty", difficulty);
                mainActivity.finish(); // 메인액티비티 종료
                startActivity(intent);
                requireActivity().finish(); // 현재액티비티 종료
            }
        });
    }

    protected void setGameDescriptions(){
        switch (gameViewModel.getSelectedGame()){
            case "card" -> {
                setdifficultyBinding.tvEasyDesc.setText(R.string.cardEasyDesc);
                setdifficultyBinding.tvNormalDesc.setText(R.string.cardNormalDesc);
                setdifficultyBinding.tvHardDesc.setText(R.string.cardHardDesc);
            }
            case "hangman" -> {
                setdifficultyBinding.tvEasyDesc.setText(R.string.hangmanEasyDesc);
                setdifficultyBinding.tvNormalDesc.setText(R.string.hangmanNormalDesc);
                setdifficultyBinding.tvHardDesc.setText(R.string.hangmanHardDesc);
            }
            case "memory" -> {
                setdifficultyBinding.tvEasyDesc.setText(R.string.memoryEasyDesc);
                setdifficultyBinding.tvNormalDesc.setText(R.string.memoryNormalDesc);
                setdifficultyBinding.tvHardDesc.setText(R.string.memoryHardDesc);
            }
        }
    }

    private void setLayoutImage(@NonNull String difficulty){
        if (difficulty.equals("easy")){
            btnEasy.setSelected(true);
            btnNormal.setSelected(false);
            btnHard.setSelected(false);
        }
        if (difficulty.equals("normal")){
            btnEasy.setSelected(false);
            btnNormal.setSelected(true);
            btnHard.setSelected(false);
        }
        if (difficulty.equals("hard")){
            btnEasy.setSelected(false);
            btnNormal.setSelected(false);
            btnHard.setSelected(true);
        }
    }
}