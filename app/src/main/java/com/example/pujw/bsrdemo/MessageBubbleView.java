package com.example.pujw.bsrdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class MessageBubbleView extends View {

    private PointF mFixationPoint;

    private PointF mDragPiont;

    private int mDragRadius = 10;

    private Paint mPint;//画笔


    private int mFixactionRadius;
    private int getmFixactionRadiusMax=7;



    public MessageBubbleView(Context context) {
        this(context, null);
    }

    public MessageBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDragRadius = dip2px(mDragRadius);
        mFixactionRadius = dip2px(getmFixactionRadiusMax);
        mPint = new Paint();
        mPint.setColor(Color.RED);
        mPint.setAntiAlias(true);
        mPint.setDither(true);
    }




    private int getdistance(PointF dragPiont,PointF fixationPoint ){

        return 0;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();
                initPoint(downX, downY);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                updateDragPoint(moveX, moveY);
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        invalidate();
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mDragPiont==null || mFixationPoint==null){
            return;
        }

        canvas.drawCircle(mDragPiont.x, mDragPiont.y, mDragRadius, mPint);


    }

    /**
     * 改变拖拽点的位置
     *
     * @param moveX
     * @param moveY
     */
    private void updateDragPoint(float moveX, float moveY) {

        mDragPiont.x = moveX;
        mDragPiont.y = moveY;


    }

    /**
     * 初始化两个圆
     */
    private void initPoint(float x, float y) {
        mFixationPoint = new PointF(x, y);

        mDragPiont = new PointF(x, y);
    }



    private int dip2px(int dip) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }
}
