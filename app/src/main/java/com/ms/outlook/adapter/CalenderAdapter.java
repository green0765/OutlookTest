package com.ms.outlook.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ms.outlook.utils.EventUtils;
import com.ms.outlook.view.CalenderView;

import java.util.Calendar;

public class CalenderAdapter extends PagerAdapter {
    public static final int INIT_POSITION = 100000;
    private Context mContext;
    private CalenderView mBasicCalenderView;
    private Calendar mCalendar;
    private CalenderView.DateClick mDateClick;

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
        Log.i("Tag", "Destroy Position:" + position);
        container.removeView((CalenderView)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //Get the new CalenderView by certain position
        if(position == INIT_POSITION){
            mBasicCalenderView.setDateClick(mDateClick);
            container.addView(mBasicCalenderView);
            return mBasicCalenderView;
        }

        mCalendar.set(mBasicCalenderView.getSelYear(),mBasicCalenderView.getSelMonth(),mBasicCalenderView.getSelDay());
        mCalendar.add(Calendar.MONTH, position - INIT_POSITION);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DATE);

        CalenderView calenderViewByPosition = new CalenderView(mContext, null);
        calenderViewByPosition.setDaysHasThingList(EventUtils.getEventListOfMonth(year, month));
        calenderViewByPosition.setDayToView(year,month,day);
        calenderViewByPosition.setDateClick(mDateClick);
        container.addView(calenderViewByPosition);
        return calenderViewByPosition;
    }

    public CalenderView.DateClick getDateClick() {
        return mDateClick;
    }

    public void setDateClick(CalenderView.DateClick mDateClick) {
        this.mDateClick = mDateClick;
    }
}
