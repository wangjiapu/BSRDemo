package com.example.pujw.bsrdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.drawable.AnimationDrawable
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast

class BubbleMessageTouchListener(val view: View, val context: Context,val listener: BubbleDisappearListener) : View.OnTouchListener, MessageBubbleView.MessageBubbleListener {


    private var mWindowManager: WindowManager? = null
    private var mParams: WindowManager.LayoutParams? = null
    private var mMessageBubbleView: MessageBubbleView? = null

    private var mBombFrame: FrameLayout? = null
    private var mBombImage: ImageView? = null

   // private var mListener:BubbleDisappearListener?=null

    init {
        mMessageBubbleView = MessageBubbleView(context)
        mMessageBubbleView!!.setMessageBubbleListener(this)
        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        mParams = WindowManager.LayoutParams()
        mParams!!.format = PixelFormat.TRANSPARENT//透明的背景

        mBombFrame=FrameLayout(context)
        mBombImage= ImageView(context)

        mBombImage!!.layoutParams=FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                ,ViewGroup.LayoutParams.WRAP_CONTENT)
        mBombFrame!!.addView(mBombImage)

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {

            MotionEvent.ACTION_DOWN -> {
                view.visibility = View.INVISIBLE
                mWindowManager!!.addView(mMessageBubbleView, mParams)
                val loaction = IntArray(2)
                view.getLocationOnScreen(loaction)

                mMessageBubbleView!!.initPoint(loaction[0].toFloat() + view.width / 2,
                        loaction[1].toFloat() + view.height / 2 - BubbleStatusUtils.getStatusBarHeight(context))
                mMessageBubbleView!!.setDragBitmap(getBitmapByView(view))
            }
            MotionEvent.ACTION_MOVE -> {

                mMessageBubbleView!!.updateDragPoint(event.rawX,
                        event.rawY - BubbleStatusUtils.getStatusBarHeight(context))

            }
            MotionEvent.ACTION_UP -> {

                mMessageBubbleView!!.handleActionUp()
            }
        }

        return true
    }

    interface BubbleDisappearListener {
        fun dismiss(view: View)
    }


    /**
     * 从一个View中获取一个Bitmap
     */
    private fun getBitmapByView(view: View): Bitmap {
        view.buildDrawingCache()
        return view.drawingCache
    }


    override fun restore() {
        mWindowManager!!.removeView(mMessageBubbleView)
        view.visibility = View.VISIBLE
    }

    override fun dismiss() {
        mWindowManager!!.removeView(mMessageBubbleView)
        //消失的时候执行爆炸动画
        mWindowManager!!.addView(mBombFrame,mParams)
        /**
         * 如果需要消失动画
         *
         */
       /* mBombImage!!.setBackgroundResource(R.drawable.anim)
        val drawable= mBombImage!!.background as AnimationDrawable
        drawable.start()
        mBombImage!!.postDelayed(Runnable {
            mWindowManager!!.removeView(mBombFrame)

            listener.dismiss(mMessageBubbleView!!)

        },getAnimationDrawableTime(drawable))*/
        Toast.makeText(context,"气泡消失",Toast.LENGTH_SHORT).show()
    }

    private fun getAnimationDrawableTime(drawable: AnimationDrawable): Long {
        val number =drawable.numberOfFrames

        var time=0
        for (i in 0..number){
            time+=drawable.getDuration(i)
        }
        return time.toLong()
    }


}