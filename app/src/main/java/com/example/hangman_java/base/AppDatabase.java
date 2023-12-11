package com.example.hangman_java.base;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.hangman_java.hangman.model.Word;
import com.example.hangman_java.hangman.model.WordDao;
import com.example.hangman_java.record.model.BestRecord;
import com.example.hangman_java.record.model.RecentRecord;
import com.example.hangman_java.record.model.RecordDao;

@Database(entities = {Word.class, RecentRecord.class, BestRecord.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    private static AppDatabase INSTANCE;
    public abstract WordDao wordDao();
    public abstract RecordDao recordDao();

    public static AppDatabase getInstance(Context context){
        if (INSTANCE==null){
            synchronized (AppDatabase.class){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "appdatebase.db")
                    .createFromAsset("appDatabase.db")
                    .build();
            }

        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}