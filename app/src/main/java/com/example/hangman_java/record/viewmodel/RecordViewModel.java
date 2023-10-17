package com.example.hangman_java.record.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hangman_java.base.AppDatabase;
import com.example.hangman_java.base.BaseViewModel;
import com.example.hangman_java.base.Event;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.model.RecordDao;

import java.util.List;

public class RecordViewModel extends BaseViewModel {
    // MutableLiveData
    private MutableLiveData<Event<String>> _selectedGame = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _cardBestRecordList = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _hangmanBestRecordList = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _memoryBestRecordList = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _cardRecentRecordList = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _hangmanRecentRecordList = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _memoryRecentRecordList = new MutableLiveData<>();
    // LiveData
    public final LiveData<Event<String>> getSelectedGame(){return this._selectedGame;}
    public final LiveData<Event<List<Record>>> cardBestRecordList(){return this._cardBestRecordList;}
    public final LiveData<Event<List<Record>>> hangmanBestRecordList(){return this._hangmanBestRecordList;}
    public final LiveData<Event<List<Record>>> memoryBestRecordList(){return this._memoryBestRecordList;}
    public final LiveData<Event<List<Record>>> cardRecentRecordList(){return this._cardRecentRecordList;}
    public final LiveData<Event<List<Record>>> hangmanRecentRecordList(){return this._hangmanRecentRecordList;}
    public final LiveData<Event<List<Record>>> memoryRecentRecordList(){return this._memoryRecentRecordList;}

    public void getBestRecord(Context context, String game){
        Runnable runnable = () -> {
            RecordDao recordDao = AppDatabase.getInstance(context).recordDao();
            switch (game){
                case "card" -> _cardBestRecordList.postValue(new Event<>(recordDao.getBestRecord(game)));
                case "hangman" -> _hangmanBestRecordList.postValue(new Event<>(recordDao.getBestRecord(game)));
                case "memory" -> _memoryBestRecordList.postValue(new Event<>(recordDao.getBestRecord(game)));
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void getRecentRecord(Context context, String game){
        Runnable runnable = () -> {
            RecordDao recordDao = AppDatabase.getInstance(context).recordDao();
            switch (game){
                case "card" -> _cardRecentRecordList.postValue(new Event<>(recordDao.getRecentRecord(game)));
                case "hangman" -> _hangmanRecentRecordList.postValue(new Event<>(recordDao.getRecentRecord(game)));
                case "memory" -> _memoryRecentRecordList.postValue(new Event<>(recordDao.getRecentRecord(game)));
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void setDisplayingGame(String game){
        _selectedGame.setValue(new Event<>(game));
    }

    public void updateBestRecord(Record record){

    }

    public void updateRecentRecord(Record record){

    }
}
