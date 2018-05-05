package com.example.pujw.bsrdemo;

import android.content.Context;
import android.content.res.Resources;

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
}
