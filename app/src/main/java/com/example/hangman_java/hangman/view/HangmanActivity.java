package com.example.hangman_java.hangman.view;

import static com.example.hangman_java.main.view.MainActivity.soundPool;

import android.content.Intent;
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

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseActivity;
import com.example.hangman_java.base.Event;
import com.example.hangman_java.base.EventObserver;
import com.example.hangman_java.databinding.ActivityHangmanBinding;
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;
import com.example.hangman_java.music.HangmanBgmService;
import com.example.hangman_java.music.SfxManager;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.viewmodel.RecordViewModel;

import java.util.HashMap;
import java.util.Map;

public class HangmanActivity extends BaseActivity {
    public static final int START_TIME = 30;
    private Handler handler = new Handler();
    private ActivityHangmanBinding hangmanBinding = null;
    private KeyboardFragment keyboardFragment;
    private HangmanViewModel hangmanViewModel;
    private RecordViewModel recordViewModel;
    private SfxManager sfxManager;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        hangmanBinding = ActivityHangmanBinding.inflate(getLayoutInflater());
        hangmanViewModel = new ViewModelProvider(this).get(HangmanViewModel.class);
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);
        sfxManager = new SfxManager(this, soundPool);
        sfxManager.addSound("gameclear", R.raw.hangman_gameclear);
        sfxManager.addSound("gameover", R.raw.hangman_gameover);

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
        serviceStart();
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
            hangmanBinding.tvRemainingTime.setTextColor(time > 10 ? Color.rgb(0xff, 0x98, 0) : Color.RED);
            if (time <= 0) gameOver();
            if (time >= 0) hangmanBinding.tvRemainingTime.setText(time.toString());
        }));
        hangmanBinding.tvWordDebug.setOnClickListener(view -> hangmanBinding.debug.setVisibility(View.GONE));
        hangmanBinding.btnPause.setOnClickListener(new PauseListener());
    }

    // 게임 클리어 플래그 핸들러
    private void initGameEndObserver(){
        hangmanViewModel.gameClearFlag().observe(this, gameClearFlag -> handler.postDelayed(() -> {
            hangmanBinding.tvRemainingTime.setText(Integer.toString(START_TIME));
            hangmanBinding.tvRemainingTime.setTextColor(Color.rgb(0xff, 0x98, 0));
            hangmanViewModel.updateGameScore();
            sfxManager.playSound("gameclear");
            updateCurrentScoreUi();
            try {
                initUi();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, 500));
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
        serviceStop();
        sfxManager.playSound("gameover");

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
        flag.observe(this, new EventObserver<>(bool -> hangmanBinding.tvCurrentScore.setTextColor(Color.rgb(0xff, 0x98, 0))));
    }

    protected void setWordDebug(@NonNull String word){
        hangmanBinding.tvWordDebug.setText(word.toUpperCase());
    }

    // 배경음 (재)시작
    protected void serviceStart(){
        Intent intent = new Intent(this, HangmanBgmService.class);
        startService(intent);
    }

    // 배경음 일시정지
    protected void servicePause(){
        Intent intent = new Intent(this, HangmanBgmService.class);
        intent.putExtra("isPaused", true);
        startService(intent);
    }

    // 배경음악 종료
    protected void serviceStop(){
        Intent intent = new Intent(this, HangmanBgmService.class);
        stopService(intent);
    }

    private class PauseListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            servicePause();
            sfxManager.playSound("sys_button");
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
