package com.example.hangman_java.record.view;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.databinding.FragmentRechangmanBinding;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.viewmodel.RecordViewModel;

public class RecHangmanFragment extends BaseFragment {
    private FragmentRechangmanBinding frRecBinding = null;
    private Context parentContext = null;
    private RecordViewModel recordViewModel = null;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        parentContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        frRecBinding = FragmentRechangmanBinding.inflate(inflater, container, false);
        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return frRecBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);
        updateUi();
    }
    @Override
    protected void initUi() throws Exception {

    }

    private void updateUi(){
        recordViewModel.getBestRecord(parentContext, "hangman");
        recordViewModel.getRecentRecord(parentContext, "hangman");
        setBestRecord();
        setRecentRecord();
    }

    private void setBestRecord(){
        recordViewModel.bestRecordList().observe(getViewLifecycleOwner(), bestRecordList -> {
            boolean[] isExist = {false, false, false};
            for (Record rec : bestRecordList.getContentIfNotHandled()) {
                Log.d("MYTAG", "기록: {" + rec.difficulty + ", " + rec.record + ", " + rec.date + "} 가 삽입됨");
                switch (rec.difficulty) {
                    case "easy" -> {
                        frRecBinding.tvEasyRecord.setText(String.valueOf(rec.record));
                        frRecBinding.tvEasyDate.setText(rec.date);
                        isExist[0] = true;
                    }
                    case "normal" -> {
                        frRecBinding.tvNormalRecord.setText(String.valueOf(rec.record));
                        frRecBinding.tvNormalDate.setText(rec.date);
                        isExist[1] = true;
                    }
                    case "hard" -> {
                        frRecBinding.tvHardRecord.setText(String.valueOf(rec.record));
                        frRecBinding.tvHardDate.setText(rec.date);
                        isExist[2] = true;
                    }
                }
            }

            for (int i = 0; i < 3; i++){
                if (!isExist[i]){
                    switch (i){
                        case 0 -> {
                            mergeCell(frRecBinding.tvEasyRecord, frRecBinding.tvEasyDate);
                            frRecBinding.tvEasyRecord.setText("기록이 없습니다");
                            break;
                        }
                        case 1 -> {
                            mergeCell(frRecBinding.tvNormalRecord, frRecBinding.tvNormalDate);
                            frRecBinding.tvNormalRecord.setText("기록이 없습니다");
                            break;
                        }
                        case 2 -> {
                            mergeCell(frRecBinding.tvHardRecord, frRecBinding.tvHardDate);
                            frRecBinding.tvHardRecord.setText("기록이 없습니다");
                            break;
                        }
                    }
                }
            }
        }); // recordViewModel.bestRecordList().observe()
    }

    private void setRecentRecord(){
        recordViewModel.recentRecordList().observe(getViewLifecycleOwner(), recentRecord -> {
            if (recentRecord.peekContent().size()==0){
                frRecBinding.tvGoneMessage.setVisibility(View.VISIBLE);
                frRecBinding.tlRecentRecord.setVisibility(View.GONE);
                return;
            }
            // 테이블 행 동적 추가
            TableRow.LayoutParams trParams = new TableRow.LayoutParams();
            trParams.bottomMargin = convertToDP();
            trParams.topMargin = convertToDP();
            for (Record rec : recentRecord.getContentIfNotHandled()){
                TableRow tableRow = new TableRow(getContext());
                tableRow.setLayoutParams(trParams);
                // 이곳에 tableRow 배경 설정 코드 삽입
                String[] textData = {"", String.valueOf(rec.record), rec.date};
                switch (rec.difficulty){
                    case "easy" -> textData[0] = "쉬움";
                    case "normal" -> textData[0] = "보통";
                    case "hard" -> textData[0] = "어려움";
                }
                for (int i = 0; i < 3; i++){
                    TextView textView = new TextView(getContext());
                    TableRow.LayoutParams params = new TableRow.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    if (i==2) params.weight = 2f;
                    params.bottomMargin = convertToDP();
                    params.topMargin = convertToDP();
                    textView.setText(textData[i]);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setLayoutParams(params);
                    tableRow.addView(textView);
                }
                frRecBinding.tlRecentRecord.addView(tableRow);
            }
        });
    }

    private void mergeCell(TextView tvRecord, TextView tvDate){
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 3f;
        tvRecord.setLayoutParams(params);
        tvDate.setVisibility(View.GONE);
    }

    private int convertToDP(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(5 * displayMetrics.density);
    }
}
