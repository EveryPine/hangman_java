package com.example.hangman_java.base;

public interface OnEventUnhandledContent<T> {
    void invoke(T value);
}
