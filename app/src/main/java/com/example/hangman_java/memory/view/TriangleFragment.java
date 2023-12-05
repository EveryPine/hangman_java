package com.example.hangman_java.memory.view;

import static com.example.hangman_java.main.viewmodel.MainViewModel.DELAY_TIME;

import android.content.Intent;
import android.media.SoundPool;
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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.base.EventObserver;
import com.example.hangman_java.databinding.FragmentTriangleBinding;

import com.example.hangman_java.hangman.view.PauseDialog;
import com.example.hangman_java.hangman.view.ResultDialog;
import com.example.hangman_java.main.view.SettingDialog;
import com.example.hangman_java.memory.viewmodel.MemoryViewModel;
import com.example.hangman_java.music.SfxManager;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.viewmodel.RecordViewModel;
import com.jetradarmobile.snowfall.SnowfallView;

import java.util.List;

public class TriangleFragment extends BaseFragment {
    private FragmentTriangleBinding binding;

    MemoryViewModel memoryViewModel;
    private Handler handler;
    public static SoundPool soundPool;
    private RecordViewModel recordViewModel = null;
    SfxManager sfxManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTriangleBinding.inflate(inflater, container, false);
        memoryViewModel = new ViewModelProvider(requireActivity()).get(MemoryViewModel.class);
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);
        soundPool = new SoundPool.Builder().build();
        sfxManager  = new SfxManager(requireContext(),soundPool);
        sfxManager.addSound("correct",R.raw.memory_correct_sound1);
        sfxManager.addSound("sound1",R.raw.memory_sound1);
        View view = binding.getRoot();
        memoryViewModel.setDifficulty(9);
        return view;
    }

    public void setViewModel(MemoryViewModel viewModel) {
        this.memoryViewModel = viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (memoryViewModel.getDifficulty() != 0) {
            initUi();
            updateUi();
        }
    }

    @Override
    public void initUi() {
        recordViewModel.getBestScore(requireContext(), "memory", String.valueOf(memoryViewModel.getDifficulty()));
        recordViewModel.bestScore().observe(requireActivity(), new EventObserver<>(bestScore -> {
            memoryViewModel.setBestScore(bestScore);
        }));
        memoryViewModel.setAnswerList();
        //memoryViewModel.setSoundPool(getContext().getApplicationContext());
        List<Integer> answer_list = memoryViewModel.getAnswerList();
        Log.d("testt", answer_list.toString());
        StartGame();
        Log.d("testt", "성공적으로 시작함 ");
    }

    private void StartGame() {
        View[] triangleViews = {
                binding.tri1, binding.tri2, binding.tri3,
                binding.tri4, binding.tri5, binding.tri6, binding.tri7, binding.tri8, binding.tri9
        };
        binding.btnPause.setOnClickListener(new PauseListener());
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                sfxManager.playSound("sound1");
                Animation anim = memoryViewModel.createAnimation();
                int answer_first = memoryViewModel.getFirstAnswer();
                triangleViews[answer_first - 1].startAnimation(anim);
            }
        }, 3000);
        for (int i = 0; i < 9; i++){
            int finalI = i;
            triangleViews[i].setOnClickListener(v -> memoryViewModel.setInputOrder(finalI + 1));
        }

    }

    private void updateUi() {
        memoryViewModel.onDialog().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (memoryViewModel.get_onDialog() == false){
                    binding.snowing.stopFalling();
                }else if(memoryViewModel.get_onDialog() == true){
                    binding.snowing.restartFalling();
                }
            }
        });
        memoryViewModel.InputOrder().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer number) {
                handler = new Handler();
                Log.d("testt", "변경 감지 ");
                // _InputOrder의 값이 변경될 때 호출되는 콜백 메서드
                // 변경된 값(number)을 이용한 처리를 여기에 추가
                Boolean output = memoryViewModel.InputOrderListener(number);
                if (output) {
                    Log.d("testt", "성공적으로 정답을 맞춤 ");
                    List<Integer> answer_list = memoryViewModel.getAnswerList();
                    View[] triangleViews = {
                            binding.tri1, binding.tri2, binding.tri3,
                            binding.tri4, binding.tri5, binding.tri6, binding.tri7, binding.tri8, binding.tri9
                    };
                    Boolean stageCheckOutput = memoryViewModel.CheckNextStage();
                    sfxManager.playSound("correct");
                    if (stageCheckOutput) {
                        binding.score.setText(String.valueOf(memoryViewModel.getScore()));
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
                                sfxManager.playSound("sound1");
                                Animation anim1 = animations[index];
                                triangleViews[answer_list.get(index) - 1].startAnimation(anim1);
                            }, i * delay);
                        }
                    } else {
                        //다음스테이지로 넘어가는게아니라 그냥 넘김
                    }
                } else {
                    gameOver(handler);
                    //정답을 못맞췃을 때 로직
                }
            }
        });
    }
    private void gameOver(Handler handler) {
        recordViewModel.insertRecord(requireContext(), new Record("memory","hard", memoryViewModel.getScore()));
        handler.postDelayed(() -> {
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            MemoryResultDialog resultDialog = new MemoryResultDialog();
            resultDialog.show(fm, "test");
        }, 2000);
    }
    private class PauseListener implements View.OnClickListener {
        public PauseListener() {

        }
        @Override
        public void onClick(View view) {
            sfxManager.playSound("sys_button");
            Log.d("testt", "add");
            // 눈 내리기를 멈춤
            memoryViewModel.set_onDialog(false);
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            MemoryPauseDialog pauseDialog = new MemoryPauseDialog();
            pauseDialog.show(fm, "test");

        }
    }

}
