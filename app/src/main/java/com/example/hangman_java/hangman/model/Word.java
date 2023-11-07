package com.example.hangman_java.hangman.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word")
public class Word{
    @PrimaryKey(autoGenerate = true)
    public int word_id;
    @ColumnInfo(name="word") @NonNull
    public String word;

    @ColumnInfo(name="meaning") @NonNull
    public String meaning;

    @ColumnInfo(name="difficulty") @NonNull
    public String difficulty;
}
