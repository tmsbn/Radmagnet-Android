package com.radmagnet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.TextView;

import hugo.weaving.DebugLog;

/**
 * Created by tmsbn on 3/27/15.
 */
public class NotifyRadView extends TextView {

    final int ANIMATION_DURATION = 500;
    final int SHOW_DURATION = 4000;

    public NotifyRadView(Context context, AttributeSet attrs) {

        super(context, attrs);
        setBackgroundResource(R.drawable.rounded_rectangle_with_red_border);
    }


    public void showTempMessage(String message) {

        setText(message);
        setAlpha(0);

        final int height = (int) (getHeight() * 1.5);

        post(new Runnable() {
            @Override
            public void run() {


                animate().translationYBy(height)
                        .setDuration(ANIMATION_DURATION)
                        .alpha(1)
                        .setStartDelay(100)
                        .withEndAction(new Runnable() {

                            @Override
                            public void run() {
                                animate().translationYBy(-height)
                                        .alpha(0)
                                        .setDuration(ANIMATION_DURATION)
                                        .setStartDelay(SHOW_DURATION);

                            }
                        });


            }


        });

    }

    @DebugLog
    public class CustomInterpolator implements Interpolator {

        float mFactor;

        public CustomInterpolator(float factor) {

            if (factor > 1) {
                throw new IllegalArgumentException();
            }
            mFactor = factor;
        }


        public float getInterpolation(float t) {
            if (t <= mFactor)
                return (mFactor * 10 * t);

            else if (t >= (1 - mFactor)) {
                return (mFactor * 10 - (mFactor * 10 * t));
            } else
                return 1;
        }
    }
}
