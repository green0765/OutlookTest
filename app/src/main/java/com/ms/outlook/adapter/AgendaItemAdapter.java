package com.ms.outlook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.johnhiott.darkskyandroidlib.RequestBuilder;
import com.johnhiott.darkskyandroidlib.models.DataBlock;
import com.johnhiott.darkskyandroidlib.models.DataPoint;
import com.johnhiott.darkskyandroidlib.models.Request;
import com.johnhiott.darkskyandroidlib.models.WeatherResponse;
import com.ms.outlook.Event;
import com.ms.outlook.view.EventItemView;
import com.ms.outlook.view.WeatherView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AgendaItemAdapter extends BaseAdapter {
    public static final int VIEW_HEIGHT = 80;
    private static final int TODAY_TEXT_COLOR = Color.parseColor("#19487E");
    private static final int TODAY_BG_COLOR = Color.parseColor("#F4F9FE");
    private List<Event> mEventList;
    private Context mContext;
    private Calendar mCalendar;
    private List<EventItemView> mEventViewList;
    private int mYear,mMonth,mDay;
    private int mCurrYear,mCurrMonth,mCurrDay;
    private String mDateString;
    private boolean mShowWeather = false;
    private int mPositionOffset = 1;
    private DataBlock mDataBlockHourly;
    private WeatherView mMorningView,mAfternoonView,mEveningView;

    public AgendaItemAdapter(Context context, List<Event> list, int year, int month, int day) {
        this.mContext = context;
        this.mEventList = list;
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
        mCalendar = Calendar.getInstance();
        mCurrYear = mCalendar.get(Calendar.YEAR);
        mCurrMonth = mCalendar.get(Calendar.MONTH);
        mCurrDay = mCalendar.get(Calendar.DATE);
        mEventViewList = new ArrayList<EventItemView>();
        mCalendar.clear();
        mCalendar.set(mYear, mMonth, mDay);
    }

    private TextView createDateItem(){
        //Add the Date Item
        TextView dateTextView;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        dateTextView = new TextView(mContext);
        dateTextView.setLayoutParams(layoutParams);
        dateTextView.setHeight(VIEW_HEIGHT);
        dateTextView.setGravity(Gravity.CENTER_VERTICAL);
        dateTextView.setTextColor(Color.BLACK);
        String week = mCalendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.US);
        String month = mCalendar.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.US);
        int day = mCalendar.get(Calendar.DATE);
        mDateString =  week + "," + month + " " + day;

        //If the date is yesterday
        Calendar currCalender = Calendar.getInstance();
        currCalender.clear();
        currCalender.set(mCurrYear,mCurrMonth,mCurrDay);
        currCalender.add(Calendar.DATE, -1);
        if( mCalendar.compareTo(currCalender) == 0){
            mDateString = "YESTERDAY · " + mDateString;
        }

        //If the date is today
        currCalender.add(Calendar.DATE, 1);
        if( mCalendar.compareTo(currCalender) == 0){
            mDateString = "TODAY · " + mDateString;
            dateTextView.setTextColor(TODAY_TEXT_COLOR);
            dateTextView.setBackgroundColor(TODAY_BG_COLOR);
            mShowWeather = true;
            mPositionOffset = 4;
            getWeather();
            createWeatherViews();
        }

        //If the date is tomorrow
        currCalender.add(Calendar.DATE, 1);
        if( mCalendar.compareTo(currCalender) == 0){
            mDateString = "TOMORROW · " + mDateString;
            mShowWeather = true;
            mPositionOffset = 4;
            getWeather();
            createWeatherViews();
        }

        dateTextView.setText(mDateString);
        return dateTextView;
    }

    private TextView createNoEventView(){
        TextView noEventTextView;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        noEventTextView = new TextView(mContext);
        noEventTextView.setLayoutParams(layoutParams);
        noEventTextView.setTextColor(Color.BLACK);
        noEventTextView.setText("No Events");

        return noEventTextView;
    }

    @Override
    public int getCount() {
        int count = 1;
        if(mShowWeather){
            count += 3;
        }
        if(mEventList != null && mEventList.size() != 0){
            return mEventList.size() + count;
        }else{
            return count + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Create the Date View at position 0;
        if(position == 0){
            if(convertView == null)
                convertView = createDateItem();
            return convertView;
        }

        //Create weather View at position 1-3 if needed
        if(mShowWeather){
            switch (position){
                case 1:
                    if(convertView == null)
                        convertView = mMorningView;
                    return convertView;
                case 2:
                    if(convertView == null)
                        convertView = mAfternoonView;
                    return convertView;
                case 3:
                    if(convertView == null)
                        convertView = mEveningView;
                    return convertView;
            }
        }

        //Create the Event Item
        if(mEventList != null && mEventList.size() != 0){
            if(position >= mPositionOffset) {
                if (convertView == null){
                    EventItemView eventItemView = new EventItemView(parent.getContext(), null, mEventList.get(position - mPositionOffset));
                    mEventViewList.add(eventItemView);
                    convertView = eventItemView;
                }
            }
        }else {
            if(position == mPositionOffset){
                if(convertView == null)
                    convertView = createNoEventView();
            }
        }

        return convertView;
    }

    private void getWeather(){
        RequestBuilder weather = new RequestBuilder();
        Request request = new Request();
        request.setLat("32.00");
        request.setLng("-81.00");
        request.setUnits(Request.Units.US);
        request.setLanguage(Request.Language.PIG_LATIN);
        request.addExcludeBlock(Request.Block.HOURLY);

        weather.getWeather(request, new Callback<WeatherResponse>() {

            @Override
            public void success(WeatherResponse weatherResponse, Response response) {
                Log.i("Weather", "Get Weather success");
                mDataBlockHourly = weatherResponse.getHourly();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d("", "Error while calling: " + retrofitError.getUrl());
            }
        });

    }

    private void createWeatherViews(){
        List<DataPoint>  dataPoints;
        double morningTemp = 10;
        double afternoonTemp = 11;
        double eveningTemp = 12;
        if (mDataBlockHourly != null) {
            dataPoints = mDataBlockHourly.getData();
            morningTemp = dataPoints.get(8).getTemperature();
            afternoonTemp = dataPoints.get(15).getTemperature();
            eveningTemp = dataPoints.get(21).getTemperature();
        }else
        mMorningView = new WeatherView(mContext, null, "Morning",morningTemp);
        mAfternoonView = new WeatherView(mContext, null, "Afternoon", afternoonTemp);
        mEveningView = new WeatherView(mContext, null, "Evening", eveningTemp);



    }
}
