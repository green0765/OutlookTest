package com.ms.outlook;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.johnhiott.darkskyandroidlib.ForecastApi;
import com.ms.outlook.adapter.AgendaViewAdapter;
import com.ms.outlook.adapter.CalenderAdapter;
import com.ms.outlook.utils.EventUtils;
import com.ms.outlook.view.CalenderView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends Activity implements CalenderView.DateClick {
    private static final String FORECAST_API_KEY = "15777627e17eea2af883007efbfdae93";
    private ViewPager mViewPager;
    private CalenderView mCalenderView;
    private ListView mAgendaView;
    private TextView mDateTextView;
    private CalenderAdapter mCalenderAdapter;
    private AgendaViewAdapter mAgendaItemAdapter;
    private Calendar mCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ForecastApi.create(FORECAST_API_KEY);
        setContentView(R.layout.activity_calendar);
        mCalendar = Calendar.getInstance();
        mViewPager = (ViewPager)findViewById(R.id.ViewPager);
        mAgendaView = (ListView)findViewById(R.id.ListView) ;
        initCalenderViewPager();
        initListView(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE));

        mDateTextView = (TextView) findViewById(R.id.dateTextView);
        mDateTextView.setTextColor(Color.BLACK);
        setTextView(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH));
    }

    /**
     * Set header TextView's Date, it should change one calender scroll to another month
     * @param year
     * @param month
     */
    private void setTextView(int year, int month){
        mCalendar.set(Calendar.MONTH, month);
        String text =  mCalendar.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.US) + " " + year;
        mDateTextView.setText(text);

    }

    /**
     * Init the scrollable Calender View
     */
    private void initCalenderViewPager(){
        //New a CalenderView with the current date as the basic Calender
        mCalenderView = new CalenderView(getApplicationContext(), null);
        List<Integer> dayList = EventUtils.getEventListOfMonth( mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH));
        mCalenderView.setDaysHasThingList(dayList);

        mCalenderAdapter = new CalenderAdapter(getApplicationContext(), mCalenderView);
        mCalenderAdapter.setDateClick(this);
        mViewPager.setAdapter(mCalenderAdapter);

        //Set ViewPager's currentItem to INIT_POSITION to make the Pager can scroll to both previous and next.
        mViewPager.setCurrentItem(CalenderAdapter.INIT_POSITION);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.i("Tag", "Position:" + position);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, position - CalenderAdapter.INIT_POSITION);
                setTextView(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int position) {

            }
        });
    }

    /**
     * Init the Agenda View
     */
    private void initListView(int year, int month, int day){
        mAgendaItemAdapter = new AgendaViewAdapter(getApplicationContext(),year, month,day);
        mAgendaView.setAdapter(mAgendaItemAdapter);
    }


    @Override
    public void onClickOnDate(int year, int month, int day) {
        initListView(year, month, day);
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}

