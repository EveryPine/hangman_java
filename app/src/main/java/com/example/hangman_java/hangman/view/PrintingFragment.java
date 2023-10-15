package com.example.hangman_java.hangman.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.databinding.FragmentPrintingBinding;
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;

public class PrintingFragment extends BaseFragment {
    private FragmentPrintingBinding binding = null;
    private HangmanViewModel hangmanViewModel = null;

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ){
        binding = FragmentPrintingBinding.inflate(inflater, container, false);
        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        hangmanViewModel = new ViewModelProvider(requireActivity()).get(HangmanViewModel.class);
        updateUi();
    }
    @Override
    public void initUi() {

    }

    private void updateUi(){
        hangmanViewModel.getPrintingIndex().observe(getViewLifecycleOwner(), printingIndex -> {
            if (printingIndex.peekContent() < hangmanViewModel.getPrintingIdListSize()){
                binding.printing.setImageResource(hangmanViewModel.getPrintingImageId());
            }
            Log.d("MyTAG", "그림 ui가 업데이트 됨");
        });
    }
}
