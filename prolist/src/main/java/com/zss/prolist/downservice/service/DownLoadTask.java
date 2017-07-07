package com.zss.prolist.downservice.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zss.prolist.MainActivity;
import com.zss.prolist.downservice.bean.FileInfo;
import com.zss.prolist.downservice.bean.ThreadInfo;
import com.zss.prolist.downservice.db.ThreadDao;
import com.zss.prolist.downservice.db.ThreadDaoImpl;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载任务类
 *
 * @author async
 *
 */
public class DownLoadTask {
	// 在intent中传递的常量
	public static final String PROGRESS = "progress";// 当前下载进度
	public static final String FILE_ID = "id";// 文件ID

	private int progress = 0;// 整个文件下载进度

	private Context context;
	private FileInfo fileInfo;
	private ThreadDao dao;

	private boolean isPause = false;// 是否暂停下载

	private int threadCount = 1;// 线程的数量
	private List<DownLoadThread> lThread;// 线程集合

	// 线程池
	public static ExecutorService esThreadService = Executors
			.newCachedThreadPool();

	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

	/**
	 * 下载线程任务
	 *
	 * @param context
	 * @param file
	 *            FileInfo对象
	 */
	public DownLoadTask(Context context, FileInfo file) {
		this(context, file, 1);
	}

	/**
	 * 下载线程任务
	 *
	 * @param context
	 * @param file
	 *            FileInfo对象
	 * @param threadCount
	 *            该文件下载线程数
	 */
	public DownLoadTask(Context context, FileInfo file, int threadCount) {
		this.context = context;
		this.fileInfo = file;
		this.threadCount = threadCount;

		this.dao = new ThreadDaoImpl(context);
	}

	public void downLoad() {
		// 读取数据库的线程信息
		List<ThreadInfo> lThreadInfo = dao.queryThread(fileInfo.getUrl());
		if (lThreadInfo.size() == 0) {
			// 获取每个线程现在的长度
			int lengthTh = fileInfo.getLength() / threadCount;
			int start, end;
			ThreadInfo threadInfo;
			for (int i = 0; i < threadCount; i++) {
				start = i * lengthTh;// 下载开始位置
				end = (i + 1) * lengthTh - 1;// 下载结束位置
				if (i == threadCount - 1) {
					end = fileInfo.getLength();
				}
				threadInfo = new ThreadInfo(i, fileInfo.getUrl(), start, end);
				// 添加到线程信息集合中
				lThreadInfo.add(threadInfo);

				// 向数据库中插入当前线程信息
				dao.insertThread(threadInfo);
			}
		}

		DownLoadThread down = null;
		lThread = new ArrayList<DownLoadThread>();
		// 启动多个线程进行下载
		for (ThreadInfo thread : lThreadInfo) {
			down = new DownLoadThread(thread);
			lThread.add(down);

			// 初始化已经下载过的进度值
			progress += thread.getProgress();

			// 使用线程池来启动、执行线程
			esThreadService.execute(down);
		}
	}

	/**
	 * 判断所有线程是否都已经执行完毕
	 *
	 * @return
	 */
	private synchronized void checkAllThreadFinished() {
		int i = 0;
		for (DownLoadThread thread : lThread) {
			if (!thread.isFinish()) {
				return;
			}
			Log.i(fileInfo.getFileName(), (i++) + "");
		}

		// 删除线程信息
		dao.deleteThread(fileInfo.getUrl());

		// 通知广播UI下载任务结束
		Intent intent = new Intent(DownService.ACTION_FINISH);
		intent.putExtra(DownService.DOWNINFO, fileInfo);
		context.sendBroadcast(intent);
	}

	/**
	 * 下载线程
	 *
	 * @author async
	 *
	 */
	private class DownLoadThread extends Thread {
		private ThreadInfo threadInfo;
		private boolean isFinish = false;// 当前线程是否下载结束

		private HttpURLConnection con;
		private RandomAccessFile raf;
		private InputStream is;

		public boolean isFinish() {
			return isFinish;
		}

		public DownLoadThread(ThreadInfo threadInfo) {
			this.threadInfo = threadInfo;
		}

		@Override
		public void run() {
			// 设置线程下载位置
			try {
				URL url = new URL(threadInfo.getUrl());
				con = (HttpURLConnection) url.openConnection();
				con.setReadTimeout(5000);
				con.setRequestMethod("GET");

				// 设置下载位置
				int start = threadInfo.getStart() + threadInfo.getProgress();
				con.setRequestProperty("Range", "bytes=" + start + "-"
						+ threadInfo.getEnd());
				// 文件写入位置
				File downFile = new File(MainActivity.DOWNLOAD_PATH,
						fileInfo.getFileName());
				raf = new RandomAccessFile(downFile, "rwd");
				// 在读写的时候跳过设置好的字节数，从下一个字节数开始读写
				raf.seek(start);

				Intent intent = new Intent(DownService.ACTION_UPDATE);
				progress += threadInfo.getProgress();// 线程完成进度

				// 开始下载
				if (HttpStatus.SC_PARTIAL_CONTENT == con.getResponseCode()) {
					// 读取数据
					is = con.getInputStream();
					byte[] buffer = new byte[1024 * 4];
					int len = -1;
					long time = System.currentTimeMillis();
					while ((len = is.read(buffer)) != -1) {
						// 写入文件
						raf.write(buffer, 0, len);

						// 整个文件的完成进度
						progress += len;

						// 当前线程完成的进度
						threadInfo.setProgress(threadInfo.getProgress() + len);

						// 每隔500毫秒发送一次广播刷新进度条
						if (System.currentTimeMillis() - time > 1000) {
							time = System.currentTimeMillis();

							int percent = progress * 100 / fileInfo.getLength();
							intent.putExtra(PROGRESS, percent);
							intent.putExtra(FILE_ID, fileInfo.getId());

							String str = "文件大小：" + fileInfo.getLength();
							str += "　threadID：" + threadInfo.getId();
							str += "　"
									+ (progress * 100 / fileInfo.getLength());
							str += "　当前下载进度：" + progress;
							str += "　len：" + len;
							str += "　线程大小："
									+ threadInfo.getStart()
									+ "/"
									+ threadInfo.getEnd()
									+ "/"
									+ (threadInfo.getEnd() - threadInfo
									.getStart());
							str += "　" + "线程进度：" + threadInfo.getProgress();

							Log.i(fileInfo.getFileName(), str);

							// 发送广播更新进度条
							context.sendBroadcast(intent);
						}

						// 下载暂停时，保存下载进度
						if (isPause) {
							dao.updateThread(threadInfo);
							return;
						}
					}

					// 标识当前线程执行完毕
					isFinish = true;

					// 检查下载任务是否执行完毕
					checkAllThreadFinished();
					Log.i(fileInfo.getFileName(),
							"ThreadId：" + threadInfo.getId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null) {
						is.close();
					}
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
