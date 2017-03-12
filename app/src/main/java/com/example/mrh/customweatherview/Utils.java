package com.example.mrh.customweatherview;

import android.content.Context;

/**
 * Created by MR.H on 2017/3/12 0012.
 */

public class Utils {
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     */
    public static int px2dip (Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dip2px (Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
