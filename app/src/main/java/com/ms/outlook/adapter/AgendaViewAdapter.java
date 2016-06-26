package com.ms.outlook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ms.outlook.Event;
import com.ms.outlook.R;
import com.ms.outlook.adapter.AgendaItemAdapter;
import com.ms.outlook.utils.EventUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgendaViewAdapter extends BaseAdapter{
    private int mYear,mMonth,mDay;
    private Calendar mCalendar;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public AgendaViewAdapter(Context context, int year, int month, int day){
        this.mContext = context;
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
        this.mCalendar = Calendar.getInstance();

        //Begin with the day before the selected day
        mCalendar.clear();
        mCalendar.set(mYear,mMonth,mDay);
        mCalendar.add(Calendar.DATE, -1);
    }

    public void setBeginData(int year,int month,int day){
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
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
        //Get the date of the item by position
        mCalendar.clear();
        mCalendar.set(mYear,mMonth,mDay);
        mCalendar.add(Calendar.DATE, position-1);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DATE);

        //Get event list of the date
        List<Event> eventsOfDay = EventUtils.getEventListByDate(year,month,day);
        ListView listView;
        if (convertView == null) {
            mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.agenda_item, null, false);
            listView = (ListView)convertView.findViewById(R.id.agendaItemListView);

        }else {
            listView = (ListView)convertView.findViewById(R.id.agendaItemListView);
        }
        AgendaItemAdapter agendaItemAdapter = new AgendaItemAdapter(mContext,eventsOfDay,year,month,day);
        listView.setAdapter(agendaItemAdapter);
        setListViewHeightBasedOnChildren(listView);
        return convertView;
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
