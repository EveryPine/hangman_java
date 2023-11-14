package com.example.hangman_java.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.hangman_java.R;

@SuppressLint("AppCompatCustomView")
public class TextOutLineView extends TextView {
    private boolean stroke = false;
    private float strokeWidth = 0.0f;
    private int strokeColor;

    public TextOutLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public TextOutLineView(Context context, AttributeSet attrs){
        super(context, attrs);
        initView(context, attrs);
    }

    public TextOutLineView(Context context){ super(context); }

    // 커스텀 텍스트뷰 동적 생성용 생성자
    public TextOutLineView(Context context, AttributeSet attrs, boolean stroke, int strokeColor, float strokeWidth){
        super(context, attrs);
        this.stroke = stroke;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
    }

    private void initView(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextOutLineView);
        stroke = a.getBoolean(R.styleable.TextOutLineView_textStroke, false);
        strokeWidth = a.getFloat(R.styleable.TextOutLineView_textStrokeWidth, 0.0f);
        strokeColor = a.getColor(R.styleable.TextOutLineView_textStrokeColor, 0xffffffff);
    }

    @Override
    protected void onDraw(Canvas canvas){
        if (stroke){
            ColorStateList states = getTextColors();
            getPaint().setStyle(Paint.Style.STROKE);
            getPaint().setStrokeWidth(strokeWidth);
            setTextColor(strokeColor);
            super.onDraw(canvas);

            getPaint().setStyle(Paint.Style.FILL);
            setTextColor(states);
        }
        super.onDraw(canvas);
    }
}
