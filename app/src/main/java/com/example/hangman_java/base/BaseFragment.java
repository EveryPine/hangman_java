package com.example.hangman_java.base;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    protected abstract void initUi() throws Exception;
}