package com.example.hangman_java.record.model;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecordDao {

    @Query("SELECT COUNT(*) FROM RecentRecord WHERE gamename = :game")
    int recordLength(String game);

    @Query("SELECT difficulty, record, strftime('%Y년 %m월 %d일', date, 'localtime') AS date FROM RecentRecord WHERE gamename = :game")
    List<Record> getRecentRecord(String game);

    @Query("SELECT difficulty, record, strftime('%Y년 %m월 %d일', date, 'localtime') AS date FROM BestRecord WHERE gamename = :game")
    List<Record> getBestRecord(String game);

    @Update
    void updateRecentRecord(RecentRecord recentRecord);

    @Update
    void updateBestRecord(BestRecord bestRecord);

}
