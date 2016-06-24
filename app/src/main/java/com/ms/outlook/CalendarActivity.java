package com.ms.outlook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.ms.outlook.view.CalenderFlipper;
import com.ms.outlook.view.CalenderView;
import com.ms.outlook.view.CalenderFlipper.OnViewFlipperListener;
import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends Activity implements OnViewFlipperListener{
    private CalenderFlipper mCalenderFlipper;
    private CalenderView mCalenderViewIn;
    List<Integer> mDayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mCalenderFlipper = (CalenderFlipper)findViewById(R.id.CalenderFlipper);
        mCalenderFlipper.setOnViewFlipperListener(this);

        mCalenderViewIn = new CalenderView(getApplicationContext(), null);
        mDayList = new ArrayList<Integer>();
        mDayList.add(10);
        mDayList.add(15);
        mCalenderViewIn.setDaysHasThingList(mDayList);
        mCalenderFlipper.addView(mCalenderViewIn);

    }

    @Override
    public CalenderView getNextView() {
        View viewInFlipper = mCalenderFlipper.getChildAt(0);
        CalenderView calenderView = new CalenderView(getApplicationContext(), null);
        calenderView.setDaysHasThingList(mDayList);
        if(viewInFlipper instanceof CalenderView){
            calenderView.setDayToView(((CalenderView)viewInFlipper).getSelYear(),((CalenderView)viewInFlipper).getSelMonth(),((CalenderView)viewInFlipper).getSelDay());
            calenderView.ChangeToNextMonth();
        }else{
            return null;
        }

        return calenderView;
    }

    @Override
    public CalenderView getPreviousView() {
        View viewInFlipper = mCalenderFlipper.getChildAt(0);
        CalenderView calenderView = new CalenderView(getApplicationContext(), null);
        calenderView.setDaysHasThingList(mDayList);
        if(viewInFlipper instanceof CalenderView){
            calenderView.setDayToView(((CalenderView)viewInFlipper).getSelYear(),((CalenderView)viewInFlipper).getSelMonth(),((CalenderView)viewInFlipper).getSelDay());
            calenderView.ChangeToLastMonth();
        }else{
            return null;
        }

        return calenderView;
    }
}
