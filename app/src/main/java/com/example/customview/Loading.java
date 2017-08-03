package com.example.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 铖哥 on 2017/8/2.
 */

public class Loading extends View{

    private int radius;
    private int color[] = new int[]{Color.argb(255,66, 133, 244),Color.argb(255,234, 67, 53),Color.argb(255,251, 188, 5),Color.argb(255,52, 168, 83)};

    private List<Circle> circleList;
    private Paint mPiant;

    private boolean isFinishing;

    public Loading(Context context) {
        super(context);
    }

    public Loading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPiant = new Paint();
        mPiant.setColor(Color.RED);
        mPiant.setStyle(Paint.Style.FILL);
        mPiant.setAntiAlias(true);
        mPiant.setStrokeWidth(10);
        radius = 20;

    }

    public Loading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawCircles(canvas,circleList);
    }

    class Circle{

        public float cX;
        public float cY;
        public float radius;
        public int color;


        public Circle(float x, float y, float radius,int color) {
            this.cX = x;
            this.cY = y;
            this.radius = radius;
            this.color = color;
        }

    }

    private void drawCircle(Canvas canvas , Circle c){


        mPiant.setColor(c.color-0xff);
        canvas.drawCircle(c.cX, c.cY, c.radius, mPiant);
    }

    private void drawCircles(Canvas canvas , List<Circle> circles){
        for(Circle c : circles){
            drawCircle(canvas,c);
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            height = width = 100;
            setMeasuredDimension(height,height);
        }


        if(circleList==null){
            circleList = new ArrayList<>();
            circleList.add(new Circle(width/4f,height/4f,radius,color[0]));
            circleList.add(new Circle(width*3/4f,height/4f,radius,color[1]));
            circleList.add(new Circle(width*3/4f,height*3/4f,radius,color[2]));
            circleList.add(new Circle(width/4f,height*3/4f,radius,color[3]));

             ip = new Myinterpolator(circleList.get(0),circleList.get(2).cX,circleList.get(2).cY,300,1);
             ip2 = new Myinterpolator(circleList.get(3),circleList.get(1).cX,circleList.get(1).cY,300,1);
             ip1 = new Myinterpolator(circleList.get(2),circleList.get(0).cX,circleList.get(0).cY,300,0);
             ip3 = new Myinterpolator(circleList.get(1),circleList.get(3).cX,circleList.get(3).cY,300,0);

            ip.start();
            ip1.start();
            ip2.start();
            ip3.start();
        }

    }

    Myinterpolator ip;
    Myinterpolator ip2;
    Myinterpolator ip1;
    Myinterpolator ip3;

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ip.exit();
        ip2.exit();
        ip3.exit();
        ip1.exit();
    }

    public void finish(){

        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    for (int i = 0; i < circleList.size(); i++) {
                        circleList.get(i).radius += 2;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }
}
