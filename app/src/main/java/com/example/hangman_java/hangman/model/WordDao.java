package com.example.hangman_java.hangman.model;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {

    @Query("SELECT * FROM word")
    List<Word> getAll();

    // 주어진 난이도에 맞는 단어를 랜덤으로 가져옴
    @Query("SELECT word FROM word WHERE difficulty = :difficulty ORDER BY RANDOM() LIMIT 1")
    String getWord(String difficulty);
}
