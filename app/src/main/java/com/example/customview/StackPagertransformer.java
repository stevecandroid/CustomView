package com.example.customview;

import android.support.annotation.FloatRange;
import android.support.annotation.Size;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.R.attr.width;

/**
 * Created by 铖哥 on 2017/7/31.
 */

public class StackPagertransformer extends StackView.PagerTransformer {

    int StackSize;
    double base ;
    float minScale ;
    float maxScale;


    public StackPagertransformer(int stackSize, @FloatRange(from = 0,to = 1,fromInclusive = false) float minScale , @FloatRange(from = 0,to = 1,fromInclusive = false) float maxScale) {
        StackSize = stackSize;
        this.minScale=minScale;
        this.maxScale  =maxScale;

        if(maxScale < minScale){
            throw new IllegalArgumentException("maxScale shoulde bigger than minScale");
        }

        base = Math.sqrt(Math.sqrt(minScale/maxScale));

    }

    public StackPagertransformer(){
        this(5,0.8f,0.85f);
    }

    @Override
    void transform( View view , float state, boolean isSwiftLeft) {

       View parent = (View) view.getParent();
        int width = parent.getMeasuredWidth();
        int height = parent.getMeasuredHeight();
        int itemPos = (int) view.getTag();
        view.setPivotX(width / 2);
        view.setPivotY(0);
        view.setTranslationY(20);

        if(state >=  0) {
            view.setScaleY(((float) Math.pow(base, StackSize - itemPos) * maxScale));
            view.setScaleX(((float) Math.pow(base, itemPos ) * maxScale));
        }else if(state < 0 && state > -1 ){
            view.setScaleY(((float) Math.pow(base,  StackSize - itemPos - state) * maxScale));
            view.setScaleX(((float) Math.pow(base, itemPos + state ) * maxScale));
        }

    }
}
