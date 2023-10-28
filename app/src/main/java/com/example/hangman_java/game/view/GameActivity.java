package com.example.hangman_java.game.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.BaseActivity;
import com.example.hangman_java.base.EventObserver;
import com.example.hangman_java.databinding.ActivityGameBinding;
import com.example.hangman_java.game.viewmodel.GameViewModel;
import com.example.hangman_java.main.view.MainActivity;

public class GameActivity extends BaseActivity {
    private ActivityGameBinding gameBinding;
    private GameViewModel gameViewModel;
    private FragmentManager fragmentManager;
    private Fragment frSetGame, frSetDifficulty;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        gameBinding = ActivityGameBinding.inflate(getLayoutInflater());
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        fragmentManager = getSupportFragmentManager();
        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setContentView(gameBinding.getRoot());
    }
    @Override
    public void initUi() throws Exception {
        if (frSetGame==null) frSetGame = new SetGameFragment();
        if (frSetDifficulty==null) frSetDifficulty = new SetDifficultyFragment();
        fragmentManager.beginTransaction()
            .add(gameBinding.frContainer.getId(), frSetGame)
            .add(gameBinding.frContainer.getId(), frSetDifficulty)
            .hide(frSetDifficulty)
            .commit();
        gameViewModel.selectedGame().observe(this, new EventObserver<>(game -> {
            gameViewModel.setCurrentFragment("difficulty");
            fragmentManager.beginTransaction().hide(frSetGame).show(frSetDifficulty).commit();
        }));
        gameViewModel.setCurrentFragment("game");
        setView();
    }

    private void setView(){
        gameBinding.btnGoback.setOnClickListener(view -> {
            // 게임 난이도 설정 화면이 올라와 있으면
            if (gameViewModel.getCurrentFragment().equals("difficulty")){
                Log.d("MyTAG", "난이도 설정 화면에서 게임 선택 화면으로 돌아감");
                gameViewModel.setCurrentFragment("game");
                fragmentManager.beginTransaction().hide(frSetDifficulty).show(frSetGame).commit();
            //  게임 선택 화면이 올라와있으면
            } else {
                finish();
            }
        });
    }
}
