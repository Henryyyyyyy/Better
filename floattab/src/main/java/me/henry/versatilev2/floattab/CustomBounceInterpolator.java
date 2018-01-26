package me.henry.versatilev2.floattab;

import android.util.Log;
import android.view.animation.Interpolator;

/**
 * Created by henry on 2018/1/16.
 */

public class CustomBounceInterpolator implements Interpolator {
    double amplitude= 0.1D;
    double frequency= 0.8D;



    @Override
    public float getInterpolation(float input) {

        float data=(float) (-1.0D * Math.exp((double)(-input) / amplitude) *  Math.cos(frequency * (double)input) + (double)1);
        return data;

    }
}
