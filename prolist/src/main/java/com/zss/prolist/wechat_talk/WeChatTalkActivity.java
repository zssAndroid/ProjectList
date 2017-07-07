package com.zss.prolist.wechat_talk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.lib.R;

import java.util.ArrayList;
import java.util.List;

public class WeChatTalkActivity extends Activity {
	private ListView mListView;
	private TalkAdapter adapter;
	private List<Recorder> lDatas;

	private AudioRecorderButton btRecorder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wechat_talk_main);

		myInit();
	}

	private void myInit() {
		initView();
		initListener();
	}

	private void initView() {
		lDatas = new ArrayList<Recorder>();
		adapter = new TalkAdapter(this, lDatas, R.layout.wechat_talk_main_item);
		mListView = (ListView) findViewById(R.id.talkListView);
		mListView.setAdapter(adapter);

		btRecorder = (AudioRecorderButton) findViewById(R.id.talkBtRecorder);
	}

	private void initListener() {
		btRecorder
				.setOnAudioRecorderFinishListener(new AudioRecorderButton.AudioRecorderFinishListener() {

					@Override
					public void onFinish(float seconds, String filePath) {
						Recorder recorder = new Recorder(seconds, filePath);
						lDatas.add(recorder);
						adapter.notifyDataSetChanged();
						// 让ListView滚动到最后一条数据
						mListView.setSelection(lDatas.size() - 1);
					}
				});

		btRecorder
				.setOnAudioRecorderLongClickLisetener(new AudioRecorderButton.AudioRecorderLongClickLisetener() {
					@Override
					public void onLongClick(View v) {
						adapter.stopPreRecorder();
					}
				});
	}

	@Override
	protected void onPause() {
		super.onPause();
		MediaManager.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MediaManager.resume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MediaManager.release();
	}
}
