package com.ms.outlook.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

public class CalenderAdapter extends PagerAdapter {
    public static final int INIT_POSITION = 100000;
    private Context mContext;
    private CalenderView mBasicCalenderView;
    private Calendar mCalendar;

    public CalenderAdapter(Context context,CalenderView calenderView){
        this.mContext = context;
        this.mBasicCalenderView = calenderView;
        this.mCalendar = Calendar.getInstance();
    };

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CalenderView)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //Get the new CalenderView by certain position
        if(position == INIT_POSITION){
            return mBasicCalenderView;
        }

        mCalendar.set(mBasicCalenderView.getSelYear(),mBasicCalenderView.getSelMonth(),mBasicCalenderView.getSelDay());
        mCalendar.add(Calendar.MONTH, position - INIT_POSITION);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DATE);
        CalenderView calenderViewByPosition = new CalenderView(mContext, null);
        calenderViewByPosition.setDayToView(year,month,day);
        container.addView(calenderViewByPosition);
        return calenderViewByPosition;
    }
}
