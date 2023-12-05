package com.example.hangman_java.memory.view;

import static com.example.hangman_java.main.viewmodel.MainViewModel.DELAY_TIME;

import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.R;
import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.base.EventObserver;
import com.example.hangman_java.databinding.FragmentRectangleBinding;

import com.example.hangman_java.main.view.SettingDialog;
import com.example.hangman_java.memory.viewmodel.MemoryViewModel;
import com.example.hangman_java.music.SfxManager;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.viewmodel.RecordViewModel;

import java.util.List;

public class RectangleFragment extends BaseFragment {
    private FragmentRectangleBinding binding;
    MemoryViewModel memoryViewModel;
    private Handler handler;
    private RecordViewModel recordViewModel = null;
    public static SoundPool soundPool;
    SfxManager sfxManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding  = FragmentRectangleBinding.inflate(inflater,container,false);
        memoryViewModel = new ViewModelProvider(requireActivity()).get(MemoryViewModel.class);
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);
        soundPool = new SoundPool.Builder().build();
        sfxManager  = new SfxManager(requireContext(),soundPool);
        sfxManager.addSound("correct", R.raw.memory_correct_sound1);
        sfxManager.addSound("sound1",R.raw.memory_sound1);
        View view = binding.getRoot();
        memoryViewModel.setDifficulty(4);
        return view;
    }
    public void setViewModel(MemoryViewModel viewModel) {
        this.memoryViewModel = viewModel;
    }

    @Override
    public void initUi() throws InterruptedException {
        recordViewModel.getBestScore(requireContext(), "memory", String.valueOf(memoryViewModel.getDifficulty()));
        recordViewModel.bestScore().observe(requireActivity(), new EventObserver<>(bestScore -> {
            memoryViewModel.setBestScore(bestScore);
        }));
        memoryViewModel.setAnswerList();
        List<Integer> answer_list = memoryViewModel.getAnswerList();
        //gameViewModel.setSoundPool(getContext().getApplicationContext());
        Log.d("testt", answer_list.toString());
        StartGame();
        Log.d("testt", "성공적으로 시작함 ");
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (memoryViewModel.getDifficulty() != 0) {
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
        binding.btnPause.setOnClickListener(new PauseListener());
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = memoryViewModel.createAnimation();
                int answer_first = memoryViewModel.getFirstAnswer();
                rectangleViews[answer_first - 1].startAnimation(anim);
                sfxManager.playSound("sound1");
            }
        }, 3000);

        for (int i = 0; i < 4; i++){
            int finalI = i;
            rectangleViews[i].setOnClickListener(v -> memoryViewModel.setInputOrder(finalI + 1));
        }
    }
    private void updateUi() {
        handler = new Handler();
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
                Log.d("testt", "변경 감지 ");
                // _InputOrder의 값이 변경될 때 호출되는 콜백 메서드
                // 변경된 값(number)을 이용한 처리를 여기에 추가
                Boolean output = memoryViewModel.InputOrderListener(number);
                if (output) {
                    Log.d("testt", "성공적으로 정답을 맞춤 ");
                    List<Integer> answer_list = memoryViewModel.getAnswerList();
                    View[] triangleViews = {
                            binding.rect1, binding.rect2, binding.rect3,
                            binding.rect4

                    };
                    sfxManager.playSound("correct");
                    Boolean stageCheckOutput = memoryViewModel.CheckNextStage();
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
                                Animation anim1 = animations[index];
                                triangleViews[answer_list.get(index) - 1].startAnimation(anim1);
                                sfxManager.playSound("sound1");
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
