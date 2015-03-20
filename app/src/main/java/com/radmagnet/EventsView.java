package com.radmagnet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EventsView extends RelativeLayout {

    TextView mDayTv,mMonthTv,mSuffixTv;

    public EventsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EventsView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(com.radmagnet.R.layout.layout_events, this);
        mDayTv= (TextView) findViewById(com.radmagnet.R.id.day);
        mMonthTv= (TextView) findViewById(com.radmagnet.R.id.month);
        mSuffixTv= (TextView) findViewById(com.radmagnet.R.id.suffix);


    }



    public void setDate(Date date){

        try {

                String day = new SimpleDateFormat("d", Locale.US).format(date);
                String month = new SimpleDateFormat("MMM",Locale.US).format(date);

                mDayTv.setText(day);
                mMonthTv.setText(month.toUpperCase());
                mSuffixTv.setText(getDayNumberSuffix(Integer.parseInt(day)).toUpperCase());

            invalidate();
            requestLayout();


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();




    }


    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }


}
