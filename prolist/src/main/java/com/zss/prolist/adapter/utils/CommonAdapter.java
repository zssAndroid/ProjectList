package com.zss.prolist.adapter.utils;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * ������ģ��
 * 
 * @author async
 * 
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context context;
	private List<T> lDatas;
	private int layoutItemID;

	public CommonAdapter(Context context, List<T> lDatas, int layoutItemID) {
		super();
		this.context = context;
		this.lDatas = lDatas;
		this.layoutItemID = layoutItemID;
	}

	@Override
	public int getCount() {
		return lDatas.size();
	}

	@Override
	public T getItem(int position) {
		return lDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.getInstance(context, convertView,
				parent, layoutItemID, position);

		getViewItem(holder, getItem(position));

		return holder.getConvertView();
	}

	public abstract void getViewItem(ViewHolder holder, T item);
}
