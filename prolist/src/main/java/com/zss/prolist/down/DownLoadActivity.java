package com.zss.prolist.down;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.lib.R;

public class DownLoadActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download);

		myInit();
	}

	private void myInit() {
		initView();
		initData();
		initListener();
	}

	private void initView() {

	}

	private void initData() {

	}

	private void initListener() {
		findViewById(R.id.downBtStart).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.downBtStart:
			myDown();
			break;
		}
	}

	private void myDown() {
		new Thread() {
			public void run() {
				String url = "http://downmobile.kugou.com/Android/KugouPlayer/7840/KugouPlayer_219_V7.8.4.apk";
				DownLoad down = new DownLoad();
				down.downLoadFile(url);
			};
		}.start();

	}
}
