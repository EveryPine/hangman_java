package com.example.hangman_java.hangman.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

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
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.viewmodel.RecordViewModel;

import java.util.HashMap;
import java.util.Map;

public class HangmanActivity extends BaseActivity {
    public static final int START_TIME = 30;
    private Handler handler = new Handler();
    private ActivityHangmanBinding hangmanBinding = null;
    private KeyboardFragment keyboardFragment;
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

    // ui 초기화
    @Override
    public void initUi() throws Exception {
        Log.d("MyTAG", "게임 초기화 시작");
        hangmanViewModel.setWaveInfo(this);
        hangmanViewModel.setResourceInfo();
        setView();

        HashMap<FragmentContainerView, Fragment> inputHashMap = new HashMap<>(){{
            put(hangmanBinding.fragmentContainerViewPrinting, new PrintingFragment());
            put(hangmanBinding.fragmentContainerViewKeyboard, keyboardFragment = new KeyboardFragment());
        }};
        replaceFragments(inputHashMap);

    }

    // 위젯 초기화
    private void setView() throws Exception {
        recordViewModel.getBestScore(this, "hangman", hangmanViewModel.getStrDifficulty());
        recordViewModel.bestScore().observe(this, new EventObserver<>(bestScore -> {
            hangmanBinding.tvBestScore.setText(bestScore.toString());
            hangmanViewModel.setBestScore(bestScore);
        }));
        hangmanViewModel.startTimer(START_TIME, false);
        hangmanViewModel.remainingTime().observe(this, new EventObserver<>(time -> {
            hangmanBinding.tvRemainingTime.setTextColor(time > 10 ? Color.BLACK : Color.RED);
            if (time <= 0) gameOver();
            if (time >= 0) hangmanBinding.tvRemainingTime.setText(time.toString());
        }));
        hangmanBinding.tvWordDebug.setOnClickListener(view -> hangmanBinding.debug.setVisibility(View.GONE));
        hangmanBinding.frPause.setOnClickListener(new PauseListener());
        hangmanBinding.btnPause.setOnClickListener(new PauseListener());
    }

    // 게임 클리어 플래그 핸들러
    private void initGameEndObserver(){
        hangmanViewModel.gameClearFlag().observe(this, gameClearFlag -> handler.postDelayed(() -> {
            hangmanBinding.tvRemainingTime.setText(Integer.toString(START_TIME));
            hangmanBinding.tvRemainingTime.setTextColor(Color.BLACK);
            hangmanViewModel.updateGameScore();
            updateCurrentScoreUi();
            try {
                initUi();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 700));
        hangmanViewModel.gameoverFlag().observe(this, gameoverFlag -> {
            try {
                gameOver();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    // 게임 오버 핸들러
    private void gameOver() throws Exception {
        recordViewModel.insertRecord(this, new Record("hangman", hangmanViewModel.getStrDifficulty(), hangmanViewModel.getGameScore()));
        hangmanViewModel.startTimer(START_TIME, true);
        keyboardFragment.setViewUnclickable();

        handler.postDelayed(() -> {
            FragmentManager fm = getSupportFragmentManager();
            ResultDialog resultDialog = new ResultDialog();
            resultDialog.show(fm, "test");}, 2000);
    }

    // 프래그먼트 새로고침
    private void replaceFragments(@NonNull HashMap<FragmentContainerView, Fragment> inputMap){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Map.Entry<FragmentContainerView, Fragment> entrySet : inputMap.entrySet()) {
            fragmentTransaction.replace(entrySet.getKey().getId(), entrySet.getValue());
        }
        fragmentTransaction.commit();
    }

    // 획득 점수 업데이트
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

    protected void setWordDebug(@NonNull String word){
        hangmanBinding.tvWordDebug.setText(word.toUpperCase());
    }

    private class PauseListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            try {
                hangmanViewModel.stopTimer();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            FragmentManager fm = getSupportFragmentManager();
            PauseDialog pauseDialog = new PauseDialog();
            pauseDialog.show(fm, "test");

        }
    }
}
