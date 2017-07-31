package com.example.customview;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Point;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import java.util.Stack;

/**
 * Created by 铖哥 on 2017/7/31.
 */

public class StackView extends FrameLayout {

    public static final int GAP = 20;
    public static final int MAX_PAGES_COUNT = 8;

    private ViewDragHelper mDragHelper;

    private Adapter mAdapter;

    private Point startPoint ;

    public StackView(@NonNull Context context) {
        super(context);
    }

    public StackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        startPoint = new Point();
        stackViewHolder = new Stack<>();
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
                //tryCaptureView如果返回ture则表示可以捕获该view，
                // 你可以根据传入的第一个view参数决定哪些可以捕获
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                Log.e("StackView",left+"POSITION CHANGED");
                super.onViewPositionChanged(changedView, left, top, dx, dy);
            }

            /**
             * clampViewPositionHorizontal,clampViewPositionVertical,
             * 可以在该方法中对child移动的边界进行控制，
             * @param child
             * @param left 即将移动到的位置
             * @param dx
             * @return 最终范围
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            //同理
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }


            //松手回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                Log.e("StackView",releasedChild.getWidth() + " " + releasedChild.getLeft());

                if( Math.abs(releasedChild.getLeft()) < releasedChild.getWidth()/2 && releasedChild.getLeft() < 0 ||
                        releasedChild.getLeft() > 0 && releasedChild.getRight() < getWidth()+releasedChild.getWidth()/2){
                    mDragHelper.settleCapturedViewAt(startPoint.x,startPoint.y);
                    invalidate();
                }else{

                    if(releasedChild.getLeft()<0){
                        mDragHelper.settleCapturedViewAt( -releasedChild.getWidth(),getHeight());
                    }else{
                        mDragHelper.settleCapturedViewAt( releasedChild.getWidth()+getWidth(),getHeight());
                    }
                    invalidate();
                }
            }


        });
    }

    public StackView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private View preCapturedView = null;
    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
            if(preCapturedView!=null){
                preCapturedView = mDragHelper.getCapturedView();
            }
        }else {
            removeView(preCapturedView);
            preCapturedView = null;
        }
    }



    public void setAdapter(Adapter adapter){
        this.mAdapter = adapter;
        mAdapter.registerObserver(new ItemObserver());
        mAdapter.mObserver.notifyChanged();
    }

    public static abstract class Adapter<VH extends ViewHolder>{

        private ItemObservable mObserver = new ItemObservable();

        public abstract VH onCreateViewHolder(ViewGroup parent);

        public abstract void onBindViewHolder(ViewHolder holder , int pos);

        public abstract int getItemtCount();

        public void registerObserver(DataSetObserver observer){
            mObserver.registerObserver(observer);
        }

        public void unregisterObserver(DataSetObserver observer){
            mObserver.unregisterObserver(observer);
        }

        public void notifyDataSetChanged (){
            mObserver.notifyChanged();
        }

    }

    public static abstract class ViewHolder{
        private View itemView;

        public ViewHolder(View itemView){
            this.itemView = itemView;
        }
    }

    public static class ItemObservable extends DataSetObservable{
        @Override
        public void notifyChanged() {
            super.notifyChanged();
        }
    }


    private Stack<ViewHolder> stackViewHolder;
    public  class ItemObserver extends DataSetObserver{
        @Override
        public void onChanged() {
            StackView.this.removeAllViews();
            for( int i = 0 ;  i < mAdapter.getItemtCount(); i++) {
                ViewHolder holder = mAdapter.onCreateViewHolder(StackView.this);
                mAdapter.onBindViewHolder(holder,i);
                addView(holder.itemView);
            }
            

        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        startPoint.x = 0;
        startPoint.y = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
//        return super.onInterceptTouchEvent(ev);
    }

    public static abstract class PagerTransformer{
        abstract  void transform(View view , float pos , boolean isSwiftLeft);
    }


}
