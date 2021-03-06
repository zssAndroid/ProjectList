package com.zss.prolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lib.R;

import java.util.List;

public class MainAdapter extends BaseAdapter {

	private List<ListItem> lItems;
	private LayoutInflater inflater;

	public MainAdapter(Context mContext, List<ListItem> lItems) {
		this.lItems = lItems;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return lItems.size();
	}

	@Override
	public Object getItem(int position) {
		return lItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.main_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ListItem bean = lItems.get(position);
		holder.tvTitle.setText(bean.getItemTitle());

		return convertView;
	}

	private class ViewHolder {
		public TextView tvTitle;

		public ViewHolder(View convertView) {
			tvTitle = (TextView) convertView.findViewById(R.id.mainItemTvTitle);
		}
	}
}
