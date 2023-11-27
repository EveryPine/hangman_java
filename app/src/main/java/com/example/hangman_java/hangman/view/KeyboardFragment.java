package com.example.hangman_java.hangman.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.databinding.FragmentKeyboardBinding;
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;

import java.util.List;

public class KeyboardFragment extends BaseFragment {
    private FragmentKeyboardBinding binding = null;
    private HangmanViewModel hangmanViewModel = null;
    private List<FrameLayout> frList;
    private List<Button> btnList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        binding = FragmentKeyboardBinding.inflate(inflater, container, false);
        hangmanViewModel = new ViewModelProvider(requireActivity()).get(HangmanViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        hangmanViewModel = new ViewModelProvider(requireActivity()).get(HangmanViewModel.class);
        initUi();
    }
    @Override
    public void initUi() {
        setButtonListener();
    }

    private void setButtonListener(){
        frList = List.of(binding.frA, binding.frB, binding.frC,
            binding.frD, binding.frE, binding.frF,
            binding.frG, binding.frH, binding.frI,
            binding.frJ, binding.frK, binding.frL,
            binding.frM, binding.frN, binding.frO,
            binding.frP, binding.frQ, binding.frR,
            binding.frS, binding.frT, binding.frU,
            binding.frV, binding.frW, binding.frX, binding.frY, binding.frZ);

        btnList = List.of(binding.btnA, binding.btnB, binding.btnC,
            binding.btnD, binding.btnE, binding.btnF,
            binding.btnG, binding.btnH, binding.btnI,
            binding.btnJ, binding.btnK, binding.btnL,
            binding.btnM, binding.btnN, binding.btnO,
            binding.btnP, binding.btnQ, binding.btnR,
            binding.btnS, binding.btnT, binding.btnU,
            binding.btnV, binding.btnW, binding.btnX, binding.btnY, binding.btnZ);

        for (FrameLayout frame: frList){
            frame.setOnClickListener(new ClickListener());
        }

        for (Button btn: btnList){
            btn.setOnClickListener(new ClickListener());
        }
    }

    private class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Log.d("MyTAG", "알파벳 버튼 클릭 이벤트 발생");
            char alphabet = '0';
            int index;
            for (index = 0; index < 26; index++){
                if (frList.get(index).equals(view) || btnList.get(index).equals(view)) {
                    alphabet = (char) ('a' + index);
                    Log.d("MyTAG", "들어온 알파벳 입력: " + alphabet);
                    break;
                }
            }

            if (alphabet!='0'){
                buttonDisabled(frList.get(index));
                buttonDisabled(btnList.get(index));
                hangmanViewModel.inputAlphabetListener(alphabet);
            } else {
                Log.e("MyTAG", "KeyboardFragment의 alphabet에 잘못된 값이 참조되었습니다.");
            }
        }
    }

    private void buttonDisabled(View view){
        view.setEnabled(false);
        view.setClickable(false);
    }

    protected void setViewUnclickable(){
        for (int i = 0; i < 26; i++){
            if (frList.get(i).isClickable()){
                frList.get(i).setClickable(false);
                btnList.get(i).setClickable(false);
            }
        }
    }
}
