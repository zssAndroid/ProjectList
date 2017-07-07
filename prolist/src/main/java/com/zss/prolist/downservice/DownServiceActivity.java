package com.zss.prolist.downservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lib.R;
import com.zss.prolist.downservice.bean.FileInfo;
import com.zss.prolist.downservice.service.DownLoadTask;
import com.zss.prolist.downservice.service.DownService;

import java.util.ArrayList;
import java.util.List;

public class DownServiceActivity extends Activity {
	private List<FileInfo> lFile;
	private ListView lvDownFile;
	private FileListAdapter adapter;

	/**
	 * 更新UI的广播接收器
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (DownService.ACTION_UPDATE.equals(intent.getAction())) {
				int progress = intent.getIntExtra(DownLoadTask.PROGRESS, 0);
				int fileId = intent.getIntExtra(DownLoadTask.FILE_ID, -1);
				for (FileInfo file : lFile) {
					if (fileId == file.getId()) {
						file.setProgress(progress);
						adapter.notifyDataSetChanged();
						return;
					}
				}
			} else if (DownService.ACTION_FINISH.equals(intent.getAction())) {
				// 当任务下载完毕，进度条设置为0
				FileInfo fileInfo = (FileInfo) intent
						.getSerializableExtra(DownService.DOWNINFO);

				for (FileInfo file : lFile) {
					if (fileInfo.getId() == file.getId()) {
						file.setProgress(0);
						adapter.notifyDataSetChanged();
						break;
					}
				}

				Toast.makeText(context,
						"文件<" + fileInfo.getFileName() + ">下载完成",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.downservice);

		myInit();
	}

	private void myInit() {
		initView();
		initData();
	}

	private void initView() {
		lFile = new ArrayList<FileInfo>();
		lvDownFile = (ListView) findViewById(R.id.downListView);
		adapter = new FileListAdapter(this, lFile, R.layout.downservice_item);
		lvDownFile.setAdapter(adapter);
	}

	private void initData() {
		// 注册广播接收者
		IntentFilter filter = new IntentFilter();
		filter.addAction(DownService.ACTION_UPDATE);
		filter.addAction(DownService.ACTION_FINISH);
		registerReceiver(receiver, filter);

		String[][] urls = new String[][] {
				{ "慕课网APK", "http://www.imooc.com/mobile/imooc.apk" },
				{
						"酷狗音乐.apk",
						"http://downmobile.kugou.com/Android/KugouPlayer/7840/KugouPlayer_219_V7.8.4.apk" },
				{
						"e代驾.apk",
						"http://f2.market.xiaomi.com/download/AppStore/0d39f5b3b27bad6601aba10606b63e472f54091c1/cn.edaijia.android.client.apk" },
				{
						"界面.apk",
						"http://f1.market.mi-img.com/download/AppStore/098b6753991fd4fb414aff12e29d9d5db9a0d63d8/com.jiemian.news.apk" },
				{
						"功夫熊猫.apk",
						"http://f2.market.mi-img.com/download/AppStore/006784c9c7551b9bbe1ea0b084a24c96c9a42b795/com.qingguo.gfxiong.apk" } };

		String ip = "192.168.31.225";
		urls = new String[][] {
				{ "QQ0.apk", "http://" + ip + ":8080/AndroidProvider/QQ0.apk" },
				{ "QQ1.apk", "http://" + ip + ":8080/AndroidProvider/QQ1.apk" },
				{ "QQ2.apk", "http://" + ip + ":8080/AndroidProvider/QQ2.apk" },
				{ "QQ3.apk", "http://" + ip + ":8080/AndroidProvider/QQ3.apk" },
				{ "QQ4.apk", "http://" + ip + ":8080/AndroidProvider/QQ4.apk" },
				{ "QQ5.apk", "http://" + ip + ":8080/AndroidProvider/QQ5.apk" },
				{ "mobile.apk",
						"http://" + ip + ":8080/AndroidProvider/mobile.apk" }, };

		lFile.clear();

		for (int i = 0; i < urls.length; i++) {
			FileInfo fileInfo = new FileInfo(i, urls[i][1], urls[i][0]);
			lFile.add(fileInfo);
		}

		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销广播接收者
		unregisterReceiver(receiver);
	}
}
