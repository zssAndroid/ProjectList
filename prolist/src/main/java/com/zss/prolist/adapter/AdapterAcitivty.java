package com.zss.prolist.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.lib.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterAcitivty extends Activity {

	private ListView mListView;
	private List<News> lDatas;
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adapter_main);

		myInit();
	}

	private void myInit() {
		initView();
		initData();
		initListener();
	}

	private void initView() {
		lDatas = new ArrayList<News>();
		mListView = (ListView) findViewById(R.id.adapterLv);
		adapter = new MyAdapter(this, lDatas, R.layout.adapter_item);
		mListView.setAdapter(adapter);
	}

	private void initData() {
		News bean;
		for (int i = 1; i < 15; i++) {
			bean = new News("����" + i, "Anadroid�������ܵ�ListView��GridView������" + i,
					"2015-05-12", (18523495256l + i) + "");
			lDatas.add(bean);
		}
		adapter.notifyDataSetChanged();
	}

	private void initListener() {

	}
}
