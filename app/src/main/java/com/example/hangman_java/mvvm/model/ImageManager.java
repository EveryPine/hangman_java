package com.example.hangman_java.mvvm.model;

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

    private static List<Integer> printingEasyImageList =
        List.of(R.drawable.empty_printing, R.drawable.easy_1, R.drawable.easy_2, R.drawable.easy_3,
            R.drawable.easy_4, R.drawable.easy_5, R.drawable.easy_6, R.drawable.easy_7,
            R.drawable.easy_8, R.drawable.easy_9, R.drawable.easy_10, R.drawable.easy_11,
            R.drawable.easy_12, R.drawable.easy_13, R.drawable.easy_14, R.drawable.easy_15);

    private static List<Integer> printingNormalImageList =
        List.of(R.drawable.empty_printing, R.drawable.normal_1, R.drawable.normal_2, R.drawable.normal_3,
            R.drawable.normal_4, R.drawable.normal_5, R.drawable.normal_6, R.drawable.normal_7,
            R.drawable.normal_8, R.drawable.normal_9, R.drawable.normal_10);

    private static List<Integer> printingHardImageList =
        List.of(R.drawable.empty_printing, R.drawable.hard_1, R.drawable.hard_2,
            R.drawable.hard_3, R.drawable.hard_4, R.drawable.hard_5);

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

    public List<Integer> getPrintingEasyImageList(){
        return printingEasyImageList;
    }

    public List<Integer> getPrintingNormalImageList(){
        return printingNormalImageList;
    }

    public List<Integer> getPrintingHardImageList(){
        return printingHardImageList;
    }

    public List<Integer> getAlphabetImageList(){
        return alphabetImageList;
    }

    public int getQuestionImage(){ return questionImage; }
    public int getUnderbarImage(){ return underbarImage; }
}