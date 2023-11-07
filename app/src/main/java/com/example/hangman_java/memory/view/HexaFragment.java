package com.example.hangman_java.memory.view;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.databinding.FragmentHexaBinding;
import com.example.hangman_java.memory.viewmodel.MemoryViewModel;

import java.util.List;

public class HexaFragment extends BaseFragment {
    private FragmentHexaBinding binding;
    MemoryViewModel memoryViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHexaBinding.inflate(inflater, container, false);
        memoryViewModel = new ViewModelProvider(this).get(MemoryViewModel.class);  // ViewModelProvider를 사용하여 초기화
        memoryViewModel.setAnswerList();
        List<Integer> answer_list = memoryViewModel.getAnswerList();
        Log.d("testt", answer_list.toString());
        View view = binding.getRoot();
        Log.d("testt", "다잤음 ");
        initUi();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUi();
    }

    @Override
    public void initUi() {
        StartGame();
        Log.d("testt", "성공적으로 시작함 ");
    }

    private void StartGame() {
        Log.d("testt", "잤음");
        HexagonView[] hexagonViews = {
                binding.hexa1, binding.hexa2, binding.hexa3,
                binding.hexa4, binding.hexa5, binding.hexa6, binding.hexa7
        };
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = memoryViewModel.createAnimation();
                int answer_first = memoryViewModel.getFirstAnswer();
                hexagonViews[answer_first - 1].startAnimation(anim);
            }
        }, 3000);

        for (int i = 0; i < 7; i++){
            int finalI = i;
            hexagonViews[i].setOnClickListener(v -> memoryViewModel.setInputOrder(finalI + 1));
        }
    }

    private void updateUi() {
        memoryViewModel.InputOrder().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer number) {
                Log.d("testt", "변경 감지 ");
                // _InputOrder의 값이 변경될 때 호출되는 콜백 메서드
                // 변경된 값(number)을 이용한 처리를 여기에 추가
                Boolean output = memoryViewModel.InputOrderListener(number);
                if (output) {
                    Log.d("testt", "성공적으로 정답을 맞춤 ");
                    List<Integer> answer_list = memoryViewModel.getAnswerList();
                    HexagonView[] temp_hexagonViews = {
                            binding.hexa1, binding.hexa2, binding.hexa3,
                            binding.hexa4, binding.hexa5, binding.hexa6, binding.hexa7
                    };
                    Boolean stageCheckOutput = memoryViewModel.CheckNextStage();
                    if (stageCheckOutput) {
                        Log.d("testt", "클리어");
                        final long delay = 1000;
                        final Handler handler2 = new Handler();
                        final Animation[] animations = new Animation[memoryViewModel.getCurrentStage() + 1];
                        for (int i = 0; i < memoryViewModel.getCurrentStage() + 1; i++) {
                            animations[i] = memoryViewModel.createAnimation();
                        }
                        for (int i = 0; i < memoryViewModel.getCurrentStage() + 1; i++) {
                            final int index = i;
                            handler2.postDelayed(() -> {
                                Animation anim1 = animations[index];
                                temp_hexagonViews[answer_list.get(index) - 1].startAnimation(anim1);
                            }, i * delay);
                        }
                    } else {
                        //다음스테이지로 넘어가는게아니라 그냥 넘김

                    }
                }else{
                    //정답을 못맞췃을 때 로직
                }
            }
        });
    }
}