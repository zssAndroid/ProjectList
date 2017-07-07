package com.zss.prolist.image_preview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

/**
 * 可缩放的图片控件
 *
 * @author async
 *
 */
public class ImageViewZoom extends ImageView implements OnGlobalLayoutListener {

	private boolean isInit;// 是否已经初始化

	public ImageViewZoom(Context context) {
		this(context, null);
	}

	public ImageViewZoom(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ImageViewZoom(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		// 添加监听
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}

	/**
	 * 全局的布局完成之后会调用这个方法<br/>
	 * 获取ImageView加载完成的图片
	 */
	@Override
	public void onGlobalLayout() {
		if (!isInit) {
			// 得到控件宽、高
			int width = getWidth();
			int height = getHeight();

			// 得到图片及宽、高
			Drawable drawable = getDrawable();
			if (null == drawable) {
				return;
			}
			int dw = drawable.getIntrinsicWidth();
			int dh = drawable.getIntrinsicHeight();

			float scale = 1.0f;
			// 图片宽度>控件宽&&图片高度<控件宽度，将其缩小
			if (dw > width && dh < height) {
				scale = width * 1.0f / dw;
			}

			// 图片宽度<控件宽&&图片高度>控件宽度，将其缩小
			if (dh > height && dw < width) {
				scale = height * 1.0f / dh;
			}

			if (dw > width && dh < height) {
				scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
			}

			if (dw < width && dh < height) {
				scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
			}

			isInit = true;
		}
	}
}
