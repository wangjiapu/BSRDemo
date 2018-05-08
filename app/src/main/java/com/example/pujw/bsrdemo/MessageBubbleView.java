package com.example.pujw.bsrdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class MessageBubbleView extends View {

    private static final int BSRDISTANCE=30;//拖拽的贝塞尔曲线距离

    private PointF mFixationPoint;//拖拽圆

    private PointF mDragPiont;//固定圆

    private int mDragRadius = 10;//拖拽圆的半径

    private Paint mPint;//画笔

    private int mFixactionRadius;
    private int getmFixactionRadiusMin = 3;//最小可绘制圆的大小
    private int mFixactionRadiusMax = 20;

    private Bitmap mDragBitmap;


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
    protected void onDraw(Canvas canvas) {
        if (mDragPiont == null || mFixationPoint == null) {
            return;
        }

        canvas.drawCircle(mDragPiont.x, mDragPiont.y, mDragRadius, mPint);

        Path bsrPath = getBSRpath();

        if (bsrPath != null) {
            canvas.drawCircle(mFixationPoint.x, mFixationPoint.y, mFixactionRadius, mPint);
            canvas.drawPath(bsrPath, mPint);
        }

        if (mDragBitmap!=null){
            canvas.drawBitmap(mDragBitmap,mDragPiont.x-mDragBitmap.getWidth()/2,
                    mDragPiont.y-mDragBitmap.getHeight()/2,null);
        }

    }

    /**
     * 改变拖拽点的位置
     *
     * @param moveX
     * @param moveY
     */
    public void updateDragPoint(float moveX, float moveY) {

        mDragPiont.x = moveX;
        mDragPiont.y = moveY;
        invalidate();
    }

    /**
     * 初始化两个圆
     */
    public void initPoint(float x, float y) {
        mFixationPoint = new PointF(x, y);
        mDragPiont = new PointF(x, y);
    }


    //dip转px
    private int dip2px(int dip) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
                getResources().getDisplayMetrics());
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


    /*
     * 获得贝塞尔曲线的路径
     * @return
     */
    public Path getBSRpath() {
        double distance = getdistance(mDragPiont, mFixationPoint);

        mFixactionRadius = (int) (mFixactionRadiusMax - distance / BSRDISTANCE);

        if (mFixactionRadius < getmFixactionRadiusMin) {
            return null;
        }

        Path path = new Path();
        float tanA = (mDragPiont.y - mFixationPoint.y) / (mDragPiont.x - mFixationPoint.x);
        Double arcTanA = Math.atan(tanA);

        //定圆一边的点
        float p0x = (float) (mFixationPoint.x + mFixactionRadius * Math.sin(arcTanA));
        float p0y = (float) (mFixationPoint.y - mFixactionRadius * Math.cos(arcTanA));

        float p3x = (float) (mFixationPoint.x - mFixactionRadius * Math.sin(arcTanA));
        float p3y = (float) (mFixationPoint.y + mFixactionRadius * Math.cos(arcTanA));

        //动圆的一个点
        float p1x = (float) (mDragPiont.x + mDragRadius * Math.sin(arcTanA));
        float p1y = (float) (mDragPiont.y - mDragRadius * Math.cos(arcTanA));

        float p2x = (float) (mDragPiont.x - mDragRadius * Math.sin(arcTanA));
        float p2y = (float) (mDragPiont.y + mDragRadius * Math.cos(arcTanA));


        PointF controlPoint = getControlPoint();
        path.moveTo(p0x, p0y);
        path.quadTo(controlPoint.x, controlPoint.y, p1x, p1y);

        path.lineTo(p2x, p2y);
        path.quadTo(controlPoint.x, controlPoint.y, p3x, p3y);
        path.close();
        return path;
    }

    public void handleActionUp(){
        if (mFixactionRadius > getmFixactionRadiusMin){//最小圆没有消失，那么就回弹
            ValueAnimator animator= ObjectAnimator.ofFloat(1);
            animator.setDuration(300);
            final PointF start=new PointF(mDragPiont.x,mDragPiont.y);
            final PointF end=new PointF(mFixationPoint.x,mFixationPoint.y);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float percent= (Float) animation.getAnimatedValue();
                    PointF pointF=BubbleStatusUtils.getPointByPercent(start,end,percent);
                    updateDragPoint(pointF.x,pointF.y);

                }
            });
            animator.setInterpolator(new OvershootInterpolator(2.0f));//设置一个查值器

            animator.start();

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (messageBubbleListener!=null){
                        messageBubbleListener.restore();
                    }
                }
            });

        }else{//爆炸
            if (messageBubbleListener!=null){
                messageBubbleListener.dismiss();

            }
        }
    }

    public PointF getControlPoint() {
        float c1 = (float) ((mDragPiont.x + mFixationPoint.x) * 0.5);
        float c2 = (float) ((mDragPiont.y + mFixationPoint.y) * 0.5);
        return new PointF(c1, c2);
    }


    public static void attch(@org.jetbrains.annotations.Nullable View view) {


        if (view == null) {
            throw new NullPointerException("View is null");
        }
        view.setOnTouchListener(new BubbleMessageTouchListener(view, view.getContext(),
                new BubbleMessageTouchListener.BubbleDisappearListener() {
            @Override
            public void dismiss(@NotNull View view) {
                //需求代码
                //这个是一个监听代码的的需求要书写的地方
                Toast.makeText(view.getContext(),"监听事件",Toast.LENGTH_SHORT).show();
            }
        }));

    }

    public void setDragBitmap(@NotNull Bitmap bitmapByView) {
        this.mDragBitmap=bitmapByView;
    }


    private MessageBubbleListener messageBubbleListener;

    public void setMessageBubbleListener(MessageBubbleListener listener){
        this.messageBubbleListener=listener;
    }

    public interface MessageBubbleListener{
        void restore();//还原
        void dismiss();//消失
    }
}
