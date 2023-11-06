package com.example.hangman_java.hangman.view;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.base.EventObserver;
import com.example.hangman_java.databinding.FragmentWordspaceBinding;
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;

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
        hangmanViewModel.word().observe(getViewLifecycleOwner(), new EventObserver<>(word -> {
            Log.d("MyTAG", "목표 단어 설정됨: " + word);
            GridLayout layout = binding.getRoot();
            int wordLength = hangmanViewModel.getWordLength();
            layout.removeAllViews();
            layout.setColumnCount(wordLength);
            LinearLayout.LayoutParams questionLayoutParams = new LinearLayout.LayoutParams(60, 70); // '?' 이미지 레이아웃 파라미터
            LinearLayout.LayoutParams underbarLayoutParams = new LinearLayout.LayoutParams(55, 20); // '_' 이미지 레이아웃 파라미터
            underbarLayoutParams.gravity = Gravity.CENTER;
            Log.d("MyTAG", "단어의 길이: " + wordLength);
            hangmanViewModel.clearImageViewList();
            hangmanViewModel.updateRemainingAlphabetCount();

            for (int i=0; i<2; i++){
                for (int j=0; j<wordLength; j++){
                    ImageView newImageView = new ImageView(this.getContext());
                    if (i==0){
                        newImageView.setImageResource(hangmanViewModel.getImageIdByTag("question"));
                        hangmanViewModel.addImageView(newImageView);
                        layout.addView(newImageView, questionLayoutParams);
                    } else {
                        newImageView.setImageResource(hangmanViewModel.getImageIdByTag("underbar"));
                        layout.addView(newImageView, underbarLayoutParams);
                    }
                }
            }
            Log.d("MyTAG", "WordspaceFragment 초기화 완료");
        }));


    }

    private void updateUi(){
        hangmanViewModel.correctAlphabetIndexList().observe(getViewLifecycleOwner(), new EventObserver<>(list -> {
            int imageId = hangmanViewModel.getAlphabetImageId(hangmanViewModel.getInputAlphabet());
            for (int index: list)
                hangmanViewModel.getImageView(index).setImageResource(imageId);
        }));
    }
}
