package com.zss.prolist.down;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 下载线程代码
 *
 * @author async
 *
 */
public class DownLoad {

	private int THREADCOUNT = 3;// 开启线程数
	private Executor threadPool = Executors.newFixedThreadPool(THREADCOUNT);

	public void downLoadFile(String url) {
		try {
			URL httpUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) httpUrl
					.openConnection();
			conn.setReadTimeout(5000);
			conn.setRequestMethod("GET");
			int fileLength = conn.getContentLength();
			int block = fileLength / THREADCOUNT;

			long start, end;
			for (int i = 0; i < THREADCOUNT; i++) {
				start = i * block;
				end = (i + 1) * block - 1;
				if (i == THREADCOUNT - 1) {
					end = fileLength;
				}

				DownLoadRunnable downThread = new DownLoadRunnable(url, start,
						end);
				threadPool.execute(downThread);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class DownLoadRunnable implements Runnable {
		private String url;
		private String fileName;
		private long start;
		private long end;

		public DownLoadRunnable(String url, long start, long end) {
			super();
			this.url = url;
			this.start = start;
			this.end = end;

			this.fileName = url.substring(url.lastIndexOf("/") + 1);
		}

		@Override
		public void run() {
			URL httpUrl;
			try {
				httpUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) httpUrl
						.openConnection();
				conn.setReadTimeout(5000);
				// 指定文件下载的开始与结束位置
				conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
				conn.setRequestMethod("GET");

				// 创建一个写入文件
				File file = Environment.getExternalStorageDirectory();
				RandomAccessFile access = new RandomAccessFile(new File(file,
						fileName), "rwd");
				access.seek(start);// 将写入位置调到开始位置

				InputStream is = conn.getInputStream();
				byte[] data = new byte[4 * 1024];
				int len = 0;
				while ((len = is.read(data)) != -1) {
					access.write(data, 0, len);
				}

				if (is != null) {
					is.close();
				}

				if (access != null) {
					access.close();
				}

				Log.i("down", "线程：" + start + "　完成");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
