package com.example.hangman_java.base;

public class Event<T> {
    private final T content;
    private boolean hasbeenHandled = false;

    public Event(T content) {
        this.content = content;
    }

    public T getContentIfNotHandled(){
        if (hasbeenHandled){
            return null;
        } else {
            hasbeenHandled = true;
            return content;
        }
    }

    public T peekContent(){
        return content;
    }

    public boolean isHasbeenHandled(){
        return hasbeenHandled;
    }
}



// OnEventUnhandledContent 인터페이스
