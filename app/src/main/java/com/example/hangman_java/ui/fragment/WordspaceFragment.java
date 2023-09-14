package com.example.hangman_java.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.databinding.FragmentWordspaceBinding;
import com.example.hangman_java.mvvm.viewmodel.GameViewModel;

public class WordspaceFragment extends BaseFragment{
    private FragmentWordspaceBinding binding = null;
    private GameViewModel gameViewModel = null;

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ){
        binding = FragmentWordspaceBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        updateUi();
    }

    @Override
    public void initUi() throws Exception {
        int wordLength = gameViewModel.getWordLength();
        GridLayout gridLayoutWordspace = binding.getRoot();
        LinearLayout.LayoutParams questionLayoutParams = gameViewModel.getLayoutParams("question");
        ViewGroup.LayoutParams underbarLayoutParams = gameViewModel.getLayoutParams("underbar");
        gridLayoutWordspace.setColumnCount(wordLength);
        gameViewModel.clearImageViewList();

        for (int i=0; i<2; i++){
            for (int j=0; j<wordLength; j++){
                ImageView newImageView = new ImageView(this.getContext());
                if (i==0){
                    newImageView.setImageResource(gameViewModel.getImageIdByTag("question"));
                    gameViewModel.addImageView(newImageView);
                    gridLayoutWordspace.addView(newImageView, questionLayoutParams);
                } else {
                    newImageView.setImageResource(gameViewModel.getImageIdByTag("underbar"));
                    gridLayoutWordspace.addView(newImageView, underbarLayoutParams);
                }
            }
        }

    }

    private void updateUi(){
        gameViewModel.getCorrectAlphabetIndexList().observe(getViewLifecycleOwner(), correctAlphabetIndexList -> {
            int imageId = gameViewModel.getAlphabetImageId(gameViewModel.getInputAlphabet());
            if (!correctAlphabetIndexList.isHasbeenHandled()){
                for (int index: correctAlphabetIndexList.getContentIfNotHandled()){
                    gameViewModel.getImageView(index).setImageResource(imageId);
                }
                Log.d("MyTAG", "단어 ui가 업데이트 됨");
            }
        });
    }
}
