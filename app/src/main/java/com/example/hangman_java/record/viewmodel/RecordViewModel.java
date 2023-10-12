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
    private MutableLiveData<Event<List<Record>>> _bestRecordList = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _recentRecordList = new MutableLiveData<>();
    // LiveData
    public final LiveData<Event<String>> getSelectedGame(){return this._selectedGame;}
    public final LiveData<Event<List<Record>>> bestRecordList(){return this._bestRecordList;}
    public final LiveData<Event<List<Record>>> recentRecordList(){return this._recentRecordList;}

    public void getBestRecord(Context context, String game){
        Runnable runnable = () -> {
            RecordDao recordDao = AppDatabase.getInstance(context).recordDao();
            _bestRecordList.postValue(new Event<>(recordDao.getBestRecord(game)));;
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void getRecentRecord(Context context, String game){
        Runnable runnable = () -> {
            RecordDao recordDao = AppDatabase.getInstance(context).recordDao();
            _recentRecordList.postValue(new Event<>(recordDao.getRecentRecord(game)));
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void setDisplayingGame(String game){
        _selectedGame.setValue(new Event<>(game));
    }
}
