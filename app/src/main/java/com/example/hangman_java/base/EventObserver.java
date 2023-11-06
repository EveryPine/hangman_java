package com.example.hangman_java.base;

import androidx.lifecycle.Observer;

public class EventObserver<T> implements Observer<Event<T>> {
    private final OnEventUnhandledContent<T> onEventUnhandledContent;

    public EventObserver(OnEventUnhandledContent<T> onEventUnhandledContent) {
        this.onEventUnhandledContent = onEventUnhandledContent;
    }

    @Override
    public void onChanged(Event<T> event) {
        if (event != null) {
            T value = event.getContentIfNotHandled();
            if (value != null) {
                try {
                    onEventUnhandledContent.invoke(value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}