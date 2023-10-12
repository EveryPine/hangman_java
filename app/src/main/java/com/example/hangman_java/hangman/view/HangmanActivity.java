package com.example.hangman_java.hangman.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.databinding.ActivityHangmanBinding;
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;
import com.example.hangman_java.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

public class HangmanActivity extends BaseActivity {
    private ActivityHangmanBinding binding = null;
    private HangmanViewModel hangmanViewModel = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = ActivityHangmanBinding.inflate(getLayoutInflater());
        hangmanViewModel = new ViewModelProvider(this).get(HangmanViewModel.class);

        if (getIntent().hasExtra("difficulty")){
            hangmanViewModel.setStageInfo(getIntent().getIntExtra("difficulty", 0));
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
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
        }
    }
    @Override
    public void initUi() throws Exception {
        Log.d("MyTAG", "게임 초기화 시작");
        HashMap<FragmentContainerView, Fragment> inputHashMap = new HashMap<>(){{
            put(binding.fragmentContainerViewPrinting, new PrintingFragment());
            put(binding.fragmentContainerViewKeyboard, new KeyboardFragment());
            put(binding.fragmentContainerViewWordspace, new WordspaceFragment());
        }};
        hangmanViewModel.setWaveInfo(this);
        hangmanViewModel.setResourceInfo();
        replaceFragments(inputHashMap);

    }

    private void initGameEndObserver(){
        hangmanViewModel.getGameClearFlag().observe(this, gameClearFlag -> {
            hangmanViewModel.updateGameScore();
            binding.textCurrentScore.setText(Integer.toString(hangmanViewModel.getGameScore()));
            try {
                initUi();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        hangmanViewModel.getGameoverFlag().observe(this, gameoverFlag -> startGameoverActivity());

    }

    private void startGameoverActivity(){
        Intent nextIntent = new Intent(this, GameoverActivity.class);
        nextIntent.putExtra("difficulty", hangmanViewModel.getDifficulty());
        startActivity(nextIntent);
    }

    private void replaceFragments(HashMap<FragmentContainerView, Fragment> inputMap){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Map.Entry<FragmentContainerView, Fragment> entrySet : inputMap.entrySet()) {
            fragmentTransaction.replace(entrySet.getKey().getId(), entrySet.getValue());
        }
        fragmentTransaction.commit();
    }


}
