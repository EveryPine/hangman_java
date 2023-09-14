package com.example.hangman_java.mvvm.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "words")
public class Word{
    @PrimaryKey
    public int id;
    @ColumnInfo(name="word")
    public String word;
}
