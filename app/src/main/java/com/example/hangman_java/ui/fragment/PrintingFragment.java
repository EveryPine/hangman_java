package com.example.hangman_java.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.databinding.FragmentPrintingBinding;
import com.example.hangman_java.mvvm.viewmodel.GameViewModel;

public class PrintingFragment extends BaseFragment {
    private FragmentPrintingBinding binding = null;
    private GameViewModel gameViewModel = null;

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
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        updateUi();
    }
    @Override
    void initUi() {

    }

    private void updateUi(){
        gameViewModel.getPrintingIndex().observe(getViewLifecycleOwner(), printingIndex -> {
            if (printingIndex.peekContent() < gameViewModel.getPrintingIdListSize()){
                binding.printing.setImageResource(gameViewModel.getPrintingImageId());
            }
            Log.d("MyTAG", "그림 ui가 업데이트 됨");
        });
    }
}
