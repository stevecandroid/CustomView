package com.example.customview.VIEW3;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 铖哥 on 2017/8/1.
 */

public class Lock extends View {

    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 500;

    public static final int DEFAULT_ITEM_COUNT = 9;
    public static final int DEFAULT_CIRCLE_RADIUS = 150;
    public static final int DEFAULT_TOUCH_COLOR = Color.RED;
    public static final int DEFAULT_NONTOUCH_COLOR = Color.BLUE;
    public static final int DEFAULT_LINE_COLOR = Color.BLACK;
    public static final int DEFAULT_SECRET_SIZE = 4;

    public int touchColor ;
    public int nonTouchColor;
    public int lineColor;

    public int secretSize;
    public int itemCount;
    public int circleRadius;
    public int gap = 0 ;

    private int width ;
    private int height ;

    private Paint mPiant ;

    private List<Circle> circles;
    private List<Line> lines;

    private List<Integer> order ;

    private Context mContext;


    public Lock(Context context) {
        super(context);
    }

    public Lock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Lock);
        itemCount = typedArray.getInteger(R.styleable.Lock_itemCount,DEFAULT_ITEM_COUNT);
        circleRadius = typedArray.getInteger(R.styleable.Lock_circleRadius,DEFAULT_CIRCLE_RADIUS);
        touchColor = typedArray.getColor(R.styleable.Lock_touchColor,DEFAULT_TOUCH_COLOR);
        nonTouchColor = typedArray.getColor(R.styleable.Lock_nonTouchColor,DEFAULT_NONTOUCH_COLOR);
        lineColor = typedArray.getColor(R.styleable.Lock_lineColor,DEFAULT_LINE_COLOR);

        mPiant = new Paint();
        mPiant.setColor(Color.RED);
        mPiant.setStyle(Paint.Style.FILL);
        mPiant.setAntiAlias(true);
        mPiant.setStrokeWidth(10);
