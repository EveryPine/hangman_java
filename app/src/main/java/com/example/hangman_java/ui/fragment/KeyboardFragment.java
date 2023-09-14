package com.example.hangman_java.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import com.example.hangman_java.databinding.FragmentKeyboardBinding;
import com.example.hangman_java.mvvm.viewmodel.GameViewModel;

import java.util.List;

public class KeyboardFragment extends BaseFragment{
    private FragmentKeyboardBinding binding = null;
    private GameViewModel gameViewModel = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        binding = FragmentKeyboardBinding.inflate(inflater, container, false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        initUi();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        setButtonListener();
    }
    @Override
    void initUi() {

    }

    private void setButtonListener(){
        List<ImageButton> btnList = List.of(binding.imageBtnA, binding.imageBtnB, binding.imageBtnC,
            binding.imageBtnD, binding.imageBtnE, binding.imageBtnF,
            binding.imageBtnG, binding.imageBtnH, binding.imageBtnI,
            binding.imageBtnJ, binding.imageBtnK, binding.imageBtnL,
            binding.imageBtnM, binding.imageBtnN, binding.imageBtnO,
            binding.imageBtnP, binding.imageBtnQ, binding.imageBtnR,
            binding.imageBtnS, binding.imageBtnT, binding.imageBtnU,
            binding.imageBtnV, binding.imageBtnW, binding.imageBtnU,
            binding.imageBtnX, binding.imageBtnY, binding.imageBtnZ);
        for (ImageButton button: btnList){
            button.setOnClickListener(view -> {
                char alphabet = '0';
                if (binding.imageBtnA.equals(view)) {
                    alphabet = 'a';
                } else if (binding.imageBtnB.equals(view)) {
                    alphabet = 'b';
                } else if (binding.imageBtnC.equals(view)) {
                    alphabet = 'c';
                } else if (binding.imageBtnD.equals(view)) {
                    alphabet = 'd';
                } else if (binding.imageBtnE.equals(view)) {
                    alphabet = 'e';
                } else if (binding.imageBtnF.equals(view)) {
                    alphabet = 'f';
                } else if (binding.imageBtnG.equals(view)) {
                    alphabet = 'g';
                } else if (binding.imageBtnH.equals(view)) {
                    alphabet = 'h';
                } else if (binding.imageBtnI.equals(view)) {
                    alphabet = 'i';
                } else if (binding.imageBtnJ.equals(view)) {
                    alphabet = 'j';
                } else if (binding.imageBtnK.equals(view)) {
                    alphabet = 'k';
                } else if (binding.imageBtnL.equals(view)) {
                    alphabet = 'l';
                } else if (binding.imageBtnM.equals(view)) {
                    alphabet = 'm';
                } else if (binding.imageBtnN.equals(view)) {
                    alphabet = 'n';
                } else if (binding.imageBtnO.equals(view)) {
                    alphabet = 'o';
                } else if (binding.imageBtnP.equals(view)) {
                    alphabet = 'p';
                } else if (binding.imageBtnQ.equals(view)) {
                    alphabet = 'q';
                } else if (binding.imageBtnR.equals(view)) {
                    alphabet = 'r';
                } else if (binding.imageBtnS.equals(view)) {
                    alphabet = 's';
                } else if (binding.imageBtnT.equals(view)) {
                    alphabet = 't';
                } else if (binding.imageBtnU.equals(view)) {
                    alphabet = 'u';
                } else if (binding.imageBtnV.equals(view)) {
                    alphabet = 'v';
                } else if (binding.imageBtnW.equals(view)) {
                    alphabet = 'w';
                } else if (binding.imageBtnX.equals(view)) {
                    alphabet = 'x';
                } else if (binding.imageBtnY.equals(view)) {
                    alphabet = 'y';
                } else if (binding.imageBtnZ.equals(view)) {
                    alphabet = 'z';
                }

                if (alphabet!='0'){
                    buttonDisabled((ImageButton) view);
                    gameViewModel.inputAlphabetListener(alphabet);
                } else {
                    Log.e("MyTAG", "KeyboardFragment의 alphabet에 잘못된 값이 참조되었습니다.");
                }
            });
        }
    }

    private void buttonDisabled(ImageButton button){
        button.setVisibility(View.INVISIBLE);
        button.setClickable(false);
    }
}
