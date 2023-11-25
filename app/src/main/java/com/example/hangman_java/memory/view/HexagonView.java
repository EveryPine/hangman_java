package com.example.hangman_java.memory.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

public class HexagonView extends View {
    private Paint boundaryPaint;
    private Paint fillPaint;
    private Path hexagonPath;
    private Region hexagonRegion;

    public HexagonView(Context context) {
        super(context);
        init();
    }

    public HexagonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HexagonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        hexagonPath = new Path();
        float midX = getWidth() / 2;
        float midY = getHeight() / 2;
        boundaryPaint = new Paint();
        boundaryPaint.setColor(Color.BLACK);
        boundaryPaint.setStyle(Paint.Style.STROKE);

        fillPaint = new Paint();
        fillPaint.setColor(Color.argb(0,0,0,0));
        fillPaint.setStyle(Paint.Style.FILL);

        hexagonPath.moveTo(midX-getWidth()/2f, midY );
        hexagonPath.lineTo(midX -midX/2f, midY - getHeight()/2f);
        hexagonPath.lineTo(midX + midX/2f, midY - getHeight()/2f);
        hexagonPath.lineTo(midX +getWidth()/2, midY);
        hexagonPath.lineTo(midX + midX/2f, midY +getHeight()/2f);
        hexagonPath.lineTo(midX -midX/2f, midY+getHeight()/2f);
        hexagonPath.lineTo(midX-getWidth()/2f, midY );
        hexagonPath.close();


        RectF rectF = new RectF(midX - 150, midY - 130, midX + 150, midY + 130);
        hexagonRegion = new Region();
        hexagonRegion.setPath(hexagonPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (hexagonRegion.contains((int) x, (int) y)) {
                // 육각형 내부를 터치했을 때 이벤트처리를 상위뷰로 넘김
                //여기서는 Fragment로 넘김
                // 예: 버튼 클릭     이벤트 처리
                performClick();
                return false;
            }
        }
        //육각형 밖을 터치했을 때 이벤트처리를 여기서 수행
        //아무코드도 적지 않는걸로 무시
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 경계선 그리기
        canvas.drawPath(hexagonPath, boundaryPaint);

        // 내부 영역 색칠하기
        canvas.drawPath(hexagonPath, fillPaint);
    }
    public void startCustomAnimation(AlphaAnimation anim) {

        anim.setDuration(1000);
        anim.setStartOffset(20);
        startAnimation(anim);
    }
}
