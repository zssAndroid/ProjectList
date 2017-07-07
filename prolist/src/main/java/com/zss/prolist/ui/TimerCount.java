package com.zss.prolist.ui;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.example.lib.R;

/**
 * 实现倒计时
 *
 * @author async
 *
 */
public class TimerCount extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timercount);

		myInit();
	}

	private void myInit() {
		final Button btTimer = (Button) findViewById(R.id.tcBtTimer);

		ValueAnimator va = ValueAnimator.ofInt(100, 0);
		va.setDuration(5000);// 设置持续时间
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Integer count = (Integer) animation.getAnimatedValue();
				btTimer.setText("倒计时：" + count);
			}
		});
		va.start();
	}
}
