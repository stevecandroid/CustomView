package com.example.customview;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 铖哥 on 2017/7/31.
 */

public class StackPagertransformer extends StackView.PagerTransformer {

    float pos ;
    int StackSize;
    double base = Math.sqrt(Math.sqrt(0.6/0.9));
    int width ;
    int height;

    public StackPagertransformer(float pos, int stackSize, int width , int height) {
        this.pos = pos;
        StackSize = stackSize;
        this.width=width;
        this.height  =height;
    }

    @Override
    void transform(View view, float pos, boolean isSwiftLeft) {

        view.setPivotX(width/2);
        view.setPivotY(0);
        view.setTranslationY(pos*20);
//        view.setTranslationX( width - width*(float) Math.pow(base,pos+1));
        Log.e("StackPagertransformer",height +"");
        view.setScaleX((float) Math.pow(base,pos+1));
        view.setScaleY(0.8f);

    }
}
