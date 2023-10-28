package com.example.hangman_java.debug.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hangman_java.base.BaseViewModel;
import com.example.hangman_java.record.model.Record;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class DebugViewModel extends BaseViewModel {
    private MutableLiveData<Record> _data = new MutableLiveData<>();
    private MutableLiveData<Integer> _difficulty = new MutableLiveData<>(0);
    public LiveData<Record> data() { return this._data; }

    public void genRandomData(){
        Random random = new Random();
        String[] gamenameArray = {"card", "hangman", "memory"};
        String[] difficultyArray = {"easy", "normal", "hard"};
        _data.setValue(new Record(gamenameArray[random.nextInt(3)], difficultyArray[random.nextInt(3)], random.nextInt(100)));
    }

    public Record getData(){
        return _data.getValue();
    }

    public int getDifficulty(){
        return _difficulty.getValue();
    }

    public String convertRecordToStr(List<Record> inputList){
        Iterator<Record> iterator = inputList.iterator();
        StringBuilder sb = new StringBuilder("게임 이름 / 난이도 / 기록 / 날짜\n");
        Record record;
        while (iterator.hasNext()){
            record = iterator.next();
            String gamename = String.format("%-10s ", record.gamename);
            String difficulty = String.format("%-10s ", record.difficulty);
            String recordStr = String.format("%-10d ", record.record);
            sb.append(gamename).append(difficulty).append(recordStr).append(record.date).append("\n");
        }
        return sb.toString();
    }

    public void setDifficulty(int difficulty){
        _difficulty.setValue(difficulty);
    }
}
