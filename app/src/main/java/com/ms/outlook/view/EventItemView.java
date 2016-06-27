package com.ms.outlook.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.ms.outlook.Event;

public class EventItemView extends View implements View.OnClickListener {
    private static final int VIEW_HEIGHT = 180;
    private static final int POINT_COLOR = Color.parseColor("#B13560");
    private int mFontSize = 40;
    private int mMarginLeft = 10;
    private int mCostTimeFontSize = 30;
    private int mCostTimeOffsetY = 30;
    private int mCircleOffset = 200;
    private int mLocationOffsetY = 60;
    private int mCircleRadius = 20;

    private Context mContext;
    private Event mEvent;
    private Paint mPaint;
    private Rect mBounds;


    public EventItemView(Context context, AttributeSet attrs, Event event) {
        super(context, attrs);
        this.mContext = context;
        this.mEvent = event;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new Rect();
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = getResources().getDisplayMetrics().densityDpi * 300;
        }
        setMeasuredDimension(widthSize, VIEW_HEIGHT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draw the Begin Time
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(mFontSize);
        String text = mEvent.getBeginTime();
        mPaint.getTextBounds(text, 0, text.length(), mBounds);
        float textWidth = mBounds.width();
        float textHeight = mBounds.height();
        canvas.drawText(text, mMarginLeft, getHeight() / 2, mPaint);

        //Draw the Red Point
        mPaint.setColor(POINT_COLOR);
        float circleX = getWidth() / 2 - mCircleOffset;
        float circleY = getHeight() / 2;
        canvas.drawCircle(circleX, circleY, mCircleRadius, mPaint);

        // Draw the Event Title
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(mFontSize);
        String title = mEvent.getTitle();
        mPaint.getTextBounds(title, 0, title.length(), mBounds);
        float titleHeight = mBounds.height();
        canvas.drawText(title, getWidth() / 2, getHeight() / 2 , mPaint);

        //Draw the Time Cost
        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(mCostTimeFontSize);
        String timeCost = mEvent.getTimeCost();
        canvas.drawText(timeCost, mMarginLeft, getHeight() / 2 + textHeight / 2 + mCostTimeOffsetY , mPaint);

        //Draw the Location
        String location = mEvent.getLocation();
        if(location != null){
            mPaint.setColor(Color.GRAY);
            mPaint.setTextSize(mFontSize);
            canvas.drawText(location, getWidth() / 2, getHeight() / 2  + mLocationOffsetY, mPaint);
        }
    }

    @Override
    public void onClick(View v) {
        // Popup a Dialog to Edit the Event
        popupEventEditDialog();
    }

    private void popupEventEditDialog(){
        //Just show a toast instead of dialog now
        //TODO popup a dialog to edit the Event
        Toast.makeText(mContext, "Popup Dialog and Edit the Event", Toast.LENGTH_LONG).show();
    }
}
