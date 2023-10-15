package com.example.hangman_java.game.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hangman_java.base.BaseViewModel;
import com.example.hangman_java.base.Event;

import java.util.Objects;

public class GameViewModel extends BaseViewModel {
    private MutableLiveData<Event<String>> _selectedGame = new MutableLiveData<>();
    private MutableLiveData<Integer> _difficulty = new MutableLiveData<>();
    public LiveData<Event<String>> selectedGame() {return this._selectedGame;}

    public void setGame(String game){
        _selectedGame.setValue(new Event<>(game));
    }

    public void setDifficulty(int difficulty){
        _difficulty.setValue(difficulty);
    }

    public String getSelectedGame(){
        return Objects.requireNonNull(_selectedGame.getValue()).peekContent();
    }

    public int getDifficulty(){return _difficulty.getValue()!=null ? _difficulty.getValue() : -1;}

}
