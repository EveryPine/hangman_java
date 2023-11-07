package com.example.hangman_java.record.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hangman_java.base.AppDatabase;
import com.example.hangman_java.base.BaseViewModel;
import com.example.hangman_java.base.Event;
import com.example.hangman_java.record.model.BestRecord;
import com.example.hangman_java.record.model.ReceivedRecord;
import com.example.hangman_java.record.model.RecentRecord;
import com.example.hangman_java.record.model.Record;
import com.example.hangman_java.record.model.RecordDao;

import java.util.List;

public class RecordViewModel extends BaseViewModel {
    private final int RECENTRECORD_MAXLENGTH = 10;
    // MutableLiveData
    private MutableLiveData<Event<String>> _selectedGame = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _debugRecordList = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _cardBestRecordList = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _hangmanBestRecordList = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _memoryBestRecordList = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _cardRecentRecordList = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _hangmanRecentRecordList = new MutableLiveData<>();
    private MutableLiveData<Event<List<Record>>> _memoryRecentRecordList = new MutableLiveData<>();
    private MutableLiveData<Event<Integer>> _bestScore = new MutableLiveData<>();
    // LiveData
    public final LiveData<Event<String>> getSelectedGame(){return this._selectedGame;}
    public final LiveData<Event<List<Record>>> getDebugRecordList(){return this._debugRecordList;}
    public final LiveData<Event<List<Record>>> cardBestRecordList(){return this._cardBestRecordList;}
    public final LiveData<Event<List<Record>>> hangmanBestRecordList(){return this._hangmanBestRecordList;}
    public final LiveData<Event<List<Record>>> memoryBestRecordList(){return this._memoryBestRecordList;}
    public final LiveData<Event<List<Record>>> cardRecentRecordList(){return this._cardRecentRecordList;}
    public final LiveData<Event<List<Record>>> hangmanRecentRecordList(){return this._hangmanRecentRecordList;}
    public final LiveData<Event<List<Record>>> memoryRecentRecordList(){return this._memoryRecentRecordList;}
    public final LiveData<Event<Integer>> bestScore(){return this._bestScore;}

    public void getAllBestRecord(Context context){
        Thread thread = new Thread(() -> {
            RecordDao recordDao = AppDatabase.getInstance(context).recordDao();
            _debugRecordList.postValue(new Event<>(recordDao.getAllBestRecord()));
        });
        thread.start();
    }

    public void getAllRecentRecord(Context context){
        Thread thread = new Thread(() -> {
            RecordDao recordDao = AppDatabase.getInstance(context).recordDao();
            _debugRecordList.postValue(new Event<>(recordDao.getAllRecentRecord()));
        });
        thread.start();
    }

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

    public void getBestScore(Context context, String game, String difficulty){
        Thread thread = new Thread(() -> {
            RecordDao recordDao = AppDatabase.getInstance(context).recordDao();
            _bestScore.postValue(new Event<>(recordDao.getBestScore(game, difficulty)));
        });
        thread.start();
    }

    public void setDisplayingGame(String game){
        _selectedGame.setValue(new Event<>(game));
    }


    public void insertRecord(Context context, Record record){
        updateBestRecord(context, record);
        updateRecentRecord(context, record);
    }

    public void updateBestRecord(Context context, Record record){
        Thread thread = new Thread(() -> {
            RecordDao recordDao = AppDatabase.getInstance(context).recordDao();
            ReceivedRecord currentRecord = recordDao.getBestRecordByDiff(record.gamename, record.difficulty);
            BestRecord newRecord = new BestRecord(currentRecord.id, record.gamename, record.difficulty, record.record);
            // DB에 저장된 기록이 더 낮으면 기록 추가
            if (record.record > currentRecord.record){
                recordDao.insertBestRecord(newRecord);
            }
        });
        thread.start();
    }

    public void updateRecentRecord(Context context, Record record){
        Thread thread = new Thread(() -> {
            RecordDao recordDao = AppDatabase.getInstance(context).recordDao();
            String game = record.gamename;
            // 최대 저장 개수에 아직 도달하지 않았다면 삽입 방식으로 투플 추가
            if (recordDao.getRecentRecordLength(game)<RECENTRECORD_MAXLENGTH){
                recordDao.insertRecentRecord(new RecentRecord(game, record.difficulty, record.record));
            } else {
                // 최대 저장 개수를 넘어가면 업데이트 방식으로 투플 추가 (가장 오래된 투플의 record_id를 참조하여 값 변경)
                int mostRecentRecordId = recordDao.getMostRecentRecordId(game);
                recordDao.updateRecentRecord(
                    new RecentRecord(mostRecentRecordId, game, record.difficulty, record.record));
            }
        });
        thread.start();
    }
}
