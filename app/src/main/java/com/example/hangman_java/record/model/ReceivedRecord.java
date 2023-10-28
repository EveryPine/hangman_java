package com.example.hangman_java.record.model;

import androidx.room.ColumnInfo;

public class ReceivedRecord {
    @ColumnInfo(name = "record_id")
    public int id;
    @ColumnInfo(name = "record")
    public int record;
}
