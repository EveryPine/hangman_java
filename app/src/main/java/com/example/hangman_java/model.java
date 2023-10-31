package com.example.hangman_java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class model {
    private int gameLevel;
    private int score = 0;
    private int time = 30;

    private ArrayList<Integer> map;

    model(int level){
        gameLevel = level;
        map = new ArrayList<>(gameLevel*gameLevel);
        this.initData();
    }

    public void initData(){
        Set<Integer> set = new HashSet<>();

        while (set.size() < gameLevel*gameLevel) {
            set.add(new Random().nextInt(156));
        }

        map = new ArrayList<>(set);
    }

    public int getData(int idx) { return map.get(idx); }
    public ArrayList<Integer> getMap() { return map; }
    public int getScore() { return score; }
    public void plusScore(int num) {score += num;}
}