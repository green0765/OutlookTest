package com.ms.outlook.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;


public class CalenderView extends View {
    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 7;
    private static final String[] WEEK_STRING = {"SUN","MON","TUE","WED","THU","FRI","SAT"};

    //Colors of every part of the view
    private int mDayColor = Color.parseColor("#000000");
    private int mSelectDayColor = Color.parseColor("#ffffff");
    private int mSelectBGColor = Color.parseColor("#0071C6");
    private int mCurrentColor = Color.parseColor("#ff0000");
    private int mCircleColor = Color.parseColor("#ff0000");
    private int mWeekendColor = Color.parseColor("#ff0000");

    //Size param
    private int mColumnSize,mRowSize;
    private int mDaySize = 18;
    private int mCircleRadius = 6;

    //Data param
    //Note: Month began with 0
    private int mCurrYear,mCurrMonth,mCurrDay;
    private int mSelYear,mSelMonth,mSelDay;

    private final ThreadLocal<Paint> mPaint = new ThreadLocal<>();
    private DisplayMetrics mDisplayMetrics;
    private int [][] daysString;
    private DateClick dateClick;
    private List<Integer> daysHasThingList;
    private Calendar mCalendar;

    public CalenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        mCalendar = Calendar.getInstance();
        mPaint.set(new Paint());
        mCurrYear = mCalendar.get(Calendar.YEAR);
        mCurrMonth = mCalendar.get(Calendar.MONTH);
        mCurrDay = mCalendar.get(Calendar.DATE);
        setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        daysString = new int[7][7];

        String dayString;
        int mMonthDays = getMonthDays(mSelYear,mSelMonth);
        mCalendar.clear();
        mCalendar.set(mSelYear,mSelMonth,1);
        int weekNumber = mCalendar.get(Calendar.DAY_OF_WEEK);

        // Draw the Week Label
        mPaint.get().setTextSize( (float) 0.7 * mDaySize * mDisplayMetrics.scaledDensity);
        for(int i=0;i < WEEK_STRING.length;i++){
            String text = WEEK_STRING[i];
            int startX = (int) (mColumnSize * i + (mColumnSize - mPaint.get().measureText(text))/2);
            int startY = (int) (mRowSize/2 - (mPaint.get().ascent() + mPaint.get().descent())/2);
            if(text.equals("SUN")|| text.equals("SAT")){
                mPaint.get().setColor(mWeekendColor);
            }else{
                mPaint.get().setColor(mDayColor);
            }
            canvas.drawText(text, startX, startY, mPaint.get());
        }

        mPaint.get().setTextSize(mDaySize * mDisplayMetrics.scaledDensity);
        //Draw the Date of previous month
        mPaint.get().setColor(Color.GRAY);
        for(int day = 0; day < weekNumber - 1; day++){
            mCalendar.add(Calendar.DATE, -1);
            dayString = String.valueOf(mCalendar.get(Calendar.DATE));
            int column = weekNumber - day - 2;
            int row = 1;
            int X = (int) (mColumnSize * column + (mColumnSize - mPaint.get().measureText(dayString))/2);;
            int Y = (int) (mRowSize * row + mRowSize/2 - (mPaint.get().ascent() + mPaint.get().descent())/2);
            canvas.drawText(dayString, X, Y, mPaint.get());
        }

