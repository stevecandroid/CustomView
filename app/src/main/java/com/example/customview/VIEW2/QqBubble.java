package com.example.customview.VIEW2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

/**
 * Created by 铖哥 on 2017/8/1.
 */

public class QqBubble extends View {

    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 500;

    private int width ;
    private int height ;

    private Paint mPiant = new Paint();

    public QqBubble(Context context) {
        super(context);
    }

    public QqBubble(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPiant.setColor(Color.RED);
        mPiant.setStyle(Paint.Style.FILL);
        mPiant.setAntiAlias(true);
        path = new Path();
    }

    public QqBubble(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawB(canvas);
    }

    float pressX ;
    float pressY ;
    Path path ;
    public void drawB(Canvas canvas){
        canvas.translate(0,0);
//        canvas.drawRect(0,0,getWidth(),getHeight(),mPiant);
//        canvas.translate(getWidth()/2,getHeight()/2);
//        mPiant.setStyle(Paint.Style.FILL);
//        canvas.drawCircle(width/2,height/2,50,mPiant);
//        canvas.drawCircle(pressX,pressY,20,mPiant);
//        path.moveTo(pressX,pressY-20);
//        path.quadTo((width/2 - pressX)/2 + pressX , (height/2 - pressY)/2 + pressY,width/2,height/2 - 50);
//        path.lineTo(width/2,height/2+50);
//        path.quadTo((width/2 - pressX)/2 + pressX , (height/2 - pressY)/2 + pressY,pressX,pressY+20);
//        path.close();
//        canvas.drawPath(path,mPiant);
        canvas.drawPoint(pressX,pressY,mPiant);
        path.reset();
    }

    public void drawSB(Canvas canvas , MotionEvent event){
        float pressX = event.getX();
        float pressY = event.getY();

    }

    private float calculateK(float x, float y){
        return (height/2-y)*(width/2-x);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        int act = event.getAction();

        switch (act){
            case MotionEvent.ACTION_DOWN:
                return true;

            case MotionEvent.ACTION_MOVE:
//                pressX = event.getX();
//                pressY = event.getY();
//                invalidate();
                calculate(new Circle(event.getX(),event.getY(),20));
                break;

            case MotionEvent.ACTION_UP:

                break;


        }
//        Log.e("QqBubble","X = " + event.getX()+ "  Y = " +event.getY() );


        return super.dispatchTouchEvent(event);
    }

    private float calculateRadius(){
        return 0f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST){
            width = DEFAULT_WIDTH;
        }

        if(heightMode == MeasureSpec.AT_MOST){
            height = DEFAULT_HEIGHT;
        }
        setMeasuredDimension(width,height);
    }

    public double calculate ( Circle m){
        Circle n = new Circle(width/2,height/2,50);
        
        double d =  Math.hypot( m.y - n.y , m.x - n.x );

        double c2x = d/( (n.radius/m.radius) - 1);
        double cos = m.radius/c2x;
//        Log.e("QqBubble", cos+"");
        double x = m.radius/cos;
        double y = m.radius/(Math.sqrt(1-cos*cos));

        pressX = (float) x;
        pressY = (float) y;
        invalidate();

//        Log.e("QqBubble",cos+"");?
        Log.e("QqBubble",y+"");
        return 1d;
    }

    class Vector {

        Point p1;
        Point p2;

        public Vector(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public double getDistance(){
            return Math.hypot( p1.y - p2.y , p1.x - p2.x );
        }
    }


    class Circle{
        public float x;
        public float y;
        public float radius;

        public Circle(float x, float y, float radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }
    }
}
