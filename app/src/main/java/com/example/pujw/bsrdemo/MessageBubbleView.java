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

    private PointF mFixationPoint;//拖拽圆

    private PointF mDragPiont;//固定圆

    private int mDragRadius = 10;//拖拽圆的半径

    private Paint mPint;//画笔

    private int mFixactionRadius;
    private int getmFixactionRadiusMin = 3;//最小可绘制圆的大小
    private int mFixactionRadiusMax = 20;


    public MessageBubbleView(Context context) {
        this(context, null);
    }

    public MessageBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDragRadius = dip2px(mDragRadius);
        mFixactionRadius = dip2px(mFixactionRadiusMax);
        getmFixactionRadiusMin = dip2px(getmFixactionRadiusMin);
        mPint = new Paint();
        mPint.setColor(Color.RED);
        mPint.setAntiAlias(true);
        mPint.setDither(true);
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
        if (mDragPiont == null || mFixationPoint == null) {
            return;
        }

        canvas.drawCircle(mDragPiont.x, mDragPiont.y, mDragRadius, mPint);

        double distance = getdistance(mDragPiont, mFixationPoint);

        mFixactionRadius = (int) (mFixactionRadiusMax - distance / 14);

        if (mFixactionRadius > getmFixactionRadiusMin) {//如果距离小于最小值，不要求绘制
            canvas.drawCircle(mFixationPoint.x, mFixationPoint.y, mFixactionRadius, mPint);
        }
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

    /**
     * 求两点之间的距离
     *
     * @param dragPiont
     * @param fixationPoint
     * @return
     */
    private double getdistance(PointF dragPiont, PointF fixationPoint) {

        return Math.sqrt((fixationPoint.x - dragPiont.x) * (fixationPoint.x - dragPiont.x)
                + (fixationPoint.y - dragPiont.y) * (fixationPoint.y - dragPiont.y));
    }

}
