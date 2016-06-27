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

public class WeatherView extends View {
    private static final int VIEW_HEIGHT = 80;
    private static final int TEXT_COLOR = Color.parseColor("#FB565B");
    private Context mContext;
    private Paint mPaint;
    private Rect mBounds;
    private int mFontSize = 40;
    private int mMarginLeft = 10;
    private String mTitle;
    private double mTemperature = Double.MIN_VALUE;

    public WeatherView(Context context, AttributeSet attrs, String title) {
        super(context, attrs);
        this.mContext = context;
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mBounds = new Rect();
        this.mTitle = title;
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
        mPaint.setColor(TEXT_COLOR);
        mPaint.setTextSize(mFontSize);
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mBounds);
        float textWidth = mBounds.width();
        float textHeight = mBounds.height();
        canvas.drawText(mTitle, mMarginLeft, getHeight() / 2 + textHeight / 2, mPaint);

        //Draw the temperature
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(mFontSize);
        String temp;
        if (mTemperature == Double.MIN_VALUE){
            temp = "Loading";
        }else {
            temp = String.valueOf(mTemperature) + "℃";
        }
        mPaint.getTextBounds(temp, 0, temp.length(), mBounds);
        float tempWidth = mBounds.width();
        float tempHeight = mBounds.height();
        canvas.drawText(temp, getWidth() - tempWidth - mMarginLeft, getHeight() / 2 + tempHeight / 2, mPaint);
    }

    public void setTemperatureToView(double temperature){
        this.mTemperature = temperature;
        invalidate();
    }

}
