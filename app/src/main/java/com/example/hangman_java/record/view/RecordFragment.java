package com.example.hangman_java.record.view;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.base.Event;
import com.example.hangman_java.record.model.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordFragment extends BaseFragment {
    @Override
    protected void initUi() throws Exception {


    }

    List<TableRow> createTableRow(List<Record> recentRecord){
        List<TableRow> resultTableRow = new ArrayList<>();
        TableRow.LayoutParams trParams = new TableRow.LayoutParams();
        trParams.bottomMargin = convertToDP(5);
        trParams.topMargin = convertToDP(5);
        for (Record rec : recentRecord){
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(trParams);
            // 이곳에 tableRow 배경 설정 코드 삽입
            String[] textData = {"", rec.record + " 점", rec.date};
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
                params.bottomMargin = convertToDP(5);
                params.topMargin = convertToDP(5);
                textView.setText(textData[i]);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setLayoutParams(params);
                tableRow.addView(textView);
            }
            resultTableRow.add(tableRow);
        }
        return resultTableRow;
    }

    void mergeCell(TextView tvRecord, TextView tvDate){
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 3f;
        tvRecord.setLayoutParams(params);
        tvDate.setVisibility(View.GONE);
    }

    int convertToDP(int px){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(px * displayMetrics.density);
    }
}
