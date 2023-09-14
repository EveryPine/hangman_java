package com.example.hangman_java.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.databinding.ActivityGameBinding;
import com.example.hangman_java.mvvm.viewmodel.GameViewModel;
import com.example.hangman_java.ui.fragment.KeyboardFragment;
import com.example.hangman_java.ui.fragment.PrintingFragment;
import com.example.hangman_java.ui.fragment.WordspaceFragment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameActivity extends BaseActivity{
    private ActivityGameBinding binding = null;
    private GameViewModel gameViewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = ActivityGameBinding.inflate(getLayoutInflater());
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        if (getIntent().hasExtra("difficulty")){
            gameViewModel.setStageInfo(getIntent().getIntExtra("difficulty", 0));
        } else {
            Log.e("MyTAG", "GameActivity에서 difficulty값을 가져오는데 실패했습니다.");
        }
        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        initGameEndObserver();
        setContentView(binding.getRoot());
    }
    @Override
    void initUi() throws Exception {
        Log.d("MyTAG", "게임 초기화 시작");
        HashMap<FragmentContainerView, Fragment> inputHashMap = new HashMap<>(){{
            put(binding.fragmentContainerViewPrinting, new PrintingFragment());
            put(binding.fragmentContainerViewKeyboard, new KeyboardFragment());
            put(binding.fragmentContainerViewWordspace, new WordspaceFragment());
        }};
        gameViewModel.setWaveInfo(this);
        gameViewModel.setResourceInfo();
        replaceFragments(inputHashMap);

    }

    private void initGameEndObserver(){
        gameViewModel.getGameClearFlag().observe(this, gameClearFlag -> {
            gameViewModel.updateGameScore();
            binding.textCurrentScore.setText(Integer.toString(gameViewModel.getGameScore()));
            try {
                initUi();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        gameViewModel.getGameoverFlag().observe(this, gameoverFlag -> startGameoverActivity());

    }

    private void startGameoverActivity(){
        Intent nextIntent = new Intent(this, GameoverActivity.class);
        nextIntent.putExtra("difficulty", gameViewModel.getDifficulty());
        startActivity(nextIntent);
    }

    private void replaceFragments(HashMap<FragmentContainerView, Fragment> inputMap){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Iterator<Map.Entry<FragmentContainerView, Fragment>> iterator = inputMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<FragmentContainerView, Fragment> entrySet = iterator.next();
            fragmentTransaction.replace(entrySet.getKey().getId(), entrySet.getValue());
        }
        fragmentTransaction.commit();
    }


}
