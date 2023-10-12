package com.example.hangman_java.record.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "BestRecord")
public class BestRecord {
    @PrimaryKey
    public int record_id;
    @ColumnInfo(name="gamename")
    public String game;
    @ColumnInfo(name="difficulty")
    public String difficulty;
    @ColumnInfo(name="record")
    public int record;
    @ColumnInfo(name="date")
    public String date;
}