//        mPiant.setShadowLayer(8,1,1,Color.BLACK);
        mPiant.setStrokeJoin(Paint.Join.ROUND);


        lines = new ArrayList<>();
        order = new ArrayList<>();

    }

    public Lock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float curX;
    private float curY;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircles(canvas,circles);
        drawLines(canvas,lines);
    }

    private void drawCircle(Canvas canvas , Circle c){
        if(c.isTouch) {
            mPiant.setColor(touchColor);
        }else{
            mPiant.setColor(nonTouchColor);
        }
        canvas.drawCircle(c.cX, c.cY, c.radius, mPiant);
        if(c.radius < circleRadius){
            c.radius  += 20;
            if(c.radius > circleRadius){
                c.radius = circleRadius;
            }
            invalidate();
        }
    }

    private void drawCircles(Canvas canvas , List<Circle> circles){
        for(Circle c : circles){
            drawCircle(canvas,c);
        }
    }

    private void drawLine(Canvas canvas , Line line){
        mPiant.setColor(lineColor);
        if(line.c2 != null){
            canvas.drawLine(line.c1.cX,line.c1.cY,line.c2.cX,line.c2.cY,mPiant);
        }else{
            canvas.drawLine(line.c1.cX,line.c1.cY,curX,curY,mPiant);
        }
    }

    private void drawLines(Canvas canvas, List<Line> lines){
        mPiant.setStrokeWidth(14);
        for (Line l : lines) {
            drawLine(canvas,l);
        }
        mPiant.setStrokeWidth(10);
    }

    class Line{

        Circle c1;
        Circle c2;

        public Line(Circle c1) {
            this.c1 = c1;
        }

        public Line(Circle c1, Circle c2) {
            this.c1 = c1;
            this.c2 = c2;
        }
    }

    class Circle{

        private int id;
        public float cX;
        public float cY;
        public float radius;

        private boolean isTouch;


        public Circle(float x, float y, float radius) {

            this.cX = x;
            this.cY = y;
            this.radius = radius;
        }

        public boolean isInclude(float x, float y){
            if(Math.hypot(y-cY,x-cX) < radius){
                return true;
            }else{
                return false;
            }
        }

        public boolean isTouch() {
            return isTouch;
        }

        public void setTouch(boolean touch) {
            isTouch = touch;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }



    @Override
        public boolean dispatchTouchEvent(MotionEvent event) {

            int act = event.getAction();

            switch (act){
                case MotionEvent.ACTION_DOWN:

                    return true;

                case MotionEvent.ACTION_MOVE:

                    for (int i = 0; i < circles.size(); i++) {
                        Circle c = circles.get(i);
                         if(c.isInclude(event.getX(), event.getY()) && !c.isTouch){
                             c.setTouch(true);
                             order.add(c.getId());
                         }
                         curX = event.getX();
                         curY = event.getY();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    for (int i = 0; i < circles.size(); i++) {
                        Circle c = circles.get(i);
                            c.setTouch(false);
                    }

                    String secret = getStringFromLocal();
                    if(onFinshInputListener!=null) {
                        if(secret==null) {
                            onFinshInputListener.onFinish(false, false, order);
                        }else{
                            onFinshInputListener.onFinish(true,secret.equals(getSecret(order)), order);
                        }
                    }
                    order.clear();

                    break;

            }

            lines.clear();
            makeUpLines();
            invalidate();
            return super.dispatchTouchEvent(event);
        }

    private void makeUpLines(){
        if(order.size()>=2){
            for (int i = 0; i < order.size()-1; i += 1) {
                lines.add(new Line(circles.get(order.get(i)),circles.get(order.get(i+1))));
            }
            lines.add(new Line(circles.get(order.get(order.size()-1))));
        }else if (order.size() == 1){
            lines.add(new Line(circles.get(order.get(order.size()-1))));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

//        if(width > height){
//            if(6 * circleRadius > height){
//                circleRadius = height/3;
//                gap = 0 ;
//            }else {
//                gap = (width - 6 * circleRadius) / 4;
//            }
//        }else{
//            if(6 * circleRadius > width){
//                circleRadius = width/3;
//                gap = 0 ;
//            }else {
//                gap = (height - 6 * circleRadius) / 4;
//            }
//        }

        gap = (width - 6 * circleRadius) / 4;


        if(circles == null) {
            circles = new ArrayList<>();
            int id = 0 ;
            for (int i = 1; i < 4; i++) {
                for (int j = 1; j < 4; j++) {
                    Circle c = new Circle((2 * j - 1) * circleRadius + j * gap, (2 * i - 1) * circleRadius + i * gap, 0);
                    c.setId(id);
                    circles.add(c);
                    id++;
                }
            }
        }

    }

    private onFinshInputListener onFinshInputListener;
    public interface onFinshInputListener{
        void onFinish(boolean isSecretSetup , boolean isCorrect , List<Integer> results);
    }

    public void setOnFinshInputListener(Lock.onFinshInputListener onFinshInputListener) {
        this.onFinshInputListener = onFinshInputListener;
    }

    public void setSecret(List<Integer> order){
        SharedPreferences save = mContext.getSharedPreferences("Secret",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = save.edit();
        String secret = getSecret(order);
        editor.putString("Secret",secret);
        editor.apply();
    }

    public String getSecret(List<Integer> order){
        StringBuffer sb = new StringBuffer();
        for (Integer i :order) {
            sb.append(i);
        }return sb.toString();
    }
    
    public String getStringFromLocal(){
        SharedPreferences save = mContext.getSharedPreferences("Secret",Context.MODE_PRIVATE);
        String str = save.getString("Secret",null);
        return str;
    }

    public void resetSecret(){
        SharedPreferences save = mContext.getSharedPreferences("Secret",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = save.edit();
        editor.remove("Secret");
    }
    
    


}
