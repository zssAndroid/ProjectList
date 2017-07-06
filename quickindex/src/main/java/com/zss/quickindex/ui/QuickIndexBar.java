package com.zss.quickindex.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 类名:      QuickIndexBar
 * 创建者:    PoplarTang
 * 创建时间:  2016/11/2.
 * 描述：     快速索引
 */

public class QuickIndexBar extends View {

    private static final String[] LETTERS = new String[]{
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"
    };

    private Paint paint;
    private int mHeight;        // 控件高度
    private int mWidth;         // 控件宽度
    private float mCellHeight; // 单元格高度
    private OnLetterUpdateListener onLetterUpdateListener;

    public QuickIndexBar(Context context) {
        super(context);
        init();
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG); // 添加抗锯齿属性
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.DEFAULT_BOLD);// 字体加粗
        // 画笔默认把字绘制在坐标右上位置
        paint.setTextAlign(Paint.Align.CENTER);// 文本对齐中间
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制字母A-Z
        for (int i = 0; i < LETTERS.length; i++) {
            String letter = LETTERS[i];

            // mWidth / 2
            float x = mWidth * 0.5f;

            // 通过画笔获取文本高度
//            paint.measureText("阿斯蒂芬") // 获取宽度
            Rect bounds = new Rect();
            paint.getTextBounds("0123456", 2, 5, bounds);

            // 文本的高度
            float textHeight = bounds.height();

            float y = mCellHeight * 0.5f + textHeight * 0.5f + i * mCellHeight;

            // 绘制字母
            canvas.drawText(letter, x, y, paint);

            // 绘制一个点
//            canvas.drawCircle(x, y, 3f, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();

        // 10 / 3 = 3
        mCellHeight = mHeight * 1.0f / LETTERS.length;
    }

    int currentIndex = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getCurrentLetter(event);


                break;
            case MotionEvent.ACTION_MOVE:
                // 获取当前字母索引
                getCurrentLetter(event);

                break;
            case MotionEvent.ACTION_UP:
                currentIndex = -1;

                break;
        }

        return true;
    }

    private void getCurrentLetter(MotionEvent event) {
        int index;// 获取当前字母索引
        index = (int) (event.getY() / mCellHeight);
        // 判断是否在正确的范围内
        if(index >= 0 && index < LETTERS.length){
            // 和上一个字母索引不同
            if(index != currentIndex){
                System.out.println("index: " + index + " letter: " + LETTERS[index]);

                // 将字母传出去
                if(onLetterUpdateListener != null){
                    onLetterUpdateListener.onLetterUpdate(LETTERS[index]);
                }

                // 记录最后摸到的字母索引
                currentIndex = index;
            }
        }
    }

    public void setOnLetterUpdateListener(OnLetterUpdateListener onLetterUpdateListener) {
        this.onLetterUpdateListener = onLetterUpdateListener;
    }

    public interface OnLetterUpdateListener{
        void onLetterUpdate(String letter);
    }
}






