package com.example.customview.VIEW1;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Canvas;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 铖哥 on 2017/7/31.
 */

public class StackView extends FrameLayout {

    public static final int GAP = 20;
    public static final int MAX_PAGES_COUNT = 8;
    List<PagerTransformer> transformers;
    private ViewDragHelper mDragHelper;
    private Adapter mAdapter;
    private Point startPoint;

    private boolean needRemoved = false;
    private View preCapturedView = null;

    public StackView(@NonNull Context context) {
        super(context);
    }

    public StackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        startPoint = new Point();
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if ((int) child.getTag() == 0 || child.getTag() == null) {
                    return true;
                } else {
                    return false;
                }
                //tryCaptureView如果返回ture则表示可以捕获该view，
                // 你可以根据传入的第一个view参数决定哪些可以捕获
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                preCapturedView = capturedChild;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                int range = getMeasuredWidth();

                transformPage(changedView, (float) ((left + 0.0) / range), left < 0);
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

                if (Math.abs(releasedChild.getLeft()) < releasedChild.getWidth() / 2 && releasedChild.getLeft() < 0 ||
                        releasedChild.getLeft() > 0 && releasedChild.getRight() < getWidth() + releasedChild.getWidth() / 2) {
                    mDragHelper.settleCapturedViewAt(startPoint.x, startPoint.y);
                    invalidate();
                } else {

                    if (releasedChild.getLeft() < 0) {
                        mDragHelper.settleCapturedViewAt(-releasedChild.getWidth(), getHeight());
                    } else {
                        mDragHelper.settleCapturedViewAt(releasedChild.getWidth() + getWidth(), getHeight());
                    }
                    needRemoved = true;
                    invalidate();
                }
            }


        });
    }

    public StackView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        } else {
            if (needRemoved) {
                removeView(preCapturedView);
                viewHolderList.remove(0);
                preCapturedView = null;
                mAdapter.onPop(0,getChildCount());
//                for (int i = 0; i < getChildCount(); i++) {
//                    getChildAt(i).setTag((int)getChildAt(i).getTag()-1);
//                }
                needRemoved = false;
            }
        }
    }

    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        mAdapter.registerObserver(new ItemObserver());
        viewHolderList = null;
        mAdapter.mObserver.notifyChanged();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        Log.e("StackView", "ONLAYOUT");
        super.onLayout(changed, left, top, right, bottom);
//        mAdapter.mObserver.notifyChanged();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mAdapter != null)
            mAdapter.mObserver.notifyChanged();
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        startPoint.x = 0;
        startPoint.y = 0;
        Log.e("StackView", "MEASURE");
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("StackView", "ONDRAW");
    }

    private void transformPage(View view, float state, boolean isLeft) {

        if (transformers == null) {
            transformers = new ArrayList<>();
            transformers.add(new StackPagertransformer()); //DEAFULT
        }

        for (int i = 0; i < getChildCount(); i++) {
            for (PagerTransformer ptf :
                    transformers) {
                ptf.transform(getChildAt(i), state, isLeft);
            }
        }
    }

    public void setPagerTransforer(PagerTransformer... params) {
        transformers = Arrays.asList(params);
    }

    public static abstract class Adapter<VH extends ViewHolder> {

        private ItemObservable mObserver = new ItemObservable();

        public abstract VH onCreateViewHolder(ViewGroup parent, int pos);

        public abstract void onBindViewHolder(ViewHolder holder, int pos);

        public abstract int getItemtCount();

        public abstract void onPop(int position,int size);

        public void registerObserver(DataSetObserver observer) {
            mObserver.registerObserver(observer);
        }

        public void unregisterObserver(DataSetObserver observer) {
            mObserver.unregisterObserver(observer);
        }

        public void notifyDataSetChanged() {
            needChangeListHolder = true;
            mObserver.notifyChanged();
        }
    }

    public static abstract class ViewHolder {
        private View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }

        public void setItemId(View view, int id) {
            view.setTag(id);
        }

        public int getItemId(View view) {
            return (int) view.getTag();
        }

    }

    public static class ItemObservable extends DataSetObservable {
        @Override
        public void notifyChanged() {
            super.notifyChanged();
        }

        @Override
        public void notifyInvalidated() {
            super.notifyInvalidated();
        }
    }

    public static abstract class PagerTransformer {

        /**
         * @param view        //抓住的View Tag内包含其坐标
         * @param pos         //状态   移动中 栈内 移除
         * @param isSwiftLeft ./没用
         */
        abstract void transform(View view, float pos, boolean isSwiftLeft);
    }

    private int SHOW_PAGER_COUNT = 4; //default;
    private List<ViewHolder> viewHolderList = null;
    private static boolean needChangeListHolder = false;

    public class ItemObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            StackView.this.removeAllViewsInLayout();
//            int itemCount = mAdapter.getItemtCount();
//            viewHolderList = new ArrayList<>();
////            if(viewHolderList.size()==0)
//
//            removeAllViews();
//            for (int i = itemCount -1; i >= 0 ; i--) {
//                ViewHolder holder = mAdapter.onCreateViewHolder(StackView.this, i);
////                mAdapter.onBindViewHolder(holder, i);
////                addView(holder.itemView);
////                holder.itemView.setTag(i);
//                viewHolderList.add(holder);
////                viewHolderList.add(holder);
////                transformPage(holder.itemView, i, true);
//            }
//            onInvalidated();
////            for(int i = itemCount -1 - curTopPosition ; i > itemCount-5 &&  i>=0 ; i--){
////                View itemView  = viewHolderList.get(i).itemView;
////                mAdapter.onBindViewHolder(viewHolderList.get(i), i);
////                addView(itemView);
////                transformPage(itemView,i,true);
////            }
            int itemCount = mAdapter.getItemtCount();
            if(viewHolderList == null || needChangeListHolder) {
                viewHolderList = new ArrayList<>();
                for (int i = 0; i < itemCount; i++) {
                    ViewHolder holder = mAdapter.onCreateViewHolder(StackView.this, i);
                    viewHolderList.add(holder);
                }
            }

            for(int i = SHOW_PAGER_COUNT -1 ; i >= 0 ; i--){
                if(i < viewHolderList.size()) {
                    ViewHolder holder = viewHolderList.get(i);
                    mAdapter.onBindViewHolder(holder, i);
                    holder.itemView.setTag(i);
                    addView(holder.itemView);
                    transformPage(holder.itemView, i, true);
                }
            }
        }

    }

    public void setShowPagerCount(int count ){
        this.SHOW_PAGER_COUNT = count;
    }

}
