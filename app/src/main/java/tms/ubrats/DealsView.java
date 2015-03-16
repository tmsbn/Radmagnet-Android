package tms.ubrats;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by tmsbn on 3/16/15.
 */
public class DealsView extends RelativeLayout {

    TextView mPercentageTv;

    public DealsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DealsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.layout_specific_details, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mPercentageTv= (TextView) findViewById(R.id.percentage);

    }

    public void setPercentage(int percentage){
        if(percentage<=100)
            mPercentageTv.setText(percentage);
    }
}
