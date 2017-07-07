package com.zss.prolist.downservice;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.lib.R;
import com.zss.prolist.adapter.utils.CommonAdapter;
import com.zss.prolist.adapter.utils.ViewHolder;
import com.zss.prolist.downservice.bean.FileInfo;
import com.zss.prolist.downservice.service.DownService;

import java.util.List;

public class FileListAdapter extends CommonAdapter<FileInfo> {

	public FileListAdapter(Context context, List<FileInfo> lDatas,
			int layoutItemID) {
		super(context, lDatas, layoutItemID);
	}

	@Override
	public void getViewItem(ViewHolder holder, final FileInfo item) {
		holder.setText(R.id.downTvFileName, item.getFileName());
		holder.setProgress(R.id.downPbProgress, item.getProgress());

		holder.getView(R.id.downBtBegin).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, DownService.class);
						intent.setAction(DownService.ACTION_START);
						intent.putExtra(DownService.DOWNINFO, item);
						context.startService(intent);
					}
				});

		holder.getView(R.id.downBtStorp).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, DownService.class);
						intent.setAction(DownService.ACTION_PAUSE);
						intent.putExtra(DownService.DOWNINFO, item);
						context.startService(intent);
					}
				});
	}
}
