package com.example.customview.VIEW1;

import android.support.annotation.FloatRange;
import android.view.View;

/**
 * Created by 铖哥 on 2017/7/31.
 */

public class StackPagertransformer extends StackView.PagerTransformer {

    private int StackSize;
    private double base ;
    private float minScale ;
    private float maxScale;

    public static final float DEFAULT_SCALEY = 0.95f;
    public static final float DEFAULT_SCALEX = 0.9f;
    public static final int DEFAULT_TRANSLATIONY = 20;

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
        this(4,0.8f,0.85f);
    }

    @Override
    void transform( View view , float state, boolean isSwiftLeft) {

       View parent = (View) view.getParent();
        int width = parent.getMeasuredWidth();
        int height = parent.getMeasuredHeight();
        int itemState = (int) view.getTag();
        view.setPivotX(width / 2);
        view.setPivotY(0);
        view.setTranslationY(DEFAULT_TRANSLATIONY);

        if(state >=  0) {
            view.setScaleY(((float) Math.pow(base, StackSize - itemState) * DEFAULT_SCALEY));
            view.setScaleX(((float) Math.pow(base, itemState ) * DEFAULT_SCALEX));
        }else if(state < 0 && state > -1 ){
            view.setScaleY(((float) Math.pow(base,  StackSize - itemState - state) * DEFAULT_SCALEY));
            view.setScaleX(((float) Math.pow(base, itemState + state ) * DEFAULT_SCALEX));
        }

    }
}
