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
        List.of(R.drawable.print_0, R.drawable.print_1, R.drawable.print_2, R.drawable.print_3,
            R.drawable.print_4, R.drawable.print_5, R.drawable.print_6, R.drawable.print_7,
            R.drawable.print_8, R.drawable.print_9, R.drawable.print_10, R.drawable.print_11);

    private static int questionImage = R.drawable.question;
    private static int underbarImage = R.drawable.underbar;

    public List<Integer> getPrintingImageList() { return printingImageList; }

    public int getQuestionImage(){ return questionImage; }
    public int getUnderbarImage(){ return underbarImage; }
}