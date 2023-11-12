package com.example.hangman_java.memory.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.hangman_java.R;
import com.example.hangman_java.databinding.ActivityMemoryBinding;
import com.example.hangman_java.memory.viewmodel.MemoryViewModel;

public class MemoryActivity extends AppCompatActivity {
    private ActivityMemoryBinding binding;
    MemoryViewModel gameViewModel;

    private int difficulty;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent =getIntent();
        if (intent != null) {
            // Get the integer extra
            difficulty  = intent.getIntExtra("difficulty",0);
            Log.d("testt","value"+difficulty);
        }else{
            Log.d("testt","value"+difficulty);
        }

        switch (difficulty){
            case 0:{
                RectangleFragment rectangleFragment = new RectangleFragment();
                gameViewModel = new ViewModelProvider(this).get(MemoryViewModel.class);
                gameViewModel.setDifficulty(4);
                transaction.replace(R.id.fragment_container, rectangleFragment);
                break;
            }
            case 1:{
                HexaFragment hexaFragment = new HexaFragment();
                gameViewModel = new ViewModelProvider(this).get(MemoryViewModel.class);
                gameViewModel.setDifficulty(7);
                hexaFragment.setViewModel(gameViewModel);
                transaction.replace(R.id.fragment_container, hexaFragment);
                break;
            }
            case 2:{
                TriangleFragment triangleFragment = new TriangleFragment();
                gameViewModel = new ViewModelProvider(this).get(MemoryViewModel.class);
                gameViewModel.setDifficulty(9);
                triangleFragment.setViewModel(gameViewModel);
                transaction.replace(R.id.fragment_container, triangleFragment);
                break;
            }
            default:{
                break;
            }
        }
        transaction.commit();

    }
}