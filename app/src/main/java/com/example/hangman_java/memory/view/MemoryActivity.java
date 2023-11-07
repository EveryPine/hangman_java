package com.example.hangman_java.memory.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.hangman_java.R;
import com.example.hangman_java.databinding.ActivityMemoryBinding;

public class MemoryActivity extends AppCompatActivity {
    private ActivityMemoryBinding binding;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        HexaFragment hexaFragment = new HexaFragment();
        transaction.replace(R.layout.fragment_hexa,hexaFragment);

    }
}