package com.example.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 铖哥 on 2017/7/30.
 */

public class MyView extends View {

    private Paint mPaint;
    public static final float RADIUS = 10;
    public static final float SINGLE_STEP = 2;
    List<Line> lines ;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);

        lines = new ArrayList<>();
//        Line line =  new Line(0,0);
//        Line line1 = new Line(0,1);
//        Line line2 = new Line(0,2);
        for( float i = 0 ; i < 1 ; i = i + 0.2f){
            Line l = new Line(0,i);
        
                l.destS = 100f;

            lines.add(l);
        }

        for( float i = 0.1f ; i < 1 ; i = i + 0.2f){
            Line l = new Line(0,i);

                l.destS = -100f;

            lines.add(l);
        }


//        Line line3 = new Line()
//        line.destS = 100f;
//        line1.destS = 100f;
//        line2.destS = -100f;
//        lines.add(line);
//        lines.add(line1);
//        lines.add(line2);

    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        canvas.translate(width/2,height/2);
        canvas.drawPoint(0,0,mPaint);

        drawLineCircle(lines,canvas);



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec); //默认实现EXACTLY模式
        //下面实现其他模式

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if( widthMode == MeasureSpec.AT_MOST){
            width = 200;
        }

        if( heightMode == MeasureSpec.AT_MOST){
            height = 200;
        }

        setMeasuredDimension(width,height);

    }

    private PointF calculateX(Line line){
        PointF p = new PointF();
        float dvalue = (float) (Math.pow(SINGLE_STEP,2)/Math.sqrt(1+Math.pow(line.k,2)));

        if(line.destS>0){
            p.x = line.x +  dvalue;
            p.y = line.k * line.x;
            line.x = p.x;
            line.y = p.y;
            Log.e("MyView",Math.sqrt(line.x * line.x + line.y * line.y)+"");
            if( Math.sqrt(line.x * line.x + line.y * line.y) > line.destS){
                line.destS = -line.destS;
                line.x = line.x - dvalue * 5;
                line.y = line.k * line.x;
            }
            Log.e("MyView",Math.sqrt(line.x * line.x + line.y * line.y)+"");

        }else{
            p.x = line.x -  dvalue;
            p.y = line.k * line.x;
            line.x = p.x;
            line.y = p.y;
            if( Math.sqrt(line.x * line.x + line.y * line.y) > -line.destS){

                line.destS = -line.destS;
                line.x = line.x + dvalue*2;
                line.y = line.k * line.x;
            }

        }
        
        return p;
    }

    private  void drawCircle(PointF c, Canvas canvas){
        canvas.drawCircle(c.x,c.y,RADIUS,mPaint);
    }

    private void drawLineCircle(List<Line> lines , Canvas canvas){

                for(int i = 0 ; i < lines.size() ; i++) {
                    drawCircle(calculateX(lines.get(i)), canvas);
                }

                postInvalidateDelayed(100);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e("MyView","DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("MyView","MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("MyView","UP");
                break;
        }
        return true;
    }



    class Line {
        public Float k ;
        public Float x ;
        public Float y;
        public Float destS;

        Line(float x , float k ){
            this.x = x;
            this.k = k;
            y = k * x;
        }

    }
}
