package com.example.hangman_java.hangman.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hangman_java.base.AppDatabase;
import com.example.hangman_java.base.BaseViewModel;
import com.example.hangman_java.base.Event;
import com.example.hangman_java.base.ImageManager;
import com.example.hangman_java.hangman.model.WordDao;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class HangmanViewModel extends BaseViewModel {
    public final int DEATH_COUNT = 11; // 행맨 그림 개수 - 1 값과 일치해야함
    private final Random random = new Random();
    private TimerThread timerThread;
    private final int refTime = 10;
    private final ImageManager imageManager = ImageManager.getInstance();

    // stageInfo
    private final MutableLiveData<Integer> _difficulty = new MutableLiveData<>();
    private final MutableLiveData<Integer> _gameScore = new MutableLiveData<>();
    private final MutableLiveData<Integer> _bestScore = new MutableLiveData<>();

    // waveInfo
    private final MutableLiveData<Event<String>> _word = new MutableLiveData<>();
    private final MutableLiveData<Integer> _remainingAlphabetCount = new MutableLiveData<>(0); // 남아있는 맞춰야할 알파벳 개수
    private final MutableLiveData<Event<Integer>> _printingIndex = new MutableLiveData<>(); // 현재 그림 번호
    private final MutableLiveData<Event<Boolean>> _gameoverFlag = new MutableLiveData<>(); // 게임 오버 플래그
    private final MutableLiveData<Event<Boolean>> _gameClearFlag = new MutableLiveData<>(); // 게임 성공 플래그
    private final MutableLiveData<Event<Integer>> _remainingTime = new MutableLiveData<>(); // 남은 시간

    // eventInfo
    private final MutableLiveData<Character> _inputAlphabet = new MutableLiveData<>(); // 입력한 알파벳
    private final MutableLiveData<Event<ArrayList<Integer>>> _correctAlphabetIndexList = new MutableLiveData<>(); // 맞춘 알파벳이 있는 인덱스 리스트

    // resourceInfo
    private final int _questionImageId = imageManager.getQuestionImage();
    private final int _underbarImageId = imageManager.getUnderbarImage();
    private final MutableLiveData<ArrayList<TextView>> _textViewList = new MutableLiveData<>();

    // observer를 붙일 변수들
    public final LiveData<Event<Integer>> printingIndex(){ return this._printingIndex;}
    public final LiveData<Event<ArrayList<Integer>>> correctAlphabetIndexList(){ return this._correctAlphabetIndexList;}
    public final LiveData<Event<Boolean>> gameoverFlag(){ return this._gameoverFlag;}
    public final LiveData<Event<Boolean>> gameClearFlag(){ return this._gameClearFlag;}
    public final LiveData<Event<Integer>> remainingTime(){ return this._remainingTime;}
    public final LiveData<Event<String>> word() { return this._word; }

    // 스테이지 정보 설정 - difficulty, deathCount, gameScore 초기화
    public void setStageInfo(int difficulty){
        _difficulty.setValue(difficulty);
        _gameScore.setValue(0);
    }

    // 웨이브 정보 설정 - 단어 출현 난이도 확률 초기화, 목표 단어 초기화
    public void setWaveInfo(Context context) throws Exception {
        String[] difficultyArray = new String[100];
        switch (_difficulty.getValue()) {
            case 0 -> {
                for (int i = 0; i < 100; i++){
                    if (i < 80) difficultyArray[i] = "easy";
                    else difficultyArray[i] = "normal";
                }
            } // 쉬움 확률 설정 (easy: 80%, normal: 20%)
            case 1 -> {
                for (int i = 0; i < 100; i++){
                    if (i < 20) difficultyArray[i] = "easy";
                    else if (i < 70) difficultyArray[i] = "normal";
                    else difficultyArray[i] = "hard";
                }
            } // 보통 확률 설정 (easy: 20%, normal: 70%, hard: 10%)
            case 2 -> {
                for (int i = 0; i < 100; i++){
                    if (i < 30) difficultyArray[i] = "normal";
                    else difficultyArray[i] = "hard";
                }
            } // 어려움 확률 설정 (normal: 30%, hard: 70%)
            default -> throw new Exception("viewModel의 difficulty값이 null입니다.");
        }

        setTargetWord(context, difficultyArray);
        _printingIndex.setValue(new Event<>(0));
    }

    // 리소스 정보 설정 - imageviewList (알파벳 이미지 뷰 리스트) 초기화
    public void setResourceInfo(){_textViewList.setValue(new ArrayList<>());}

    // 목표 단어 설정 (DB 스레드 이용)
    private void setTargetWord(Context context, @NonNull String[] difficultyArray){
        String wordDiffculty = difficultyArray[random.nextInt(100)]; // 가져올 단어 난이도
        Thread thread = new Thread(() -> {
            WordDao wordDao = AppDatabase.getInstance(context).wordDao();
            _word.postValue(new Event<>(wordDao.getWord(wordDiffculty)));
        });
        thread.start();
    }

    public void startTimer(int inputTime, boolean isFinished){
        if (timerThread!=null) {
            timerThread.interrupt();
            timerThread = null;
        }
        if (isFinished) return;
        timerThread = new TimerThread(inputTime, refTime);
        timerThread.start();
    }

    public void stopTimer() throws InterruptedException {
        if (timerThread==null) {
            Log.e("MyTAG", "HangmanViewModel.stopTimer(): 아직 timerThread가 생성되지 않았습니다.");
            return;
        }
        timerThread.setStopFlag(true);
    }

    public void restartTimer() {
        if (timerThread==null) {
            Log.e("MyTAG", "HangmanViewModel.restartTimer(): 아직 timerThread가 생성되지 않았습니다.");
            return;
        }
        timerThread.setStopFlag(false);
        timerThread.resumeThread();
    }

    public void updateTimerTime(int time) {
        if (timerThread==null) {
            Log.e("MyTAG", "HangmanViewModel.updateTimerTime(): 아직 timerThread가 생성되지 않았습니다.");
            return;
        }
        timerThread.updateTime(time, false);
    }
    public void setBestScore(int bestScore) { _bestScore.setValue(bestScore); }

    // 남은 알파벳 개수 업데이트
    public void updateRemainingAlphabetCount() {
        if (_remainingAlphabetCount.getValue()==0) _remainingAlphabetCount.setValue(getWordLength());
        else _remainingAlphabetCount.setValue(_remainingAlphabetCount.getValue() - 1);
    }

    public String getStrDifficulty() throws Exception{
        switch (_difficulty.getValue()){
            case 0 -> {return "easy";}
            case 1 -> {return "normal";}
            case 2 -> {return "hard";}
            default -> throw new Exception("SetDifficultyFragment에서 잘못된 difficulty 값이 전송되었습니다.");
        }
    }
    
    public int getIntDifficulty() { return _difficulty.getValue(); }

    public int getGameScore(){return _gameScore.getValue();}

    public int getBestScore() { return _bestScore.getValue(); }

    // 그림 이미지 id 반환
    public int getPrintingImageId(){return imageManager.getPrintingImageList().get(_printingIndex.getValue().peekContent());}

    // 특수문자 이미지 id 반환
    public int getImageIdByTag(String tag) {
        if ("question".equals(tag)) return _questionImageId;
        else if ("underbar".equals(tag)) return _underbarImageId;
        else {
            Log.e("MyTAG", "HangmanViewmModel의 getImageIdByTag에서 에러 발생");
            return -1;
        }
    }

    // 주어진 인덱스에 해당하는 이미지뷰 객체 반환
    public TextView getTextView(int index) { return Objects.requireNonNull(_textViewList.getValue()).get(index); }

    public char getInputAlphabet() { return _inputAlphabet.getValue(); }

    public int getWordLength() {
        if (word().getValue()!=null) return word().getValue().peekContent().length();
        else {
            Log.e("MyTAG", "아직 목표 단어가 설정되지 않았습니다");
            return -1;
        }
    }

    // 게임 점수 갱신
    public void updateGameScore(){ _gameScore.setValue(_gameScore.getValue() + 1); }

    // 사용자 입력이 들어오면 로직 수행
    public void inputAlphabetListener(char alphabet) {
        _inputAlphabet.setValue(alphabet);
        ArrayList<Integer> correctAlphabetIndexList = new ArrayList<>();
        for (int i=0; i<getWordLength(); i++){
            if (_word.getValue().peekContent().charAt(i)==alphabet){
                updateRemainingAlphabetCount();
                correctAlphabetIndexList.add(i);
            }
        }
        // 성공
        if (correctAlphabetIndexList.size()!=0){
            Log.d("MyTAG", "성공..단어 ui를 갱신합니다");
            updateTimerTime(3);
            _correctAlphabetIndexList.setValue(new Event<>(correctAlphabetIndexList));
        }
        // 실패
        if (correctAlphabetIndexList.size()==0 && !isGameover()){
            _printingIndex.setValue(new Event<>(_printingIndex.getValue().peekContent() + 1));
        }
        Log.d("MyTAG", "프린트 인덱스: " + Objects.requireNonNull(_printingIndex.getValue()).peekContent());

        // 단어를 맞추는데 실패하면
        if (isGameover()){
            _gameoverFlag.setValue(new Event<>(true));
        }
        if (isGameClear()){ // 단어를 맞췄다면
            _gameClearFlag.setValue(new Event<>(true));
        }

    }

    // 물음표 텍스트뷰 리스트에 텍스트 뷰를 순차적으로 추가
    public void addTextView(TextView view){
        _textViewList.getValue().add(view);
    }

    // 이미지 뷰 리스트 초기화
    public void clearImageViewList(){
        if (Objects.requireNonNull(_textViewList.getValue()).size()!=0){
            _textViewList.getValue().clear();
        }
    }

    // 게임오버인지 확인
    private boolean isGameover(){
        return Objects.equals(Objects.requireNonNull(_printingIndex.getValue()).peekContent(), DEATH_COUNT);
    }

    // 게임 클리어가 되었는지 확인
    private boolean isGameClear(){
        return _remainingAlphabetCount.getValue()==0;
    }

    private class TimerThread extends Thread{
        private int remainTime; // 남은 시간
        private int refTime; // 해당 밀리초만큼 스레드를 sleep
        private int diffTime; // diffTime
        private boolean stopFlag = false;

        public TimerThread(int remainTime, int refTime){
            this.refTime = refTime;
            this.diffTime = 1000 / refTime;
            this.remainTime = remainTime * diffTime;
        }

        @Override
        public void run(){
            synchronized (this){
                this.updateTime(diffTime, true);
                while (remainTime >= 0){
                    if (stopFlag) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try{
                        Thread.sleep(refTime);
                    } catch (InterruptedException e) {
                        break;
                    }
                    this.updateTime(-1, true);
                    _remainingTime.postValue(new Event<>(remainTime / diffTime));
                }
            }
        }

        // time: 가감할 시간 (단위: 초), fromThread: (내부 호출 여부 부울값)
        public void updateTime(int time, boolean fromThread) {
            if (fromThread) remainTime += time;
            else remainTime += time * diffTime;
        }

        // 스레드 일시정지 플래그 설정
        public void setStopFlag(boolean flag) { stopFlag = flag; }
        // wait 상태인 스레드를 깨움
        public synchronized void resumeThread() { notify(); }
    }
}
