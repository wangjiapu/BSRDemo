package com.example.pujw.bsrdemo;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class StatusBarUtils {

    public static void setStatusBarColor(Activity activity, int color) {

        //大于５．０以上的手机直接使用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            activity.getWindow().setStatusBarColor(color);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//４．４到５．０
            //全屏，没有电量等信息
            // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            /**
             * 创建一个Ｖｉｅｗ填充到原来bar的位置
             */
            View view = new View(activity);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity)));

            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(view);

            //android:fitsSystemWindows="true"每个布局都要写，麻烦

            ViewGroup contenView = activity.findViewById(android.R.id.content);
            View activityView = contenView.getChildAt(0);
            activityView.setFitsSystemWindows(true);


        }
    }

    /**
     * 获取状态栏高度
     * @param activity
     * @return
     */

    private static int getStatusBarHeight(Activity activity) {
        int result=0;
        Resources resources=activity.getResources();
        int resourceId=resources.getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId>0)
            result=resources.getDimensionPixelSize(resourceId);
        return result;
    }

    public static void setActivityTranslucent(Activity activity){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){


        }else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
