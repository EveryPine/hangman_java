package com.example.hangman_java.mvvm.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class WordDatabase extends RoomDatabase{
    private static WordDatabase INSTANCE;

    public abstract WordDao wordDao();

    public static WordDatabase getInstance(Context context){
        if (INSTANCE==null){
            synchronized (WordDatabase.class){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        WordDatabase.class, "word.db")
                    .createFromAsset("WordDataBase.db")
                    .fallbackToDestructiveMigration()
                    .build();
            }

        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}