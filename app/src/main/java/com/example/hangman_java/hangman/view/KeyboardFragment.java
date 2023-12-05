package com.example.hangman_java.hangman.view;

import static com.example.hangman_java.main.view.MainActivity.soundPool;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.databinding.FragmentKeyboardBinding;
import com.example.hangman_java.hangman.viewmodel.HangmanViewModel;
import com.example.hangman_java.music.SfxManager;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFragment extends BaseFragment {
    private FragmentKeyboardBinding binding = null;
    private HangmanViewModel hangmanViewModel = null;
    private SfxManager sfxManager;
    private List<FrameLayout> frList;
    private List<Button> btnList;
    private List<Boolean> isClicked = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        binding = FragmentKeyboardBinding.inflate(inflater, container, false);
        sfxManager = new SfxManager(requireContext(), soundPool);
        sfxManager.addSound("alphabet_button", R.raw.hangman_btn_alphabet);
        sfxManager.addSound("disabled_button", R.raw.hangman_blip);

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

        for (int i = 0; i < 26; i++)
            isClicked.add(false);

        for (FrameLayout frame: frList){
            frame.setOnClickListener(new ClickListener());
        }

        for (Button btn: btnList){
            btn.setOnClickListener(new ClickListener());
        }
    }

    protected void setViewUnclickable(){
        for (int i = 0; i < 26; i++){
            frList.get(i).setClickable(false);
            btnList.get(i).setClickable(false);
        }
    }

    private class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            sfxManager.playSound("alphabet_button");
            Log.d("KeyboardFragment", "알파벳 버튼 클릭 이벤트 발생");
            char alphabet = '0';
            int index;
            for (index = 0; index < 26; index++){
                if (frList.get(index).equals(view) || btnList.get(index).equals(view)) {
                    alphabet = (char) ('a' + index);
                    Log.d("KeyboardFragment", "들어온 알파벳 입력: " + alphabet);
                    break;
                }
            }
            if (isClicked.get(index)){
                sfxManager.playSound("disabled_button");
                return;
            }

            if (alphabet!='0'){
                btnList.get(index).setBackgroundResource(R.drawable.hangman_alphabet_disabled);
                btnList.get(index).setTextColor(Color.GRAY);
                isClicked.set(index, true);
                hangmanViewModel.inputAlphabetListener(alphabet);
            } else {
                Log.e("KeyboardFragment", "alphabet에 잘못된 값이 참조되었습니다.");
            }
        }
    }
}