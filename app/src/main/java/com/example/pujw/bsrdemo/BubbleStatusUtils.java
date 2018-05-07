package com.example.pujw.bsrdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;

public class BubbleStatusUtils {
    /**
     * 获取状态栏高度
     * @param context
     * @return
     */

    public static int getStatusBarHeight(Context context) {
        int result=0;
        Resources resources=context.getResources();
        int resourceId=resources.getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId>0)
            result=resources.getDimensionPixelSize(resourceId);
        return result;
    }

    public static PointF getPointByPercent(PointF p1, PointF p2, float percent) {

        return new PointF(evaluateValue(percent,p1.x,p2.x),evaluateValue(percent,p1.y,p2.y));

    }


    private static float evaluateValue(float percent, Number start, Number end) {
        return start.floatValue()+(end.floatValue()-start.floatValue())*percent;

    }
}
