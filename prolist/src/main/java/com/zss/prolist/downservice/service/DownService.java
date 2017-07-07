package com.zss.prolist.downservice.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.zss.prolist.MainActivity;
import com.zss.prolist.downservice.bean.FileInfo;
import com.zss.prolist.downservice.service.DownLoadTask;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressLint("HandlerLeak")
public class DownService extends Service {
	public static final String ACTION_START = "ACTION_START";// 开始下载
	public static final String ACTION_PAUSE = "ACTION_PAUSE";// 停止下载
	public static final String ACTION_FINISH = "ACTION_FINISH";// 结束下载任务
	public static final String ACTION_UPDATE = "ACTION_UPDATE";// 更新界面进度条的值

	public static final String DOWNINFO = "fileInfo";// 传过来数据信息

	private final int MSG_INIT = 0x001;// 初始化

	// 下载任务集合
	private Map<Integer, DownLoadTask> mDownTask = new LinkedHashMap<Integer, DownLoadTask>();

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_INIT:
					FileInfo file = (FileInfo) msg.obj;

					// 启动下载任务
					DownLoadTask downTask = new DownLoadTask(DownService.this,
							file, 3);
					downTask.downLoad();

					// 将下载任务添加到集合中
					mDownTask.put(file.getId(), downTask);
					break;
			}
		};
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		FileInfo file = (FileInfo) intent.getSerializableExtra(DOWNINFO);

		if (ACTION_START.equals(intent.getAction())) {
			// 使用线程池来执行线程
			DownLoadTask.esThreadService.execute(new DownLoadThread(file));

			Log.i(file.getFileName(), "Start：" + file.toString());
		} else if (ACTION_PAUSE.equals(intent.getAction())) {
			DownLoadTask downTask = mDownTask.get(file.getId());
			if (downTask != null) {
				// 设置下载线程处于暂停状态
				downTask.setPause(true);
			}

			Log.i(file.getFileName(), "Stop：" + file.toString());
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 数据下载线程
	 *
	 * @author async
	 *
	 */
	private class DownLoadThread extends Thread {

		private FileInfo mFileInfo;

		public DownLoadThread(FileInfo mFileInfo) {
			this.mFileInfo = mFileInfo;
		}

		@Override
		public void run() {
			HttpURLConnection con = null;
			RandomAccessFile raf = null;
			try {
				// 链接网络文件
				URL url = new URL(mFileInfo.getUrl());
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(3000);
				con.setRequestMethod("GET");// 除下载文件外其他都用post方式
				if (HttpStatus.SC_OK != con.getResponseCode()) {
					return;
				}

				// 获取文件长度
				int length = con.getContentLength();
				if (length <= 0) {// 网络链接或读取文件有问题
					return;
				}

				// 在本地创建文件
				File dir = new File(MainActivity.DOWNLOAD_PATH);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File file = new File(dir, mFileInfo.getFileName());
				// 随机访问文件，可以在文件任意位置进行写入操作
				raf = new RandomAccessFile(file, "rwd");
				// 设置文件长度
				raf.setLength(length);

				mFileInfo.setLength(length);
				handler.obtainMessage(MSG_INIT, mFileInfo).sendToTarget();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (raf != null) {
						raf.close();
					}

					if (con != null) {
						con.disconnect();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
}
