package com.example.customview;

import android.util.Log;

import com.example.customview.Loading.Circle;

/**
 * Created by 铖哥 on 2017/8/2.
 */

public class Myinterpolator {

    private float pieceX ;
    private float pieceY ;

    private boolean begin = true;

    private Circle curfrom;

    private float fromCx;
    private float toX;

    int type;

    public Myinterpolator(Loading.Circle from, float toX , float toY, int duration,int type) {

        this.curfrom = from;

        this.toX = toX;
        fromCx = from.cX;
        isIncrease = from.cX < toX;;

        if(isIncrease) {
            pieceX = (toX - from.cX) * 5f / duration;
            pieceY = (toY - from.cY) * 5f / duration;
        } else{
            pieceX = (from.cX - toX) * 5f / duration;
            pieceY = (from.cY - toY) * 5f / duration;
        }

        this.type = type;
    }

    boolean isIncrease ;
    public void start(){
        begin = true;
        if(type ==1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (begin) {
                        try {
                            Thread.sleep( 5);
                            if (isIncrease) {
                                curfrom.cX += pieceX;
                                curfrom.cY += pieceY;

                                if (curfrom.cX > toX) {
                                    isIncrease = false;
                                }
                            } else {
                                curfrom.cX -= pieceX;
                                curfrom.cY -= pieceY;
                                if (curfrom.cX < fromCx) {
                                    isIncrease = true;
                                }
                            }
                            Log.e("Myinterpolator", isIncrease + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (begin) {
                        try {
                            Thread.sleep(5);
                            Log.e("Myinterpolator", isIncrease + "");
                            if (isIncrease) {
                                curfrom.cX += pieceX;
                                curfrom.cY += pieceY;

                                if (curfrom.cX > fromCx) {
                                    isIncrease = false;
                                }
                            } else {
                                curfrom.cX -= pieceX;
                                curfrom.cY -= pieceY;
                                if (curfrom.cX < toX) {
                                    isIncrease = true;
                                }
                            }
                            Log.e("Myinterpolator", isIncrease + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();
        }
    }

    public void exit(){
        begin = false;
    }
}
