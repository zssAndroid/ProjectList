package com.zss.slidingmenu2;

import android.animation.FloatEvaluator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 类名:      DragLayout
 * 创建者:    PoplarTang
 * 创建时间:  2016/11/3.
 * 描述：     TODO
 */

public class DragLayout2 extends FrameLayout {

    ViewDragHelper mDragHelper;
    private View redView;
    private View greenView;

    public DragLayout2(Context context) {
        this(context, null);
    }

    public DragLayout2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 1 创建ViewDragHelper
        mDragHelper = ViewDragHelper.create(this, 1.0f, callback);
    }

    // 2 传递拦截判断, 处理触摸事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;// 消费事件.
    }

    // 3 重写事件回调
    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            // 将left限制在 0 -> 100之间
//            if(left < 0){
//                left = 0;
//            }else if(left > 100){
//                left = 100;
//            }
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }

        FloatEvaluator evaluator = new FloatEvaluator();

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if(changedView == redView){ // 触摸的是红色布局
                // 让绿色布局在当前基础上产生dx的水平偏移
                ViewCompat.offsetLeftAndRight(greenView, dx);
                // 让绿色布局在当前基础上产生dy的竖直偏移
                ViewCompat.offsetTopAndBottom(greenView, dy);

                float percent = left * 1.0f / 100f;
                System.out.println("left: " + left + " percent: " + percent);

                // 1.0 -> 0.3
                // 设置绿色布局的缩放比例
                ViewCompat.setScaleX(greenView, evaluator.evaluate(percent, 1.0f, 0.3f));
                ViewCompat.setScaleY(greenView, evaluator.evaluate(percent, 1.0f, 0.3f));
                // 0 -> 360
                ViewCompat.setRotation(greenView, evaluator.evaluate(percent, 0, 360));
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if(releasedChild == redView){
//                getChildAt(0).layout(0, 0, 80, 80);
                // 重置redView的位置
                resetView();
            }
        }
    };

    private void resetView() {
        // a.触发平滑动画
        if(mDragHelper.smoothSlideViewTo(redView, 0, redView.getTop())){
            // true , 当前redView还没有移动到指定位置(0,0) ,需要重绘界面
            ViewCompat.postInvalidateOnAnimation(DragLayout2.this); // drawChild -> child.draw -> computeScroll
        }
    }

    // b. 维持动画的继续
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mDragHelper.continueSettling(true)){
            // true , 当前redView还没有移动到指定位置(0,0) ,需要重绘界面
            ViewCompat.postInvalidateOnAnimation(DragLayout2.this); // drawChild -> child.draw -> computeScroll
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // 当布局从xml中填充完毕时, 执行
        redView = getChildAt(0);
        greenView = getChildAt(1);

    }
}
