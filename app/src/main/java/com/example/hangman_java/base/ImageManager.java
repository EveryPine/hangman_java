package com.example.hangman_java.base;

import com.example.hangman_java.R;

import java.util.List;

public class ImageManager {
    private static ImageManager instance;

    public static ImageManager getInstance(){
        if (instance==null){
            synchronized (ImageManager.class){
                instance = new ImageManager();
            }
        }
        return instance;
    }

    private static List<Integer> printingImageList =
        List.of(0, R.drawable.hangman_print_01, R.drawable.hangman_print_02, R.drawable.hangman_print_03,
            R.drawable.hangman_print_04, R.drawable.hangman_print_05, R.drawable.hangman_print_06, R.drawable.hangman_print_07,
            R.drawable.hangman_print_08, R.drawable.hangman_print_09, R.drawable.hangman_print_10, R.drawable.hangman_print_11);

    private static int questionImage = R.drawable.hangman_question;
    private static int underbarImage = R.drawable.hangman_underbar;

    public List<Integer> getPrintingImageList() { return printingImageList; }

    public int getQuestionImage(){ return questionImage; }
    public int getUnderbarImage(){ return underbarImage; }
}