package com.example.hangman_java.record.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class Record {
    @ColumnInfo(name="difficulty")
    public String difficulty;
    @ColumnInfo(name="record")
    public int record;
    @ColumnInfo(name="date")
    public String date;
}
