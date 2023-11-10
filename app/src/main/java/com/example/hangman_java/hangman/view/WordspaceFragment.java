package com.example.hangman_java.hangman.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.base.EventObserver;
import com.example.hangman_java.databinding.FragmentWordspaceBinding;
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;

import org.w3c.dom.Text;

public class WordspaceFragment extends BaseFragment {
    private FragmentWordspaceBinding wordspaceBinding = null;
    private HangmanViewModel hangmanViewModel = null;

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ){
        wordspaceBinding = FragmentWordspaceBinding.inflate(inflater, container, false);

        return wordspaceBinding.getRoot();
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
            ((HangmanActivity) getActivity()).setWordDebug(word);
            GridLayout layout = wordspaceBinding.getRoot();
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
                    if (i==0){
                        TextView textView = new TextView(this.getContext());
                        setTextViewProperties(textView);
                        hangmanViewModel.addTextView(textView);
                        layout.addView(textView, questionLayoutParams);
                    } else {
                        ImageView imageView = new ImageView(this.getContext());
                        imageView.setImageResource(hangmanViewModel.getImageIdByTag("underbar"));
                        layout.addView(imageView, underbarLayoutParams);
                    }
                }
            }
            Log.d("MyTAG", "WordspaceFragment 초기화 완료");
        }));


    }

    private void updateUi(){
        hangmanViewModel.correctAlphabetIndexList().observe(getViewLifecycleOwner(), new EventObserver<>(list -> {
            String alphabet = String.valueOf(Character.toUpperCase(hangmanViewModel.getInputAlphabet()));
            for (int index: list){
                TextView textView = hangmanViewModel.getTextView(index);
                textView.setBackgroundColor(Color.TRANSPARENT);
                textView.setText(alphabet);
            }
        }));
    }

    private void setTextViewProperties(TextView textView){
        Typeface font = ResourcesCompat.getFont(requireContext(), R.font.alphabet_font);
        textView.setBackgroundResource(hangmanViewModel.getImageIdByTag("question"));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(30);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(font);
    }
}
