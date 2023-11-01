package com.example.hangman_java.card.controller;

import android.os.Handler;

import com.example.hangman_java.card.model.model;

import java.util.ArrayList;
import java.util.Collections;

public class logic {
    private boolean gameEnd = false;
    private model gameModel = null;
    private boolean touchEnabled = true;
    private ArrayList<Integer> checkli = null;
    private int check_idx = 0;

    public logic(model gameModel){
        this.gameModel = gameModel;
        this.initGame();
    }

    public boolean check(int num){
        if (touchEnabled) {
            if(num == checkli.get(check_idx)){
                plusIdx();
                gameModel.plusScore(num);// 최솟값 처리
                if(check_idx == checkli.size()) setGameEnd();
                return true;
            } else {
                disableTouch(); // 터치 비활성화
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enableTouch(); // 3초 후 터치 활성화
                    }
                }, 3000);
            }
        }return false;
    }
    public void disableTouch() {
        touchEnabled = false;
    }
    public void enableTouch() {
        touchEnabled = true;
    }
    public void plusIdx() {check_idx++;}
    public ArrayList<Integer> getData(){ return gameModel.getMap(); }
    public int getScore() {return gameModel.getScore();}
    public boolean checkGameEnd() {return gameEnd;}
    public void setGameEnd() {gameEnd = true;}
    public void initGame(){
        check_idx = 0;
        gameModel.initData();
        checkli = new ArrayList<>(gameModel.getMap());
        Collections.sort(checkli);
        gameEnd = false;
    }
}
