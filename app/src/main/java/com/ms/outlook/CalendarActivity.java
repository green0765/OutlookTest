package com.ms.outlook;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.ms.outlook.view.CalenderAdapter;
import com.ms.outlook.view.CalenderView;
import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends Activity {
    private ViewPager mViewPager;
    private CalenderView mCalenderView;
    private CalenderAdapter mCalenderAdapter;
    List<Integer> mDayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mViewPager = (ViewPager)findViewById(R.id.ViewPager);
        initView();

    }

    private void initView(){
        //New a CalenderView with the current date as the basic Calender
        mCalenderView = new CalenderView(getApplicationContext(), null);
        mDayList = new ArrayList<Integer>();
        mDayList.add(10);
        mDayList.add(15);
        mCalenderView.setDaysHasThingList(mDayList);

        mCalenderAdapter = new CalenderAdapter(getApplicationContext(), mCalenderView);
        mViewPager.setAdapter(mCalenderAdapter);
        mViewPager.setCurrentItem(CalenderAdapter.INIT_POSITION);
    }

}
