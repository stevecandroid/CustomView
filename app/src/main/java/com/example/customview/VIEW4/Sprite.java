package com.example.customview.VIEW4;

import android.util.Log;

/**
 * Created by 铖哥 on 2017/8/2.
 */

public class Sprite {

    public static final float gravity = 10;

    float clock = 0 ; //ms
    float DEFAULT_V = 10 ; // 10 pix / 10ms
    float DEFAULT_H;
    float DEFAULT_W;
    float velocity ;
    float height ;
    float width ;


    Float x;
    Float y;

    public Sprite(float width, float height , int parentHeight) {
        this.height = height;
        this.width = width;
        DEFAULT_H = height;
        DEFAULT_W = width;
        velocity = 10;

        DEFAULT_V = velocity = (float) Math.sqrt(parentHeight * 0.02);//10ms
        Log.e("Sprite",velocity+"");
    }


}
