package com.zss.prolist.listviewrefresh;

import android.content.Context;

import com.example.lib.R;
import com.zss.prolist.adapter.utils.CommonAdapter;
import com.zss.prolist.adapter.utils.ViewHolder;

import java.util.List;

public class ListViewAdapter extends CommonAdapter<Integer> {

	public ListViewAdapter(Context context, List<Integer> lDatas,
						   int layoutItemID) {
		super(context, lDatas, layoutItemID);
	}

	@Override
	public void getViewItem(ViewHolder holder, Integer item) {
		holder.setText(R.id.refreshTvName, "默认用户　" + item);
	}
}
