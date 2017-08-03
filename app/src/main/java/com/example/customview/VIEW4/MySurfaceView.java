package com.example.customview.VIEW4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.customview.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by 铖哥 on 2017/8/2.
 */

public class MySurfaceView extends SurfaceView implements  SurfaceHolder.Callback , Runnable{

    public static final int FREQUENCY = 1 ; // 600毫秒/60帧 每帧时间 ms
    public static final float GRAVITY = -2000f/(1000f*1000f);

    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint mPaint;

    public MySurfaceView(Context context) {
        this(context,null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true); //透明背景
        setZOrderOnTop(true);//透明背景
        holder.setFormat(PixelFormat.TRANSLUCENT);
//        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
    }

    Thread t ;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRun = true;
        sprite = new Sprite(bitmap.getWidth(),bitmap.getHeight(),getHeight());
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRun = false;
    }


    volatile Sprite sprite;
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.boy);
    private void drawSprite(Sprite sprite){
        canvas = holder.lockCanvas();

        if(sprite.x==null){
            sprite.x = getWidth()/2-sprite.width/2;
            sprite.y = getHeight()-sprite.height;
        }
        canvas.drawBitmap(bitmap,sprite.x,sprite.y,null);

        if(canvas!=null)
            holder.unlockCanvasAndPost(canvas);
    }


    boolean isRun ;
    @Override
    public void run() {
        drawBackGround();
        while(isRun) {

//
//            periodChangedSpriteHeight(sprite);
            clean();
            drawSprite(sprite);
            periodChangedSpriteHeight(sprite);
        }
    }

    private void  periodChangedSpriteHeight(Sprite sprite){
        sprite.clock += FREQUENCY;
//        sprite.velocity = sprite.DEFAULT_V - GRAVITY * sprite.clock;
//        if(sprite.height > sprite.DEFAULT_H){
//            sprite.height = sprite.DEFAULT_H;
//            sprite.clock = 0;
//        }

        sprite.y -= 10;
//        Log.e("MySurfaceView","clock = " + sprite.clock);
//        Log.e("MySurfaceView","gravity = " + GRAVITY );
//        Log.e("MySurfaceView", sprite.height+"");
//        Log.e("MySurfaceView",sprite.clock+"");
//          Log.e("MySurfaceView",""+ GRAVITY * Math.pow(sprite.clock,2)/2);
        Log.e("MySurfaceView",sprite.height+"");
    }

    private void clean(){
          canvas = holder.lockCanvas();
          canvas.drawColor(0, PorterDuff.Mode.CLEAR);
          if(canvas!=null){
            holder.unlockCanvasAndPost(canvas);
           }
    }



    private void drawBackGround(){
        mPaint.setColor(Color.WHITE);
        canvas = holder.lockCanvas();
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        if(canvas!=null){
            holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(500,500);
    }
}



