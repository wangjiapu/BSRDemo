package com.example.pujw.bsrdemo

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

class BubbleMessageTouchListener(val view: View, val context: Context) : View.OnTouchListener {

    private var mWindowManager: WindowManager? = null
    private var mParams: WindowManager.LayoutParams? = null


    private var mMessageBubbleView:MessageBubbleView?=null
    init {
        mMessageBubbleView= MessageBubbleView(context)
        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        mParams = WindowManager.LayoutParams()
        mParams!!.format=PixelFormat.TRANSPARENT//透明的背景
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {

            MotionEvent.ACTION_DOWN -> {
                view.visibility = View.INVISIBLE
                mWindowManager!!.addView(mMessageBubbleView,mParams)
                mMessageBubbleView!!.initPoint(event.rawX,
                        event.rawY-BubbleStatusUtils.getStatusBarHeight(context))
                mMessageBubbleView!!.setDragBitmap(getBitmapByView(view))
            }
            MotionEvent.ACTION_MOVE -> {

                mMessageBubbleView!!.updateDragPoint(event.rawX,
                        event.rawY-BubbleStatusUtils.getStatusBarHeight(context))

            }
            MotionEvent.ACTION_UP -> {


            }
        }

        return true
    }


    /**
     * 从一个View中获取一个Bitmap
     */
    private fun getBitmapByView(view: View): Bitmap {
        view.buildDrawingCache()
        return view.drawingCache
    }
}