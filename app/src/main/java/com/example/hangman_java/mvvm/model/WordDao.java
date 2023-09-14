package com.example.hangman_java.mvvm.model;

import androidx.room.Dao;
import androidx.room.Query;
import java.util.List;

@Dao
public interface WordDao {

    @Query("SELECT * FROM words")
    List<Word> getAll();

    // length 길이를 가지는 단어들의 id를 모두 조회
    @Query("SELECT id FROM words WHERE LENGTH(word) = :length")
    List<Integer> getWordByLength(int length);

    // 해당 id의 단어를 가져옴
    @Query("SELECT word FROM words WHERE id = :targetId")
    String getWordById(int targetId);
}
