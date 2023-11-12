package com.example.hangman_java.memory.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class InvertedTriangleView extends View {

    private Paint boundaryPaint;
    private Paint fillPaint;
    private Path trianglePath;
    private Region triangleRegion;

    public InvertedTriangleView(Context context) {
        super(context);
        init();
    }

    public InvertedTriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InvertedTriangleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        trianglePath = new Path();
        boundaryPaint = new Paint();
        boundaryPaint.setColor(Color.BLACK);
        boundaryPaint.setStyle(Paint.Style.STROKE);
        fillPaint = new Paint();
        fillPaint.setColor(Color.GREEN);
        fillPaint.setStyle(Paint.Style.FILL);
        float midX = getWidth() / 2f;
        float midY = getHeight() / 2f;
        trianglePath.moveTo(midX, midY + 65); // 시작점 설정
        trianglePath.lineTo(midX + 75, midY - 65); // 오른쪽 꼭지점
        trianglePath.lineTo(midX - 75, midY - 65);
        trianglePath.lineTo(midX, midY + 65);// 왼쪽 꼭지점
        trianglePath.close(); // 세 점을 연결하여 삼각형 완성

        RectF rectF = new RectF();
        trianglePath.computeBounds(rectF, true);
        triangleRegion = new Region();
        triangleRegion.setPath(trianglePath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw boundary
        canvas.drawPath(trianglePath, boundaryPaint);

        // Fill the triangle
        canvas.drawPath(trianglePath, fillPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (triangleRegion.contains((int) x, (int) y)) {
                performClick(); // Handle the event
                return false;
            }
        }
        return true;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
    }
}

