package com.example.hangman_java.record.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecordDao {
    @Query("SELECT * FROM BestRecord")
    List<Record> getAllBestRecord();

    @Query("SELECT * FROM RecentRecord")
    List<Record> getAllRecentRecord();

    @Query("SELECT COUNT(*) FROM RecentRecord WHERE gamename = :game")
    int getRecentRecordLength(String game);

    @Query("SELECT record_id FROM RecentRecord WHERE gamename = :game ORDER BY date LIMIT 1")
    int getMostRecentRecordId(String game);

    @Query("SELECT gamename, difficulty, record, strftime('%Y년 %m월 %d일', date, 'localtime') AS date FROM RecentRecord WHERE gamename = :game")
    List<Record> getRecentRecord(String game);

    @Query("SELECT gamename, difficulty, record, strftime('%Y년 %m월 %d일', date, 'localtime') AS date " +
        "FROM BestRecord " +
        "WHERE gamename = :game")
    List<Record> getBestRecord(String game);

    @Query("SELECT record_id, record FROM BestRecord WHERE gamename = :game AND difficulty = :difficulty")
    ReceivedRecord getBestRecordByDiff(String game, String difficulty);


    @Insert
    void insertRecentRecord(RecentRecord recentRecord);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBestRecord(BestRecord bestRecord);

    @Update
    void updateRecentRecord(RecentRecord recentRecord);

}
