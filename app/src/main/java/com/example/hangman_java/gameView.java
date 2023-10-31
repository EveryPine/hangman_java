package com.example.hangman_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class gameView extends AppCompatActivity {

    Intent intent;
    int level = -1;  // 적절한 기본값으로 초기화
    private logic gameLogic = null;
    private model gameModel = null;
    private TextView scoreText = null;
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
        setContentView(R.layout.activity_game_view);

        intent = getIntent();
        level = intent.getIntExtra("level", -1);
        scoreText = findViewById(R.id.score); scoreText.setText("Score: " + 0);
        LocalBroadcastManager.getInstance(this).registerReceiver(scoreUpdateReceiver, new IntentFilter("update_score"));

        if (level != -1) {
            gameModel = new model(level);
            gameLogic = new logic(gameModel);

            gridView = findViewById(R.id.gridView);
            gridView.setNumColumns(level);
            adapter = new GridViewAdapter(gameLogic);
            gridView.setAdapter(adapter);


        } else {
            // level이 올바르게 전달되지 않은 경우 처리
            Toast.makeText(this, "올바른 level이 전달되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}