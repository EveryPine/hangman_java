package com.example.hangman_java.hangman.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.BaseActivity;
import com.example.hangman_java.base.Event;
import com.example.hangman_java.base.EventObserver;
import com.example.hangman_java.databinding.ActivityHangmanBinding;
import com.example.hangman_java.game.view.ResultDialog;
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.viewmodel.RecordViewModel;

import java.util.HashMap;
import java.util.Map;

public class HangmanActivity extends BaseActivity {
    public static final int START_TIME = 30;
    private ActivityHangmanBinding hangmanBinding = null;
    private KeyboardFragment keyboardFragment;
    private PrintingFragment printingFragment;
    private HangmanViewModel hangmanViewModel = null;
    private RecordViewModel recordViewModel = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        hangmanBinding = ActivityHangmanBinding.inflate(getLayoutInflater());
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
        setContentView(hangmanBinding.getRoot());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){}
    }
    @Override
    public void initUi() throws Exception {
        Log.d("MyTAG", "게임 초기화 시작");
        hangmanViewModel.setWaveInfo(this);
        hangmanViewModel.setResourceInfo();
        setView();

        HashMap<FragmentContainerView, Fragment> inputHashMap = new HashMap<>(){{
            put(hangmanBinding.fragmentContainerViewPrinting, printingFragment = new PrintingFragment());
            put(hangmanBinding.fragmentContainerViewKeyboard, keyboardFragment = new KeyboardFragment());
        }};
        replaceFragments(inputHashMap);

    }

    private void setView() throws Exception {
        recordViewModel.getBestScore(this, "hangman", hangmanViewModel.getStrDifficulty());
        recordViewModel.bestScore().observe(this, new EventObserver<>(bestScore -> {
            hangmanBinding.tvBestScore.setText(bestScore.toString());
            hangmanViewModel.setBestScore(bestScore);
        }));
        hangmanViewModel.setTimer(START_TIME, false);
        hangmanViewModel.remainingTime().observe(this, new EventObserver<>(time -> {
            hangmanBinding.tvRemainingTime.setTextColor(time > 10 ? Color.BLACK : Color.RED);
            if (time < 0) gameOver();
            if (time >= 0) hangmanBinding.tvRemainingTime.setText(time.toString());
        }));

    }

    private void initGameEndObserver(){
        hangmanViewModel.gameClearFlag().observe(this, gameClearFlag -> {
            hangmanBinding.tvRemainingTime.setText(Integer.toString(START_TIME));
            hangmanBinding.tvRemainingTime.setTextColor(Color.BLACK);
            hangmanViewModel.updateGameScore();
            updateCurrentScoreUi();
            try {
                initUi();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        hangmanViewModel.gameoverFlag().observe(this, gameoverFlag -> {
            try {
                gameOver();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void gameOver() throws Exception {
        Handler handler = new Handler();
        recordViewModel.insertRecord(this, new Record("hangman", hangmanViewModel.getStrDifficulty(), hangmanViewModel.getGameScore()));
        hangmanViewModel.setTimer(START_TIME, true);
        keyboardFragment.setBtnUnclickable();

        handler.postDelayed(() -> {
            FragmentManager fm = getSupportFragmentManager();
            ResultDialog resultDialog = new ResultDialog();
            resultDialog.show(fm, "test");}, 2000);
    }

    private void replaceFragments(@NonNull HashMap<FragmentContainerView, Fragment> inputMap){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Map.Entry<FragmentContainerView, Fragment> entrySet : inputMap.entrySet()) {
            fragmentTransaction.replace(entrySet.getKey().getId(), entrySet.getValue());
        }
        fragmentTransaction.commit();
    }

    private void updateCurrentScoreUi(){
        MutableLiveData<Event<Boolean>> flag = new MutableLiveData<>();
        hangmanBinding.tvCurrentScore.setText(Integer.toString(hangmanViewModel.getGameScore()));
        hangmanBinding.tvCurrentScore.setTextColor(Color.GREEN);
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            flag.postValue(new Event<>(true));
        });
        thread.start();
        flag.observe(this, new EventObserver<>(bool -> hangmanBinding.tvCurrentScore.setTextColor(Color.BLACK)));
    }
}
