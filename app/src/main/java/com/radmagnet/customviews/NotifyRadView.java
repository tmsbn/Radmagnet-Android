package com.radmagnet.customviews;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.radmagnet.R;

import java.util.ArrayList;

/**
 * Created by tmsbn on 3/27/15.
 */
public class NotifyRadView extends TextView {

    final int ANIMATION_DURATION = 4500;
    boolean isAnimationStarted = false;

    View[] mViews;

    public NotifyRadView(Context context, AttributeSet attrs) {

        super(context, attrs);
        setBackgroundResource(R.drawable.rounded_rectangle_with_red_border);
    }

    public void disableViewsOnAnimation(View... views){
        mViews=views;
    }

    private void toggleEnableViews(boolean enable){
        for(View view:mViews){
            view.setEnabled(enable);
        }
    }


    public void showTempMessage(String message) {

        setText(message);
        setAlpha(0);

        final int height = (int) (getHeight() * 1.5);

        post(new Runnable() {
            @Override
            public void run() {

                //if a previous animation has already started
                if (isAnimationStarted)
                    return;

                animate().translationYBy(height)
                        .setDuration(ANIMATION_DURATION)
                        .alpha(1)
                        .setStartDelay(100)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                isAnimationStarted = true;
                                toggleEnableViews(false);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                isAnimationStarted = false;
                                toggleEnableViews(true);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .setInterpolator(new CustomInterpolator(0.15f));




            }


        });

    }

    public class CustomInterpolator implements Interpolator {

        float mFactor;

        public CustomInterpolator(float factor) {

            if (factor > 1) {
                throw new IllegalArgumentException();
            }
            mFactor = factor;
        }

        @Override
        public float getInterpolation(float t) {
            float inverse = 1f - mFactor;
            float reciprocal = 1f / mFactor;

            if (t <= mFactor)
                return (reciprocal * t);

            else if (t >= inverse) {
                return (reciprocal) * (1 - t);
            } else
                return 1;
        }
    }


}
