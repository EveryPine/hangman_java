package com.example.hangman_java.memory.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.databinding.FragmentRectangleBinding;

import com.example.hangman_java.memory.viewmodel.MemoryViewModel;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.viewmodel.RecordViewModel;

import java.util.List;

public class RectangleFragment extends BaseFragment {
    private FragmentRectangleBinding binding;
    MemoryViewModel gameViewModel;
    private RecordViewModel recordViewModel = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding  = FragmentRectangleBinding.inflate(inflater,container,false);
        gameViewModel = new ViewModelProvider(requireActivity()).get(MemoryViewModel.class);
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);
        View view = binding.getRoot();
        gameViewModel.setDifficulty(4);
        return view;
    }
    public void setViewModel(MemoryViewModel viewModel) {
        this.gameViewModel = viewModel;
    }

    @Override
    public void initUi() throws InterruptedException {
        gameViewModel.setAnswerList();
        List<Integer> answer_list = gameViewModel.getAnswerList();
        gameViewModel.setSoundPool(getContext().getApplicationContext());
        Log.d("testt", answer_list.toString());
        StartGame();
        Log.d("testt", "성공적으로 시작함 ");
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (gameViewModel.getDifficulty() != 0) {
            try {
                initUi();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            updateUi();
        }
    }
    private void StartGame() {
        Log.d("testt", "잤음");
        View[] rectangleViews = {
                binding.rect1, binding.rect2, binding.rect3,
                binding.rect4
        };

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = gameViewModel.createAnimation();
                int answer_first = gameViewModel.getFirstAnswer();
                rectangleViews[answer_first - 1].startAnimation(anim);
                gameViewModel.playSound(1);
            }
        }, 3000);

        for (int i = 0; i < 4; i++){
            int finalI = i;
            rectangleViews[i].setOnClickListener(v -> gameViewModel.setInputOrder(finalI + 1));
        }
    }
    private void updateUi() {
        gameViewModel.InputOrder().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer number) {
                Log.d("testt", "변경 감지 ");
                // _InputOrder의 값이 변경될 때 호출되는 콜백 메서드
                // 변경된 값(number)을 이용한 처리를 여기에 추가
                Boolean output = gameViewModel.InputOrderListener(number);
                if (output) {
                    Log.d("testt", "성공적으로 정답을 맞춤 ");
                    List<Integer> answer_list = gameViewModel.getAnswerList();
                    View[] triangleViews = {
                            binding.rect1, binding.rect2, binding.rect3,
                            binding.rect4

                    };
                    gameViewModel.playSound(2);
                    Boolean stageCheckOutput = gameViewModel.CheckNextStage();
                    if (stageCheckOutput) {
                        binding.score.setText(String.valueOf(gameViewModel.getScore()));
                        Log.d("testt", "클리어");
                        final long delay = 1000;
                        final Handler handler2 = new Handler();
                        final Animation[] animations = new Animation[gameViewModel.getCurrentStage() + 1];
                        for (int i = 0; i < gameViewModel.getCurrentStage() + 1; i++) {
                            animations[i] = gameViewModel.createAnimation();
                        }
                        for (int i = 0; i < gameViewModel.getCurrentStage() + 1; i++) {
                            final int index = i;
                            handler2.postDelayed(() -> {
                                Animation anim1 = animations[index];
                                triangleViews[answer_list.get(index) - 1].startAnimation(anim1);
                                gameViewModel.playSound(1);
                            }, i * delay);
                        }
                    } else {
                        //다음스테이지로 넘어가는게아니라 그냥 넘김

                    }
                } else {
                    gameOver();
                    //정답을 못맞췃을 때 로직
                }
            }
        });
    }
    private void gameOver(){
        recordViewModel.insertRecord(requireContext(), new Record("memory","easy", gameViewModel.getScore()));
    }
}
