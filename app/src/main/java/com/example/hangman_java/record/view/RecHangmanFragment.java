package com.example.hangman_java.record.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.databinding.FragmentRechangmanBinding;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.viewmodel.RecordViewModel;

public class RecHangmanFragment extends RecordFragment {
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
                switch (rec.difficulty) {
                    case "easy" -> {
                        frRecBinding.tvEasyRecord.setText(rec.record + " 점");
                        frRecBinding.tvEasyDate.setText(rec.date);
                        isExist[0] = true;
                    }
                    case "normal" -> {
                        frRecBinding.tvNormalRecord.setText(rec.record + " 점");
                        frRecBinding.tvNormalDate.setText(rec.date);
                        isExist[1] = true;
                    }
                    case "hard" -> {
                        frRecBinding.tvHardRecord.setText(rec.record + " 점");
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
                        }
                        case 1 -> {
                            mergeCell(frRecBinding.tvNormalRecord, frRecBinding.tvNormalDate);
                            frRecBinding.tvNormalRecord.setText("기록이 없습니다");
                        }
                        case 2 -> {
                            mergeCell(frRecBinding.tvHardRecord, frRecBinding.tvHardDate);
                            frRecBinding.tvHardRecord.setText("기록이 없습니다");
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
            for (TableRow tr : createTableRow(recentRecord)){
                frRecBinding.tlRecentRecord.addView(tr);
            }
        });
    }
}
