package com.example.hangman_java.card.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseActivity;
import com.example.hangman_java.card.controller.logic;
import com.example.hangman_java.card.model.model;
import com.example.hangman_java.game.view.GameActivity;
import com.example.hangman_java.main.view.MainActivity;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.view.RecordActivity;
import com.example.hangman_java.record.viewmodel.RecordViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class CardActivity extends BaseActivity {

    Intent intent;
    int level = -1;  // 적절한 기본값으로 초기화
    private logic gameLogic = null;
    private model gameModel = null;
    private TextView scoreText = null;
    private ProgressBar progressBar = null;
    private Timer timer;
    private int time;
    private TimerTask timerTask;
    private GridView gridView = null;
    private GridViewAdapter adapter = null;
    private BroadcastReceiver scoreUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("update_score")) {
                int newScore = intent.getIntExtra("new_score", 0);
                scoreText.setText("Score: " + newScore);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        intent = getIntent();
        level = intent.getIntExtra("difficulty", -1) + 2;
        scoreText = findViewById(R.id.score); scoreText.setText("Score: " + 0);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        LocalBroadcastManager.getInstance(this).registerReceiver(scoreUpdateReceiver, new IntentFilter("update_score"));

        if (level != -1) {
            gameModel = new model(level);
            gameLogic = new logic(gameModel);
            timer = new Timer(); time = gameModel.getTime();

            gridView = findViewById(R.id.gridView);
            gridView.setNumColumns(level);
            adapter = new GridViewAdapter(gameLogic);
            gridView.setAdapter(adapter);

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(time > 0){
                        time--;
                        progressBar.setProgress((int)(((float)time / (float)gameModel.getTime()) * 100));
                    }else{
                        timer.cancel();
                        exitGame();
                    }
                }
            };
            timer.schedule(timerTask,0,1000);

        } else {
            // level이 올바르게 전달되지 않은 경우 처리
            Toast.makeText(this, "올바른 level이 전달되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void exitGame() {
            String diff;
            if (level == 2) diff = "easy";
            else if (level == 3) diff = "normal";
            else diff = "hard";

            Record record = new Record("card", diff, gameModel.getScore());
            RecordViewModel recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);
            recordViewModel.insertRecord(this, record);

            Intent intent = new Intent(CardActivity.this, RecordActivity.class);
            Intent intent2 = new Intent(CardActivity.this, MainActivity.class);
            finish();
            startActivity(intent2);
            startActivity(intent);
    }

    @Override
    public void initUi() throws Exception {

    }
}