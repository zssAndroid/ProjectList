package com.zss.prolist.downservice.bean;

import java.io.Serializable;

/**
 * 保存下载文件信息
 *
 * @author async
 *
 */
public class FileInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String url;// 文件下载地址
	private String fileName;// 文件名字
	private int length;// 文件长度
	private int progress;// 下载进度

	public FileInfo() {
		super();
	}

	public FileInfo(int id, String url, String fileName) {
		this(id, url, fileName, 0, 0);
	}

	public FileInfo(int id, String url, String fileName, int length,
					int progress) {
		this.id = id;
		this.url = url;
		this.fileName = fileName;
		this.length = length;
		this.progress = progress;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	@Override
	public String toString() {
		return fileName;
	}
}
