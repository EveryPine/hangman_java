package com.example.hangman_java.record.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.EventObserver;
import com.example.hangman_java.databinding.FragmentRechangmanBinding;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.viewmodel.RecordViewModel;

public class RecHangmanFragment extends RecordFragment {
    private FragmentRechangmanBinding recHangmanBinding = null;
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
        recHangmanBinding = FragmentRechangmanBinding.inflate(inflater, container, false);
        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return recHangmanBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);
        try {
            updateUi();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void initUi() throws Exception {

    }

    private void updateUi() throws InterruptedException {
        recordViewModel.getBestRecord(parentContext, "hangman");
        recordViewModel.getRecentRecord(parentContext, "hangman");
        setBestRecord();
        setRecentRecord();
    }

    private void setBestRecord(){
        recordViewModel.hangmanBestRecordList().observe(getViewLifecycleOwner(), new EventObserver<>(bestRecordList -> {
            for (Record rec : bestRecordList) {
                switch (rec.difficulty) {
                    case "easy" -> {
                        recHangmanBinding.tvEasyRecord.setText(rec.record + " 점");
                        recHangmanBinding.tvEasyDate.setText(rec.date);
                    }
                    case "normal" -> {
                        recHangmanBinding.tvNormalRecord.setText(rec.record + " 점");
                        recHangmanBinding.tvNormalDate.setText(rec.date);
                    }
                    case "hard" -> {
                        recHangmanBinding.tvHardRecord.setText(rec.record + " 점");
                        recHangmanBinding.tvHardDate.setText(rec.date);
                    }
                }
            }
        }));

    }

    private void setRecentRecord(){
        recordViewModel.hangmanRecentRecordList().observe(getViewLifecycleOwner(), new EventObserver<>(recentRecord -> {
            if (recentRecord.size()==0){
                recHangmanBinding.tvGoneMessage.setVisibility(View.VISIBLE);
                recHangmanBinding.tlRecentRecord.setVisibility(View.GONE);
                return;
            }
            for (TableRow tr : createTableRow(recentRecord)){
                recHangmanBinding.tlRecentRecord.addView(tr);
            }
        }));
    }
}