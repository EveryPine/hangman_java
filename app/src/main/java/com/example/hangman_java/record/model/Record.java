package com.example.hangman_java.record.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class Record {
    @ColumnInfo(name="gamename")
    public String gamename;
    @ColumnInfo(name="difficulty")
    public String difficulty;
    @ColumnInfo(name="record")
    public int record;
    @ColumnInfo(name="date")
    public String date;

    public Record(String gamename, String difficulty, int record){
        this.gamename = gamename;
        this.difficulty = difficulty;
        this.record = record;
    }
}
