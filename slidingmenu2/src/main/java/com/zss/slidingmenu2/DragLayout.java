package com.zss.slidingmenu2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 类名:      DragLayout
 * 创建者:    zss
 * 创建时间:  2016/11/2.
 * 描述：     TODO
 */

public class DragLayout extends FrameLayout {

    private ViewDragHelper mDragHelper;
    private ViewGroup mLeftMenu;    // 左菜单
    private ViewGroup mMainContent; // 主面板
    private int mHeight; // 控件高度
    private int mWidth;  // 控件宽度
    private int mRange;  // 拖拽范围
    private Status status = Status.Close; // 默认关闭

    // 状态枚举
    public static enum Status {
        Close,
        Open,
        Draging;
    }

    // 代码new
    public DragLayout(Context context) {
        this(context, null);
    }

    // xml布局文件
    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    // xml布局, style
    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化ViewDragHelper 3个步骤

        // 1. 创建ViewDragHelper
        // forParent : 被拖拽的控件的父布局(控件).
        // sensitivity : 敏感度, 1.0默认, 越大越敏感.
        // Callback : 回调, 提供信息及接受事件.
        mDragHelper = ViewDragHelper.create(this, 1.0f, callback);
    }

    // 2. 传递拦截判断, 处理触摸事件
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

    // 3. 重写事件回调
    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        /**
         * 尝试捕获View
         * a. 返回值决定了子控件能否被拖拽
         * @param child 被拖拽的子控件
         * @param pointerId 多点触摸手指id
         * @return  子控件是否可以被拖拽
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            System.out.println("tryCaptureView: " + child);
//            return child == mMainContent;
            return true;
        }

        // 返回控件水平方向拖拽范围, 不会真正限制拖拽位置
        // 1. 决定了回弹动画执行时长
        // 2. 决定了水平方向是否可以拖拽
        // 设置 > 0 的值即可.
        @Override
        public int getViewHorizontalDragRange(View child) {
            return mRange;
        }

        /**
         * 修正视图水平方向的位置
         * b.返回值决定了将要移动到的位置. 限制拖拽范围
         * @param child 被拖拽的子控件
         * @param left 建议移动到的水平位置
         * @param dx 将要发生的变化量, 向右+, 向左-
         * @return 最终要移动到的位置
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            int oldLeft = child.getLeft();
            System.out.println("clampViewPositionHorizontal: " +
                    "oldLeft: " + oldLeft + " left: " + left + " dx: " + dx);

            if(child == mMainContent){ // 滑动主面板
                // 限制拖拽范围在 0 -> mRange之内
                left = fixLeft(left);
            }
            return left;
        }

        /**
         * c. 当控件位置发生变化了, 此方法会被执行
         * 传递事件, 状态更新, 伴随动画
         * @param changedView 位置发生变化的控件
         * @param left  控件最新的水平位置
         * @param top   控件最新的竖直位置
         * @param dx    刚刚发生的水平偏移量
         * @param dy    刚刚发生的竖直偏移量
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            System.out.println("onViewPositionChanged: left: " + left + " dx: " + dx);
            // 左菜单滑动时, 让主面板运动, 固定自己
            if(changedView == mLeftMenu){
                // 固定自己, 放回原来位置
                mLeftMenu.layout(0, 0, 0 + mWidth, 0 + mHeight);

                // 让主面板运动
                int newLeft = mMainContent.getLeft() + dx;
                // 限制left范围
                newLeft = fixLeft(newLeft);

                //让主面板运动
                mMainContent.layout(newLeft, 0, newLeft + mWidth, 0 + mHeight);
            }

            dispatchEvent();

            invalidate(); // 兼容3.0以下版本.重绘界面
        }

        /**
         * d. 当控件被释放了, 此方法被执行
         * @param releasedChild 被释放的子控件
         * @param xvel 水平方向的速度, 向右+, 向左-, 速度越大, 绝对值越大
         * @param yvel 竖直方向的速度, 向下+, 向上-, 速度越大, 绝对值越大
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            System.out.println("onViewReleased: xvel: " + xvel);

            // 先考虑打开的情况
            if(xvel == 0 && mMainContent.getLeft() > mRange * 0.5f){
                // 速度比较小, 根据位置判断
                open();
            }else if(xvel > 0){
                // 速度向右
                open();
            }else {
                close();
            }
        }
    };

    /**
     * 分发事件
     */
    private void dispatchEvent() {
        // 0 -> mRange
        // 0.0 -> 1.0
        float percent = mMainContent.getLeft() * 1.0f / mRange;
        System.out.println("dispatchEvent percent: " + percent );

        // 执行伴随动画
        animViews(percent);

        if(mOnDragChangeListener != null){
            mOnDragChangeListener.onDraing(percent);
        }

        Status lastStatus = status;
        // 更新状态
        status = updateStatus(percent);

        System.out.println("status: " + status);
        if(status != lastStatus && mOnDragChangeListener != null){
            if(status == Status.Open){
                mOnDragChangeListener.onOpen();
            }else if(status == Status.Close){
                mOnDragChangeListener.onClose();
            }
        }

    }

    // 更新状态
    private Status updateStatus(float percent) {

        if(percent == 0){
            return Status.Close;
        }else if(percent == 1){
            return  Status.Open;
        }
        return Status.Draging;

    }

    // 执行伴随动画
    private void animViews(float percent) {
//        1. 左菜单: 缩放动画,
        // 0.0 -> 1.0 >> 0.0 -> 0.5 >> 0.5f -> 1.0f
//        mLeftMenu.setScaleX(0.5f + percent * 0.5f);
//        mLeftMenu.setScaleY(0.5f + percent * 0.5f);
        ViewCompat.setScaleX(mLeftMenu, evaluate(percent, 0.5f, 1.0f));
        ViewCompat.setScaleY(mLeftMenu, evaluate(percent, 0.5f, 1.0f));

        // 平移动画 -mWidth * 0.5f -> 0
        ViewCompat.setTranslationX(mLeftMenu, evaluate(percent, -mWidth * 0.5f, 0f));

        // 透明度动画 0.3f -> 1.0f
        ViewCompat.setAlpha(mLeftMenu, evaluate(percent, 0.3f, 1.0f));

//        2. 主界面: 缩放动画, 1.0 -> 0.8
        ViewCompat.setScaleX(mMainContent, evaluate(percent, 1.0f, 0.8f));
        ViewCompat.setScaleY(mMainContent, evaluate(percent, 1.0f, 0.8f));

//        3. 背景: 亮度变化, 黑色 -> 透明
        Drawable drawable = getBackground();
        drawable.setColorFilter(
                (Integer)evaluateColor(percent, Color.BLACK, Color.TRANSPARENT),
                PorterDuff.Mode.SRC_OVER);
    }

    /**
     * FloatEvaluator
     * 类型估值器
     * @param fraction  分度值
     * @param startValue 开始值
     * @param endValue 结束值
     * @return  中间某个时刻的值
     */
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * 颜色估值器
     * @param fraction 分度值
     * @param startValue 开始颜色
     * @param endValue 结束颜色
     * @return
     */
    public Object evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int)((startA + (int)(fraction * (endA - startA))) << 24) |
                (int)((startR + (int)(fraction * (endR - startR))) << 16) |
                (int)((startG + (int)(fraction * (endG - startG))) << 8) |
                (int)((startB + (int)(fraction * (endB - startB))));
    }

    /**
     * 关闭控件
     */
    public void close() {
        close(true);
    }

    /**
     * 关闭
     * @param isSmooth 是否是平滑动画
     */
    public void close(boolean isSmooth){
        int finalLeft = 0;
        if(isSmooth){
            // 1. 触发一个平滑动画
            if(mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)){
                // true, 当前控件还没移动到指定位置, 需要继续重绘界面
                ViewCompat.postInvalidateOnAnimation(this); // -> drawChild -> child.child -> computeScroll
            }
        }else {
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth, 0 + mHeight);
        }
    }

    /**
     * 打开控件
     */
    public void open() {
        open(true);
    }

    /**
     * 开启
     * @param isSmooth 是否是平滑动画
     */
    public void open(boolean isSmooth){
        int finalLeft = mRange;
        if(isSmooth){
            // 1. 触发一个平滑动画
            if(mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)){
                // true, 当前控件还没移动到指定位置, 需要继续重绘界面
                ViewCompat.postInvalidateOnAnimation(this); // -> drawChild -> child.child -> computeScroll
            }
        }else {
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth, 0 + mHeight);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        // 2. 维持动画的继续, 此方法在一定时间内容重复执行
        if(mDragHelper.continueSettling(true)){
            // true, 当前控件还没移动到指定位置, 需要继续重绘界面
            ViewCompat.postInvalidateOnAnimation(this); // -> drawChild -> child.child -> computeScroll
        }
    }

    @Nullable
    private int fixLeft(int left) {
        // 将left限制在 0 -> mRange之间.

        if(left < 0){
            return 0;
        }else if(left > mRange){
            return mRange;
        }
        return left;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        // View从xml中填充结束后调用

        // 做健壮性检查
        // 子控件个数检查
        if(getChildCount() < 2){
            throw new IllegalStateException("子控件个数至少2个! Your viewgroup must contains 2 child at least.");
        }
        // 子控件类型检查
        if(!(getChildAt(0) instanceof ViewGroup) || !(getChildAt(1) instanceof ViewGroup)){
            throw new IllegalArgumentException("子控件必须是ViewGroup的实现. Your child must be an instance of ViewGroup.");
        }

        mLeftMenu = (ViewGroup) getChildAt(0);
        mMainContent = (ViewGroup) getChildAt(1);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // 在onMeasure之后, 控件大小发生变化时, 调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();

        mRange = (int) (mWidth * 0.6f);
    }

    OnDragChangeListener mOnDragChangeListener;

    // 设置监听回调
    public void setOnDragChangeListener(OnDragChangeListener mOnDragChangeListener) {
        this.mOnDragChangeListener = mOnDragChangeListener;
    }

    public interface OnDragChangeListener{
        void onClose();
        void onOpen();
        void onDraing(float percent);
    }


}
