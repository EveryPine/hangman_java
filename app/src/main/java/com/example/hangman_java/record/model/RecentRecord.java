package com.example.hangman_java.record.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "RecentRecord")
public class RecentRecord {
    @PrimaryKey(autoGenerate = true)
    public int record_id;
    @ColumnInfo(name="gamename") @NonNull
    public String game;
    @ColumnInfo(name="difficulty") @NonNull
    public String difficulty;
    @ColumnInfo(name="record")
    public int record;
    @ColumnInfo(name="date") @NonNull
    public String date;

    public RecentRecord(int id, @NonNull String game, @NonNull String difficulty, int record){
        this.record_id = id;
        this.game = game;
        this.difficulty = difficulty;
        this.record = record;
        Date date = new Date((long) System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.date = dateFormat.format(date);
    }

    public RecentRecord(@NonNull String game, @NonNull String difficulty, int record){
        this.game = game;
        this.difficulty = difficulty;
        this.record = record;
        Date date = new Date((long) System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.date = dateFormat.format(date);
    }
}
