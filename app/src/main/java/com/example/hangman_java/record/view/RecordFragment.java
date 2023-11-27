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
import java.util.Objects;

public class RecordFragment extends BaseFragment {
    @Override
    protected void initUi() throws Exception {

    }

    @SuppressLint("ResourceAsColor")
    List<TableRow> createTableRow(@NonNull List<Record> recentRecord){
        List<TableRow> resultTableRow = new ArrayList<>();
        TableLayout.LayoutParams trParams = getTableRowParams();
        for (Record rec : recentRecord){
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(trParams);
            String[] textData = {"", rec.record + " 점", rec.date};
            if (Objects.equals(rec.difficulty, "easy")) textData[0] = "쉬움";
            if (Objects.equals(rec.difficulty, "normal")) textData[0] = "보통";
            if (Objects.equals(rec.difficulty, "hard")) textData[0] = "어려움";
            tableRow.setBackgroundResource(R.drawable.record_bar);
            for (int i = 0; i < 3; i++){
                TextOutLineView textView
                    = new TextOutLineView(getContext(), null, true, 0xff848484, 5f);
                setTextViewParams(textView, textData[i], i == 2);
                // 텍스트뷰 레이아웃 파라미터
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

    TableLayout.LayoutParams getTableRowParams(){
        TableLayout.LayoutParams trParams = new TableLayout.LayoutParams();
        trParams.leftMargin = convertToDP(10);
        trParams.rightMargin = convertToDP(10);
        trParams.bottomMargin = convertToDP(5);
        trParams.topMargin = convertToDP(5);
        return trParams;
    }

    void setTextViewParams(@NonNull TextView textView, String text, boolean isDate){
        Typeface font = ResourcesCompat.getFont(requireContext(), R.font.sys_font);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
            0, LinearLayout.LayoutParams.WRAP_CONTENT, !isDate ? 1f : 2f);
        params.bottomMargin = convertToDP(5);
        params.topMargin = convertToDP(5);
        textView.setText(text);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, !isDate ? 18 : 15);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTypeface(font);
        textView.setLayoutParams(params);
    }
}
