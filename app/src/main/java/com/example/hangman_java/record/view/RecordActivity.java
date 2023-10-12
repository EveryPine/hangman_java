package com.example.hangman_java.record.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.example.hangman_java.databinding.ActivityRecordBinding;
import com.example.hangman_java.base.BaseActivity;
import com.example.hangman_java.record.view.RecHangmanFragment;

public class RecordActivity extends BaseActivity {
    private ActivityRecordBinding recordBinding = null;
    private FragmentContainerView fcRecord = null;
    private FragmentManager fragmentManager;
    private Fragment frGame1, frGame2, frGame3;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        recordBinding = ActivityRecordBinding.inflate(getLayoutInflater());
        fcRecord = recordBinding.fcRecord;
        fragmentManager = getSupportFragmentManager();

        try {
            initUi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setContentView(recordBinding.getRoot());
    }
    @Override
    public void initUi() throws Exception {
        setView();
    }

    public void setView(){
        recordBinding.imgBtnGame1.setOnClickListener(view -> {
            // fcRecord.setVisibility(View.VISIBLE);
        });

        recordBinding.imgBtnGame2.setOnClickListener(view -> {
            if (frGame2==null){
                frGame2 = new RecHangmanFragment();
                fragmentManager.beginTransaction().add(fcRecord.getId(), frGame2).commit();
            }

            if (frGame1!=null) fragmentManager.beginTransaction().hide(frGame1).commit();
            if (frGame2!=null) fragmentManager.beginTransaction().show(frGame2).commit();
            if (frGame3!=null) fragmentManager.beginTransaction().hide(frGame3).commit();

        });

        recordBinding.imgBtnGame3.setOnClickListener(view -> {

        });
    }
}
