package tms.ubrats;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
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

    public EventsView(Context context,ViewGroup viewGroup) {
        super(context);
        init(context,viewGroup);
    }

    private void init(Context context,ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.layout_events, this);
        mDayTv= (TextView) findViewById(R.id.day);
        mMonthTv= (TextView) findViewById(R.id.month);
        mSuffixTv= (TextView) findViewById(R.id.suffix);


    }



    public void setDate(Date date){

        try {

                String day = new SimpleDateFormat("d", Locale.US).format(date);
                String month = new SimpleDateFormat("MMM",Locale.US).format(date);

                mDayTv.setText(day);
                mMonthTv.setText(month);
                mSuffixTv.setText(getDayNumberSuffix(Integer.parseInt(day)));


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
