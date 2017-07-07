package com.zss.prolist.adapter.utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ͨ��ViewHolder
 * 
 * @author async
 * 
 */
public class ViewHolder {
	private SparseArray<View> saView;

	@SuppressWarnings("unused")
	private int mPosition;
	private View convertView;

	private ViewHolder(Context context, ViewGroup parent, int layoutID,
			int position) {
		this.saView = new SparseArray<View>();

		this.mPosition = position;
		convertView = LayoutInflater.from(context).inflate(layoutID, parent,
				false);
		convertView.setTag(this);
	}

	/**
	 * ��ȡViewHolder��ʵ��
	 * 
	 * @return
	 */
	public static ViewHolder getInstance(Context context, View convertView,
			ViewGroup parent, int layoutID, int position) {

		if (convertView == null) {
			return new ViewHolder(context, parent, layoutID, position);
		}

		ViewHolder holder = (ViewHolder) convertView.getTag();
		// convertView���Ը��õ���position�Ǳ仯��,�ڴ˸���
		holder.mPosition = position;
		return holder;
	}

	/**
	 * ��ȡʵ������ConvertView
	 * 
	 * @return
	 */
	public View getConvertView() {
		return convertView;
	}

	/**
	 * ͨ��ViewID��ȡ���
	 * 
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = saView.get(viewId);
		if (view == null) {
			view = convertView.findViewById(viewId);
			saView.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * Ϊtextview������ʾ����
	 * 
	 * @param viewId
	 *            ���ID
	 * @param strId
	 *            Ҫ��ʾ���ַ���������ֵ
	 */
	public void setText(int viewId, int strId) {
		setText(viewId, convertView.getContext().getResources()
				.getString(strId));
	}

	/**
	 * Ϊtextview������ʾ����
	 * 
	 * @param viewId
	 *            ���ID
	 * @param str
	 *            Ҫ��ʾ���ַ���
	 */
	public void setText(int viewId, String str) {
		TextView tv = getView(viewId);
		tv.setText(str);
	}

	/**
	 * ����progressBar��������ֵ
	 * 
	 * @param viewId
	 *            ���ID
	 * @param progress
	 *            ��ǰ����ֵ
	 */
	public void setProgress(int viewId, int progress) {
		ProgressBar pb = getView(viewId);
		pb.setProgress(progress);
	}
}
