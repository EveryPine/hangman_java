package com.example.hangman_java.base;

import android.util.DisplayMetrics;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    protected abstract void initUi() throws Exception;

    public int convertToDP(int px){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(px * displayMetrics.density);
    }
}