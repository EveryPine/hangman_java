package com.example.hangman_java.hangman.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.BaseActivity;
import com.example.hangman_java.base.Event;
import com.example.hangman_java.base.EventObserver;
import com.example.hangman_java.databinding.ActivityHangmanBinding;
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;
import com.example.hangman_java.record.viewmodel.RecordViewModel;

import java.util.HashMap;
import java.util.Map;

public class HangmanActivity extends BaseActivity {
    private ActivityHangmanBinding binding = null;
    private HangmanViewModel hangmanViewModel = null;
    private RecordViewModel recordViewModel = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = ActivityHangmanBinding.inflate(getLayoutInflater());
        hangmanViewModel = new ViewModelProvider(this).get(HangmanViewModel.class);
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

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
        hangmanViewModel.setWaveInfo(this);
        hangmanViewModel.setResourceInfo();
        setView();

        HashMap<FragmentContainerView, Fragment> inputHashMap = new HashMap<>(){{
            put(binding.fragmentContainerViewPrinting, new PrintingFragment());
            put(binding.fragmentContainerViewKeyboard, new KeyboardFragment());
            put(binding.fragmentContainerViewWordspace, new WordspaceFragment());
        }};
        replaceFragments(inputHashMap);

    }

    private void setView(){
        recordViewModel.getBestScore(this, "hangman", hangmanViewModel.getDifficulty());
        recordViewModel.bestScore().observe(this, new EventObserver<>(bestScore -> binding.tvBestScore.setText(bestScore.toString())));
        hangmanViewModel.setTimer();
        hangmanViewModel.remainingTime().observe(this, new EventObserver<>(time -> {
            binding.tvRemainingTime.setTextColor(time > 10 ? Color.BLACK : Color.RED);
            if (time < 0) gameOver();
            if (time >= 0) binding.tvRemainingTime.setText(time.toString());
        }));

    }

    private void initGameEndObserver(){
        hangmanViewModel.getGameClearFlag().observe(this, gameClearFlag -> {
            binding.tvRemainingTime.setText("30");
            binding.tvRemainingTime.setTextColor(Color.BLACK);
            hangmanViewModel.updateGameScore();
            updateCurrentScoreUi();
            try {
                initUi();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        hangmanViewModel.getGameoverFlag().observe(this, gameoverFlag -> gameOver());

    }

    private void gameOver(){

    }

    private void replaceFragments(HashMap<FragmentContainerView, Fragment> inputMap){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Map.Entry<FragmentContainerView, Fragment> entrySet : inputMap.entrySet()) {
            fragmentTransaction.replace(entrySet.getKey().getId(), entrySet.getValue());
        }
        fragmentTransaction.commit();
    }

    private void updateCurrentScoreUi(){
        MutableLiveData<Event<Boolean>> flag = new MutableLiveData<>();
        binding.tvCurrentScore.setText(Integer.toString(hangmanViewModel.getGameScore()));
        binding.tvCurrentScore.setTextColor(Color.GREEN);
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            flag.postValue(new Event<>(true));
        });
        thread.start();
        flag.observe(this, new EventObserver<>(bool -> binding.tvCurrentScore.setTextColor(Color.BLACK)));
    }


}
