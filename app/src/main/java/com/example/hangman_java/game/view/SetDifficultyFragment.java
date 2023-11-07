package com.example.hangman_java.game.view;

import static com.example.hangman_java.main.view.MainActivity.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.databinding.FragmentSetdifficultyBinding;
import com.example.hangman_java.game.viewmodel.GameViewModel;
import com.example.hangman_java.hangman.view.HangmanActivity;
import com.example.hangman_java.card.view.CardActivity;
import com.example.hangman_java.memory.view.MemoryActivity;

public class SetDifficultyFragment extends BaseFragment{
    private FragmentSetdifficultyBinding setdifficultyBinding;
    private GameViewModel gameViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        setdifficultyBinding = FragmentSetdifficultyBinding.inflate(inflater, container, false);
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
        setdifficultyBinding.btnEasy.setOnClickListener(view -> gameViewModel.setDifficulty(0));
        setdifficultyBinding.btnNormal.setOnClickListener(view -> gameViewModel.setDifficulty(1));
        setdifficultyBinding.btnHard.setOnClickListener(view -> gameViewModel.setDifficulty(2));
        setdifficultyBinding.btnGamestart.setOnClickListener(view -> {
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
}