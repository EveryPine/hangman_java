package com.example.hangman_java.hangman.view;

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
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;
import com.example.hangman_java.base.BaseFragment;

public class WordspaceFragment extends BaseFragment {
    private FragmentWordspaceBinding binding = null;
    private HangmanViewModel hangmanViewModel = null;

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
        hangmanViewModel = new ViewModelProvider(requireActivity()).get(HangmanViewModel.class);
        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        updateUi();
    }

    @Override
    public void initUi() throws Exception {
        int wordLength = hangmanViewModel.getWordLength();
        GridLayout gridLayoutWordspace = binding.getRoot();
        LinearLayout.LayoutParams questionLayoutParams = hangmanViewModel.getLayoutParams("question");
        ViewGroup.LayoutParams underbarLayoutParams = hangmanViewModel.getLayoutParams("underbar");
        gridLayoutWordspace.setColumnCount(wordLength);
        hangmanViewModel.clearImageViewList();

        for (int i=0; i<2; i++){
            for (int j=0; j<wordLength; j++){
                ImageView newImageView = new ImageView(this.getContext());
                if (i==0){
                    newImageView.setImageResource(hangmanViewModel.getImageIdByTag("question"));
                    hangmanViewModel.addImageView(newImageView);
                    gridLayoutWordspace.addView(newImageView, questionLayoutParams);
                } else {
                    newImageView.setImageResource(hangmanViewModel.getImageIdByTag("underbar"));
                    gridLayoutWordspace.addView(newImageView, underbarLayoutParams);
                }
            }
        }

    }

    private void updateUi(){
        hangmanViewModel.getCorrectAlphabetIndexList().observe(getViewLifecycleOwner(), correctAlphabetIndexList -> {
            int imageId = hangmanViewModel.getAlphabetImageId(hangmanViewModel.getInputAlphabet());
            if (!correctAlphabetIndexList.isHasbeenHandled()){
                for (int index: correctAlphabetIndexList.getContentIfNotHandled()){
                    hangmanViewModel.getImageView(index).setImageResource(imageId);
                }
                Log.d("MyTAG", "단어 ui가 업데이트 됨");
            }
        });
    }
}
