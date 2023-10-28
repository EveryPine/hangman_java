package com.example.hangman_java.debug.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseActivity;
import com.example.hangman_java.base.EventObserver;
import com.example.hangman_java.databinding.ActivityDebugBinding;
import com.example.hangman_java.debug.viewmodel.DebugViewModel;
import com.example.hangman_java.hangman.view.HangmanActivity;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.viewmodel.RecordViewModel;

import java.util.Iterator;

public class DebugActivity extends BaseActivity {
    private ActivityDebugBinding debugBinding;
    private DebugViewModel debugViewModel;
    private RecordViewModel recordViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        debugBinding = ActivityDebugBinding.inflate(getLayoutInflater());
        debugViewModel = new ViewModelProvider(this).get(DebugViewModel.class);
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);
        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setContentView(debugBinding.getRoot());
    }

    @Override
    public void initUi() throws Exception {
        setView();
    }

    @SuppressLint("SetTextI18n")
    private void setView(){
        debugViewModel.data().observe(this, data -> {
            debugBinding.tvGenData.setText("gamename: " + data.gamename +"\ndifficulty: " + data.difficulty + "\nrecord: " + data.record);
        });

        recordViewModel.getDebugRecordList().observe(this, new EventObserver<>(debugRecordList -> {
            Log.d("MyTAG", "디버그 레코드 보여줌");
            String inputStr = debugViewModel.convertRecordToStr(debugRecordList);
            debugBinding.tvShowDb.setText(inputStr);
        }));

        debugBinding.btnGenData.setOnClickListener(view -> {
            debugViewModel.genRandomData();
        });

        debugBinding.btnInsertData.setOnClickListener(view -> {
            if (debugViewModel.getData()==null){
                debugBinding.tvGenData.setText("삽입할 데이터를 먼저 생성해주세요!");
                return;
            }
            recordViewModel.insertRecord(this, debugViewModel.getData());
        });

        debugBinding.btnShowBest.setOnClickListener(view -> {
            recordViewModel.getAllBestRecord(this);
        });

        debugBinding.btnShowRecent.setOnClickListener(view -> {
            recordViewModel.getAllRecentRecord(this);
        });

        debugBinding.btnCard.setOnClickListener(view -> {
//            Intent intent = new Intent(this, ActivityName.class);
//            intent.putExtra("difficulty", debugViewModel.getDifficulty());
//            startActivity(intent);
        });

        debugBinding.btnHangman.setOnClickListener(view -> {
            Intent intent = new Intent(this, HangmanActivity.class);
            intent.putExtra("difficulty", debugViewModel.getDifficulty());
            startActivity(intent);
        });

        debugBinding.btnMemory.setOnClickListener(view -> {
//            Intent intent = new Intent(this, ActivityName.class);
//            intent.putExtra("difficulty", debugViewModel.getDifficulty());
//            startActivity(intent);
        });
        setDiffSpinner();
    }

    private void setDiffSpinner(){
        Spinner diffSpinner = debugBinding.sprDifficulty;
        String[] diffArray = {"쉬움", "보통", "어려움"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, diffArray);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        diffSpinner.setAdapter(adapter);

        diffSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                debugViewModel.setDifficulty(i + 1);
                Log.d("MyTAG", "난이도가 " + diffArray[i] + "으로 변경되었습니다.");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                debugViewModel.setDifficulty(1);
            }
        });
    }
}
