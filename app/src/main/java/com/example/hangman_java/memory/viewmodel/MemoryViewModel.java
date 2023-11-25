package com.example.hangman_java.memory.viewmodel;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;
import android.view.animation.AlphaAnimation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hangman_java.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class MemoryViewModel extends ViewModel {
    public MemoryViewModel() {
        _CurrentStage.setValue(0);
        _ClicckCount.setValue(0);
        _difficulty.setValue(0);
        _score.setValue(0);
    }private SoundPool soundPool;
    private int mp3FileSoundId1;
    private int mp3FileSoundId2;
    private int mp3FileSoundId3;

    private MutableLiveData<Integer> _score = new MutableLiveData<>();
    private  MutableLiveData<Integer> _difficulty = new MutableLiveData<>();
    private MutableLiveData<List<Integer>> _answerList = new MutableLiveData<>();
    private MutableLiveData<List<Integer>> _currentAnswer = new MutableLiveData<>();
    public int getScore(){
        return _score.getValue();
    }
    public LiveData<List<Integer>>  answerList() {
        return _answerList;
    }
    private MutableLiveData<Integer> _InputOrder = new MutableLiveData<>();
    public LiveData<Integer> InputOrder(){
        return _InputOrder;
    }
    public void setInputOrder(int number){
        _InputOrder.setValue(number);
    }
    private MutableLiveData<Integer> _CurrentStage = new MutableLiveData<>();
    public  void setCurrentStage(){
        _CurrentStage.setValue(1);
    }
    private MutableLiveData<Integer> _ClicckCount = new MutableLiveData<>();
    private int getCurrentCount(){
        return _ClicckCount.getValue();
    }
    private MutableLiveData<Integer> _CurrentAnswer = new MutableLiveData<>();
    public void setSoundPool(Context context){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(attributes)
                .build();
        mp3FileSoundId1 = soundPool.load(context, R.raw.memory_sound1,1);
        mp3FileSoundId2= soundPool.load(context,R.raw.memory_correct_sound1,1);
    }
    public void playSound(int sound) {
        if (soundPool != null && mp3FileSoundId1 != 0) {
            // MP3 파일 사운드 재생
            if(sound == 1){
                soundPool.play(mp3FileSoundId1, 1.0f, 1.0f, 1, 0, 1.0f);
            } else if (sound == 2) {
                soundPool.play(mp3FileSoundId2, 1.0f, 1.0f, 1, 0, 1.0f);

            }else if(sound == 3){
                soundPool.play(mp3FileSoundId2, 1.0f, 1.0f, 1, 0, 1.0f);
            }


        }
    }
    public int getDifficulty() {
        Log.d("testt", "Difficulty is" + _difficulty.getValue());
        return (int) _difficulty.getValue();
    }
    public void setDifficulty(int difficulty) {
        if (_difficulty == null) {
            Log.d("testt", "Difficulty is null!");
        }
        Log.d("testt", "Difficulty is" + difficulty);
        _difficulty.setValue(difficulty);
    }
    public int getFirstAnswer(){
        ArrayList<Integer> tempList = (ArrayList<Integer>) getAnswerList();
        return tempList.get(0);
    }
    public Integer getCurrentStage(){
        return _CurrentStage.getValue();
    }
    public List<Integer> getAnswerList(){
        return _answerList.getValue();
    }
    public void setAnswerList(){
        List<Integer> tempAnswerList = new ArrayList<>();
        for (int j = 0 ; j <= 100 ;j++){
            List<Integer> tempList = new ArrayList<>();
            for (int i = 1; i <= _difficulty.getValue(); i++) {
                tempList.add(i);
            }
            Collections.shuffle(tempList);
            tempAnswerList.addAll(tempList);
        }
        _answerList.setValue(tempAnswerList);

    }
    Integer getInputOrder(){
        return _InputOrder.getValue();
    }
    public Boolean InputOrderListener(int number) {
        if (_difficulty == null || _ClicckCount == null || _InputOrder == null) {
            return false; // 어떤 변수라도 null이면 오류로 처리
        }

        ArrayList<Integer> tempList = (ArrayList<Integer>) getAnswerList();
        if (tempList.get(_ClicckCount.getValue()) == getInputOrder()) {
            int tempClickCount = _ClicckCount.getValue();
            _ClicckCount.setValue(tempClickCount + 1);
        } else {
            return false;
        }

        // 다음단계로 넘어가 화면 깜빡이게하는 함수 실행
        return true;
    }
    public Boolean CheckNextStage(){
        if(getCurrentStage() < _ClicckCount.getValue()){
            _ClicckCount.setValue(0);
            _CurrentStage.setValue(getCurrentStage() + 1);
            _score.setValue(_score.getValue()+1);
            return true;
        }
        return false;
    }
    public AlphaAnimation createAnimation(){
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setStartOffset(20);
        return anim;
    }


}
