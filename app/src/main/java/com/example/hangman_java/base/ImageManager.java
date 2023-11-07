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

    private static List<Integer> alphabetImageList =
        List.of(R.drawable.enabled_a, R.drawable.enabled_b, R.drawable.enabled_c, R.drawable.enabled_d,
            R.drawable.enabled_e, R.drawable.enabled_f, R.drawable.enabled_g, R.drawable.enabled_h,
            R.drawable.enabled_i, R.drawable.enabled_j, R.drawable.enabled_k, R.drawable.enabled_l,
            R.drawable.enabled_m, R.drawable.enabled_n, R.drawable.enabled_o, R.drawable.enabled_p,
            R.drawable.enabled_q, R.drawable.enabled_r, R.drawable.enabled_s, R.drawable.enabled_t,
            R.drawable.enabled_u, R.drawable.enabled_v, R.drawable.enabled_w, R.drawable.enabled_x,
            R.drawable.enabled_y, R.drawable.enabled_z);

    private static int questionImage = R.drawable.question;
    private static int underbarImage = R.drawable.underbar;

    public List<Integer> getPrintingImageList() { return printingImageList; }
    public List<Integer> getAlphabetImageList(){
        return alphabetImageList;
    }

    public int getQuestionImage(){ return questionImage; }
    public int getUnderbarImage(){ return underbarImage; }
}