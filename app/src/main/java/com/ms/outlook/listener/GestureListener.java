package com.ms.outlook.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

    private OnFlingListener mOnFlingListener;

    /**用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发 **/
    @Override
    public final boolean onFling(final MotionEvent e1, final MotionEvent e2,
                                 final float speedX, final float speedY) {
        if (mOnFlingListener == null) {
            return super.onFling(e1, e2, speedX, speedY);
        }

        float XFrom = e1.getX();
        float YFrom = e1.getY();
        float XTo = e2.getX();
        float YTo = e2.getY();

        // 左右滑动的X轴幅度大于100，并且X轴方向的速度大于100
        if (Math.abs(XFrom - XTo) > 100.0f && Math.abs(speedX) > 100.0f) {
            // X轴幅度大于Y轴的幅度
            if (Math.abs(XFrom - XTo) >= Math.abs(YFrom - YTo)) {
                if (XFrom > XTo) {
                    // 下一个
                    mOnFlingListener.flingToNext();
                } else {
                    // 上一个
                    mOnFlingListener.flingToPrevious();
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public OnFlingListener getOnFlingListener() {
        return mOnFlingListener;
    }

    public void setOnFlingListener(OnFlingListener mOnFlingListener) {
        this.mOnFlingListener = mOnFlingListener;
    }
}
