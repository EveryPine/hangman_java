package com.example.hangman_java.mvvm.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.hangman_java.mvvm.model.ImageManager;
import com.example.hangman_java.mvvm.model.WordDao;
import com.example.hangman_java.mvvm.model.WordDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameViewModel extends ViewModel {
    private final Random random = new Random();
    private final ImageManager imageManager = ImageManager.getInstance();

    // stageInfo
    private final MutableLiveData<Integer> _difficulty = new MutableLiveData<>();
    private final MutableLiveData<Integer> _gameScore = new MutableLiveData<>();

    // waveInfo
    private final MutableLiveData<String> _word = new MutableLiveData<>();
    private final MutableLiveData<Integer> _wordLength = new MutableLiveData<>(); // 단어 길이
    private final MutableLiveData<Integer> _remainingAlphabetCount = new MutableLiveData<>(); // 남아있는 맞춰야할 알파벳 개수
    private final MutableLiveData<Integer> _deathCount = new MutableLiveData<>(); // 데스 카운트
    private final MutableLiveData<Event<Integer>> _printingIndex = new MutableLiveData<>(); // 현재 그림 번호
    private final MutableLiveData<Event<Boolean>> _gameoverFlag = new MutableLiveData<>(); // 게임 오버 플래그
    private final MutableLiveData<Event<Boolean>> _gameClearFlag = new MutableLiveData<>(); // 게임 성공 플래그

    // eventInfo
    private final MutableLiveData<Character> _inputAlphabet = new MutableLiveData<>(); // 입력한 알파벳
    private final MutableLiveData<Event<ArrayList<Integer>>> _correctAlphabetIndexList = new MutableLiveData<>(); // 맞춘 알파벳이 있는 인덱스 리스트

    // resourceInfo
    private final LinearLayout.LayoutParams _questionLayoutParams = new LinearLayout.LayoutParams(60, 70); // '?' 이미지 레이아웃 파라미터
    private final LinearLayout.LayoutParams _underbarLayoutParams = new LinearLayout.LayoutParams(55, 20); // '_' 이미지 레이아웃 파라미터
    private final int _questionImageId = imageManager.getQuestionImage();
    private final int _underbarImageId = imageManager.getUnderbarImage();
    private final List<Integer> _alphabetIdList = imageManager.getAlphabetImageList();
    private final MutableLiveData<List<Integer>> _printingIdList = new MutableLiveData<>(); // 출력할 그림 id 리스트
    private final MutableLiveData<ArrayList<ImageView>> _imageviewList = new MutableLiveData<>();

    // observer를 붙일 변수들
    public final LiveData<Event<Integer>> getPrintingIndex(){ return this._printingIndex;}
    public final LiveData<Event<ArrayList<Integer>>> getCorrectAlphabetIndexList(){ return this._correctAlphabetIndexList;}
    public final LiveData<Event<Boolean>> getGameoverFlag(){ return this._gameoverFlag;}
    public final LiveData<Event<Boolean>> getGameClearFlag(){ return this._gameClearFlag;}

    public void setStageInfo(int difficulty){
        _difficulty.setValue(difficulty);
        switch (difficulty) {
            case 1 -> _deathCount.setValue(15);
            case 2 -> _deathCount.setValue(10);
            case 3 -> _deathCount.setValue(5);
        }
        _gameScore.setValue(0);
    }

    public void setWaveInfo(Context context) throws Exception {
        switch (_difficulty.getValue()) {
            case 1 -> _wordLength.setValue(4 + random.nextInt(4)); // 4 ~ 7 길이의 단어 생성
            case 2 -> _wordLength.setValue(5 + random.nextInt(4)); // 5 ~ 8 길이의 단어 생성
            case 3 -> _wordLength.setValue(6 + random.nextInt(5)); // 6 ~ 10 길이의 단어 생성
            default -> throw new Exception("viewModel의 difficulty값이 null입니다.");
        }
        setTargetWord(context, _wordLength.getValue());
        _remainingAlphabetCount.setValue(_wordLength.getValue());
        _printingIndex.setValue(new Event<>(0));
    }

    public void setResourceInfo() throws Exception{
        switch (_difficulty.getValue()) {
            case 1 -> _printingIdList.setValue(imageManager.getPrintingEasyImageList());
            case 2 -> _printingIdList.setValue(imageManager.getPrintingNormalImageList());
            case 3 -> _printingIdList.setValue(imageManager.getPrintingHardImageList());
            default -> throw new Exception("viewModel의 difficulty값이 null입니다.");
        }
        _imageviewList.setValue(new ArrayList<>());
    }

    private void setTargetWord(Context context, int wordLength){
        Runnable runnable = () -> {
            WordDao wordDao = WordDatabase.getInstance(context).wordDao();
            List<Integer> wordIdList = wordDao.getWordByLength(wordLength);
            _word.postValue(wordDao.getWordById(wordIdList.get(random.nextInt(wordIdList.size()))));
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public int getDifficulty(){
        return _difficulty.getValue();
    }

    public int getGameScore(){
        return _gameScore.getValue();
    }

    public int getPrintingIdListSize(){
        return Objects.requireNonNull(_printingIdList.getValue()).size();
    }

    public int getPrintingImageId(){
        return Objects.requireNonNull(_printingIdList.getValue()).get(Objects.requireNonNull(_printingIndex.getValue()).peekContent());
    }

    public int getAlphabetImageId(char alphabet){
        return _alphabetIdList.get(alphabet - 'a');
    }

    public int getImageIdByTag(String tag) throws Exception {
        if ("question".equals(tag)) {
            return _questionImageId;
        } else if ("underbar".equals(tag)) {
            return _underbarImageId;
        } else {
            throw new Exception("GameViewModel의 getImageIdByTag에서 TagError 발생");
        }
    }

    public ImageView getImageView(int index){
        return Objects.requireNonNull(_imageviewList.getValue()).get(index);
    }

    public int getWordLength(){
        return _wordLength.getValue();
    }

    public char getInputAlphabet(){
        return _inputAlphabet.getValue();
    }

    public LinearLayout.LayoutParams getLayoutParams(String tag) throws Exception{
        switch (tag){
            case "question" -> {
                return _questionLayoutParams;
            }
            case "underbar" -> {
                _underbarLayoutParams.gravity = Gravity.CENTER;
                return _underbarLayoutParams;
            }
            default -> throw new Exception("GameViewModel의 getLayoutParams에 잘못된 태그가 전달되었습니다.");
        }
    }

    public void updateGameScore(){
        _gameScore.setValue(_gameScore.getValue() + 1);
    }

    // 사용자 입력이 들어오면 로직 수행
    public void inputAlphabetListener(char alphabet){
        _inputAlphabet.setValue(alphabet);
        ArrayList<Integer> correctAlphabetIndexList = new ArrayList<>();
        for (int i=0; i<_wordLength.getValue(); i++){
            if (Objects.requireNonNull(_word.getValue()).charAt(i)==alphabet){
                _remainingAlphabetCount.setValue(_remainingAlphabetCount.getValue() - 1);
                correctAlphabetIndexList.add(i);
            }
        }
        // 성공
        if (correctAlphabetIndexList.size()!=0 && !isGameClear()){
            Log.d("MyTAG", "성공..단어 ui를 갱신합니다");
            _correctAlphabetIndexList.setValue(new Event<>(correctAlphabetIndexList));
        }
        if (correctAlphabetIndexList.size()==0 && !isGameover()){ // 실패
            _printingIndex.setValue(new Event<>(Objects.requireNonNull(_printingIndex.getValue()).peekContent() + 1));
        }
        Log.d("MyTAG", "남아있는 알파벳 개수: " + _remainingAlphabetCount.getValue());
        Log.d("MyTAG", "프린트 인덱스: " + Objects.requireNonNull(_printingIndex.getValue()).peekContent());

        if (isGameover()){
            _gameoverFlag.setValue(new Event<>(true));
        }
        if (isGameClear()){
            _gameClearFlag.setValue(new Event<>(true));
        }

    }

    public void addImageView(ImageView view){
        Objects.requireNonNull(_imageviewList.getValue()).add(view);
    }

    public void clearImageViewList(){
        if (Objects.requireNonNull(_imageviewList.getValue()).size()!=0){
            _imageviewList.getValue().clear();
        }
    }

    private boolean isGameover(){
        return Objects.equals(Objects.requireNonNull(_printingIndex.getValue()).peekContent(), _deathCount.getValue());
    }

    private boolean isGameClear(){
        return _remainingAlphabetCount.getValue()==0;
    }
}