        //Draw the Data of this month
        for(int day = 0;day < mMonthDays;day++){
            dayString = (day + 1) + "";
            int column = (day + weekNumber - 1) % 7;
            int row = (day + weekNumber - 1) / 7 + 1;
            daysString[row][column]=day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.get().measureText(dayString))/2);
            int startY = (int) (mRowSize * row + mRowSize/2 - (mPaint.get().ascent() + mPaint.get().descent())/2);
            if(dayString.equals(mSelDay+"")){
                //Draw the background for selected day
                int startRecX = mColumnSize * column;
                int startRecY = mRowSize * row;
                int endRecX = startRecX + mColumnSize/2;
                int endRecY = startRecY + mRowSize/2;
                mPaint.get().setColor(mSelectBGColor);
                canvas.drawCircle(endRecX, endRecY, mRowSize / 2, mPaint.get());
            }

            //Draw mark for the day that has events
            drawCircle(row,column,day + 1,canvas);

            //Make different font color for the date in different status
            if(dayString.equals(mSelDay+"")){
                mPaint.get().setColor(mSelectDayColor);
            }else if(dayString.equals(mCurrDay+"") && mCurrDay != mSelDay && mCurrMonth == mSelMonth){
                //Selected date is not today, make today's color different
                mPaint.get().setColor(mCurrentColor);
            }else{
                mPaint.get().setColor(mDayColor);
            }
            canvas.drawText(dayString, startX, startY, mPaint.get());

        }
        //Draw the Data of next month
        mPaint.get().setColor(Color.GRAY);
        mCalendar.clear();
        mCalendar.set(mSelYear,mSelMonth,mMonthDays);
        for(int day = weekNumber + mMonthDays - 1; day < 42; day++){
            mCalendar.add(Calendar.DATE, 1);
            dayString = String.valueOf(mCalendar.get(Calendar.DATE));
            int column = day % 7;
            int row = day / 7 + 1;
            int X = (int) (mColumnSize * column + (mColumnSize - mPaint.get().measureText(dayString))/2);;
            int Y = (int) (mRowSize * row + mRowSize/2 - (mPaint.get().ascent() + mPaint.get().descent())/2);
            canvas.drawText(dayString, X, Y, mPaint.get());
        }
    }

    private void drawCircle(int row,int column,int day,Canvas canvas){
        if(daysHasThingList != null && daysHasThingList.size() >0){
            if(!daysHasThingList.contains(day))
                return;
            mPaint.get().setColor(mCircleColor);
            float circleX = (float) (mColumnSize * column + mColumnSize*0.5);
            float circleY = (float) (mRowSize * row + mRowSize*0.9);
            canvas.drawCircle(circleX, circleY, mCircleRadius, mPaint.get());
        }
    }
    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int downX = 0,downY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode=  event.getAction();
        switch(eventCode){
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if(Math.abs(upX-downX) < 10 && Math.abs(upY - downY) < 10){
                    performClick();
                    doClickAction((upX + downX)/2,(upY + downY)/2);
                }else{
                    return super.onTouchEvent(event);
                }
                break;
        }
        return true;
    }

    /**
     * Init the width and height of the date
     */
    private void initSize(){
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
    }

    /**
     * Set the Selected date
     */
    public void setSelectYearMonth(int year,int month,int day){
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    private void doClickAction(int x,int y){
        int row = y / mRowSize;
        int column = x / mColumnSize;

        //Forbidden the click action on week label
        if(row == 0)
            return;

        //Change the Selected Day
        setSelectYearMonth(mSelYear,mSelMonth,daysString[row][column]);
        invalidate();

        //Do the Click Action
        if(dateClick != null){
            dateClick.onClickOnDate(mSelYear,mSelMonth,daysString[row][column]);
        }
    }

    /**
     * Change to the the last month
     */
    public void ChangeToLastMonth(){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 0){
            //January's last month is December
            year = mSelYear-1;
            month = 11;
        }else if(getMonthDays(year, month) == day){
            //if Selected day is the last one of the month,keep it also the last day of last month
            month = month-1;
            day = getMonthDays(year, month);
        }else{
            month = month-1;
        }
        setSelectYearMonth(year,month,day);
    }

    /**
     * Change to the next month
     */
    public void ChangeToNextMonth(){
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 11){
            //December's next month is January
            year = mSelYear+1;
            month = 0;
        }else if(getMonthDays(year, month) == day){
            //if Selected day is the last one of the month,keep it also the last day of last month
            month = month + 1;
            day = getMonthDays(year, month);
        }else{
            month = month + 1;
        }
        setSelectYearMonth(year,month,day);
    }

    public int getSelYear() {
        return mSelYear;
    }

    public int getSelMonth() {
        return mSelMonth;
    }

    public int getSelDay() {
        return this.mSelDay;
    }

    /**
     * Set the color of the date
     */
    public void setDayColor(int mDayColor) {
        this.mDayColor = mDayColor;
    }

    /**
     * Set the Font Color of the Selected date
     */
    public void setSelectDayColor(int mSelectDayColor) {
        this.mSelectDayColor = mSelectDayColor;
    }

    /**
     * Set the Background Color of the Selected date
     */
    public void setSelectBGColor(int mSelectBGColor) {
        this.mSelectBGColor = mSelectBGColor;
    }
    /**
     * Set the Today's Color
     */
    public void setCurrentColor(int mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
    }

    /**
     * Set the font size of date
     */
    public void setDaySize(int mDaySize) {
        this.mDaySize = mDaySize;
    }

   /**
     * Set the list of days that has events
     */
    public void setDaysHasThingList(List<Integer> daysHasThingList) {
        this.daysHasThingList = daysHasThingList;
    }

    public void setCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
    }

    /**
     * Interface for the Click Action of the date
     *
     */
    public interface DateClick{
        public void onClickOnDate(int year, int month, int day);
    }

    /**
     * Set the date click Callback
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }

    /**
     * Set Selected date to doday
     */
    public void setTodayToView(){
        setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
        invalidate();
    }

    /**
     * Set Selected date and invalidate
     * @param year
     * @param month
     * @param day
     */
    public void setDayToView(int year,int month,int day){
        setSelectYearMonth(year,month,day);
        invalidate();
    }

    /**
     * Get how many days there is in the certain month
     **/
    private int getMonthDays(int year, int month) {
        mCalendar.clear();
        mCalendar.set(year,month,1);
        return mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
