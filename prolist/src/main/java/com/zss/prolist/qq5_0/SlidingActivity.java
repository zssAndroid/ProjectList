package com.zss.prolist.qq5_0;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.lib.R;

public class SlidingActivity extends Activity {

	private SlidingMenu mSlidingMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.qq_main);

		myInit();
	}

	private void myInit() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.qqSlidingMenu);
	}

	public void toggleMenu(View view) {
		mSlidingMenu.toggleMenu();
	}
}
