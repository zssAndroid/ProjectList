package com.zss.prolist.listviewrefresh;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.example.lib.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewRfreshActivity extends Activity implements ListViewRefresh.OnRefreshListener {
	private ListViewRefresh lvContent;
	private ListViewAdapter adapter;
	private List<Integer> lDatas;

	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listviewrefresh_main);

		myInit();
	}

	private void myInit() {
		initView();
		initData();
		initListener();
	}

	private void initView() {
		lDatas = new ArrayList<Integer>();
		lvContent = (ListViewRefresh) findViewById(R.id.refreshLV);
		adapter = new ListViewAdapter(this, lDatas,
				R.layout.listviewrefresh_item);
		lvContent.setAdapter(adapter);
	}

	private void initData() {
		for (int i = 0; i < 5; i++) {
			lDatas.add(i);
		}
		adapter.notifyDataSetChanged();
	}

	private void initListener() {
		lvContent.setOnRefreshListener(this);
	}

	@Override
	public void onPullDownLisetner() {

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				int index = lDatas.size();
				for (int i = index; i < index + 2; i++) {
					lDatas.add(0, i);
				}
				adapter.notifyDataSetChanged();

				lvContent.refreshComplete();
			}
		}, 5000);

	}

	@Override
	public void onPullUpListener() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				int index = lDatas.size();
				for (int i = index; i < index + 2; i++) {
					lDatas.add(i);
				}
				adapter.notifyDataSetChanged();

				lvContent.refreshComplete();
			}
		}, 5000);
	}
}
