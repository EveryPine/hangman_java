package com.example.hangman_java.memory.viewmodel;

import android.util.Log;
import android.view.animation.AlphaAnimation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class MemoryViewModel extends ViewModel {
    public MemoryViewModel() {
        _CurrentStage.setValue(0);
        _difficulty.setValue(7);
        _ClicckCount.setValue(0);
    }
    private  MutableLiveData<Integer> _difficulty = new MutableLiveData<>();
    private MutableLiveData<List<Integer>> _answerList = new MutableLiveData<>();
    private MutableLiveData<List<Integer>> _currentAnswer = new MutableLiveData<>();

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
        List<Integer> tempList = new ArrayList<>();
        for(int i =1;i<=_difficulty.getValue();i++){
            tempList.add(i);
        }
        Collections.shuffle(tempList);
        _answerList.setValue(tempList);
    }
    Integer getInputOrder(){
        return _InputOrder.getValue();
    }
    public Boolean InputOrderListener(int number) {
        if (_difficulty == null || _ClicckCount == null || _InputOrder == null) {
            return false; // 어떤 변수라도 null이면 오류로 처리
        }
        Log.d("testt",getAnswerList().toString());
        ArrayList<Integer> tempList = (ArrayList<Integer>) getAnswerList();
        Log.d("testt", String.valueOf(tempList.get(Integer.parseInt(_ClicckCount.getValue().toString())))+"정답리스트에있는 답");
        if (tempList.get(_ClicckCount.getValue()) == getInputOrder()) {
            int tempClickCount = _ClicckCount.getValue();
            _ClicckCount.setValue(tempClickCount + 1);
        } else {
            return false;
        }
        if (getCurrentStage() > _difficulty.getValue()) {
            //게임 종료
            //게임 자체가 종료됨
            Log.d("testt","게임끝");
        }
        // 다음단계로 넘어가 화면 깜빡이게하는 함수 실행
        return true;
    }
    public Boolean CheckNextStage(){
        if(getCurrentStage() < _ClicckCount.getValue()){
            _ClicckCount.setValue(0);
            Integer tempCurrentStage = getCurrentStage();
            _CurrentStage.setValue(tempCurrentStage+1);
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
    private List<Integer> sliceArrayList(List<Integer> originalList, int newSize) {
        if (newSize <= 0) {
            return new ArrayList<>(); // 원하는 크기가 0 이하면 빈 ArrayList 반환
        } else if (newSize >= originalList.size()) {
            return new ArrayList<>(originalList); // 원래 크기 이상이면 원래 ArrayList를 복사하여 반환
        } else {
            return originalList.subList(0, newSize); // 새로운 크기로 ArrayList 잘라내기
        }
    }

}
