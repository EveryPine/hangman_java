package com.example.hangman_java.record.view;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.base.TextOutLineView;
import com.example.hangman_java.record.model.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordFragment extends BaseFragment {
    @Override
    protected void initUi() throws Exception {

    }

    @SuppressLint("ResourceAsColor")
    List<TableRow> createTableRow(@NonNull List<Record> recentRecord){
        List<TableRow> resultTableRow = new ArrayList<>();
        // 테이블로우 레이아웃 파라미터
        TableLayout.LayoutParams trParams = new TableLayout.LayoutParams();
        Typeface font = ResourcesCompat.getFont(requireContext(), R.font.sys_font);
        trParams.leftMargin = convertToDP(10);
        trParams.rightMargin = convertToDP(10);
        trParams.bottomMargin = convertToDP(5);
        trParams.topMargin = convertToDP(5);
        for (Record rec : recentRecord){
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(trParams);
            String[] textData = {"", rec.record + " 점", rec.date};
            switch (rec.difficulty){
                case "easy" -> {
                    textData[0] = "쉬움";
                    tableRow.setBackgroundResource(R.drawable.record_easy_bar);
                }
                case "normal" -> {
                    textData[0] = "보통";
                    tableRow.setBackgroundResource(R.drawable.record_normal_bar);
                }
                case "hard" -> {
                    textData[0] = "어려움";
                    tableRow.setBackgroundResource(R.drawable.record_hard_bar);
                }
            }
            for (int i = 0; i < 3; i++){
                TextOutLineView textView = new TextOutLineView(getContext(), null, true, 0xff848484, 5f);
                // 텍스트뷰 레이아웃 파라미터
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, i!=2 ? 1f : 2f);
                params.bottomMargin = convertToDP(5);
                params.topMargin = convertToDP(5);
                textView.setText(textData[i]);
                switch (rec.difficulty){
                    case "easy" -> textView.setTextColor(ContextCompat.getColor(getContext(), R.color.record_easy_color));
                    case "normal" -> textView.setTextColor(ContextCompat.getColor(getContext(), R.color.record_normal_color));
                    case "hard" -> textView.setTextColor(ContextCompat.getColor(getContext(), R.color.record_hard_color));
                }
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, i!=2 ? 18 : 15);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setTypeface(font);
                textView.setLayoutParams(params);
                tableRow.addView(textView);
            }
            resultTableRow.add(tableRow);
        }
        return resultTableRow;
    }

    void mergeCell(@NonNull TextView tvRecord, @NonNull TextView tvDate){
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 3f;
        tvRecord.setLayoutParams(params);
        tvDate.setVisibility(View.GONE);
    }
}
