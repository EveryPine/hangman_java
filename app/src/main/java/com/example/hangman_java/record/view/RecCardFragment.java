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
import com.example.hangman_java.databinding.FragmentReccardBinding;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.viewmodel.RecordViewModel;

public class RecCardFragment extends RecordFragment {
    private FragmentReccardBinding frRecBinding = null;
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
        frRecBinding = FragmentReccardBinding.inflate(inflater, container, false);
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
        recordViewModel.getBestRecord(parentContext, "card");
        recordViewModel.getRecentRecord(parentContext, "card");
        setBestRecord();
        setRecentRecord();
    }

    private void setBestRecord(){
        recordViewModel.cardBestRecordList().observe(getViewLifecycleOwner(), new EventObserver<>(bestRecordList -> {
            for (Record rec : bestRecordList) {
                switch (rec.difficulty) {
                    case "easy" -> {
                        frRecBinding.tvEasyRecord.setText(rec.record + " 점");
                        frRecBinding.tvEasyDate.setText(rec.date);
                    }
                    case "normal" -> {
                        frRecBinding.tvNormalRecord.setText(rec.record + " 점");
                        frRecBinding.tvNormalDate.setText(rec.date);
                    }
                    case "hard" -> {
                        frRecBinding.tvHardRecord.setText(rec.record + " 점");
                        frRecBinding.tvHardDate.setText(rec.date);
                    }
                }
            }
        }));

    }

    private void setRecentRecord(){
        recordViewModel.cardRecentRecordList().observe(getViewLifecycleOwner(), new EventObserver<>(recentRecord -> {
            if (recentRecord.size()==0){
                frRecBinding.tvGoneMessage.setVisibility(View.VISIBLE);
                frRecBinding.tlRecentRecord.setVisibility(View.GONE);
                return;
            }
            for (TableRow tr : createTableRow(recentRecord)){
                frRecBinding.tlRecentRecord.addView(tr);
            }
        }));
    }
}