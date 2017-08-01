package com.example.customview.TESTVIEW;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by 铖哥 on 2017/7/30.
 */

public class MyViewGroup extends ViewGroup {

    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            int w = view.getMeasuredWidth();
            int h = view.getMeasuredHeight();
            Log.e("MyViewGroup",w+"");
            Log.e("MyViewGroup",h+"");
            view.layout(i*w ,i*h   ,(i+1)*w   ,(i+1)*h  );
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec,heightMeasureSpec);

//        Log.e("MyViewGroup",MeasureSpec.getSize(heightMeasureSpec)+" on measure");
        if(widthMode == MeasureSpec.AT_MOST){
            width = 0 ;
            for (int i = 0; i < getChildCount(); i++) {
                width += getChildAt(i).getMeasuredWidth();
            }
        }

        if(heightMode == MeasureSpec.AT_MOST){
            height = 0 ;
            for (int i = 0; i < getChildCount(); i++) {
                height += getChildAt(i).getMeasuredHeight();
            }
        }
        Log.e("MyViewGroup",height + " and " + width);

            setMeasuredDimension(width,height);
    }


}
