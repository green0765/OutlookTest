package com.ms.outlook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

import com.ms.outlook.R;
import com.ms.outlook.listener.GestureListener;
import com.ms.outlook.listener.OnFlingListener;

public class CalenderFlipper extends ViewFlipper implements OnFlingListener {
    private GestureDetector mGestureDetector = null;
    private OnViewFlipperListener mOnViewFlipperListener = null;

    public CalenderFlipper(Context context) {
        super(context);
    }

    public CalenderFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnViewFlipperListener(OnViewFlipperListener mOnViewFlipperListener) {
        this.mOnViewFlipperListener = mOnViewFlipperListener;
        //初始化自定义滑动事件监听器
        GestureListener myGestureListener = new GestureListener();
        //绑定自定义的滑动监听器
        myGestureListener.setOnFlingListener(this);
        mGestureDetector = new GestureDetector(myGestureListener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (null != mGestureDetector) {
            return mGestureDetector.onTouchEvent(ev);
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    /**向下一条滑动事件**/
    @Override
    public void flingToNext() {
        if (null != mOnViewFlipperListener) {
            int childCnt = getChildCount();
            if (childCnt == 2) {
                removeViewAt(1);
            }
            addView(mOnViewFlipperListener.getNextView(), 0);
            if (childCnt != 0) {
                setInAnimation(getContext(), R.anim.push_left_in);
                setOutAnimation(getContext(), R.anim.push_left_out);
                setDisplayedChild(0);
            }
        }
    }

    /**向上一条滑动事件**/
    @Override
    public void flingToPrevious() {
        if (null != mOnViewFlipperListener) {
            int childCnt = getChildCount();
            if (childCnt == 2) {
                removeViewAt(1);
            }
            addView(mOnViewFlipperListener.getPreviousView(), 0);
            if (childCnt != 0) {
                setInAnimation(getContext(), R.anim.push_right_in);
                setOutAnimation(getContext(), R.anim.push_right_out);
                setDisplayedChild(0);
            }
        }
    }

    /**自定义View变化监听回调接口**/
    public interface OnViewFlipperListener {
        CalenderView getNextView();   //获取下一页View

        CalenderView getPreviousView();  //获取上一页View
    }
}
