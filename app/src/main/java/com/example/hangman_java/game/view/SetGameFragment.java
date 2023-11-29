package com.example.hangman_java.game.view;

import static com.example.hangman_java.main.view.MainActivity.soundPool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman_java.base.BaseFragment;
import com.example.hangman_java.databinding.FragmentSetgameBinding;
import com.example.hangman_java.game.viewmodel.GameViewModel;
import com.example.hangman_java.music.SfxManager;

public class SetGameFragment extends BaseFragment{
    private FragmentSetgameBinding setgameBinding = null;
    private GameViewModel gameViewModel = null;
    private SfxManager sfxManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        setgameBinding = FragmentSetgameBinding.inflate(inflater, container, false);
        sfxManager = new SfxManager(requireContext(), soundPool);
        return setgameBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void initUi() throws Exception {
        setView();
    }

    private void setView(){
        setgameBinding.btnCard.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            gameViewModel.setGame("card");
        });
        setgameBinding.btnHangman.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            gameViewModel.setGame("hangman");
        });
        setgameBinding.btnMemory.setOnClickListener(view -> {
            sfxManager.playSound("sys_button");
            gameViewModel.setGame("memory");
        });
    }
}
