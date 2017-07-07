package com.zss.prolist.wechat_talk;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lib.R;

/**
 * 录音提示界面
 *
 * @author Administrator
 *
 */
public class DialogManager {
	private Dialog mDialog;

	private ImageView imgIcon;
	private ImageView imgVoice;
	private TextView tvLable;

	private Context mContext;

	public DialogManager(Context context) {
		this.mContext = context;
	}

	/**
	 * 显示录音界面
	 */
	public void showRecordingDialog() {
		mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.wechat_talk_dialog_recorder, null);
		mDialog.setContentView(view);

		imgIcon = (ImageView) view.findViewById(R.id.talkImgIcon);
		imgVoice = (ImageView) view.findViewById(R.id.talkImgVoice);
		tvLable = (TextView) view.findViewById(R.id.talkTvLable);

		mDialog.show();
	}

	public void recording() {
		if (null != mDialog && mDialog.isShowing()) {
			imgIcon.setVisibility(View.VISIBLE);
			imgVoice.setVisibility(View.VISIBLE);
			tvLable.setVisibility(View.VISIBLE);

			imgIcon.setImageResource(R.drawable.recorder);
			tvLable.setText("手指上滑，取消发送");
		}
	}

	/**
	 * 取消录音界面
	 */
	public void showWantCancelDialog() {
		if (null != mDialog && mDialog.isShowing()) {
			imgIcon.setVisibility(View.VISIBLE);
			imgVoice.setVisibility(View.GONE);
			tvLable.setVisibility(View.VISIBLE);

			imgIcon.setImageResource(R.drawable.cancel);
			tvLable.setText("松开手指，取消发送");
		}
	}

	/**
	 * 录音时间过短界面
	 */
	public void showTooShortDialog() {
		if (null != mDialog && mDialog.isShowing()) {
			imgIcon.setVisibility(View.VISIBLE);
			imgVoice.setVisibility(View.GONE);
			tvLable.setVisibility(View.VISIBLE);

			imgIcon.setImageResource(R.drawable.voice_to_short);
			tvLable.setText("录音时间过短");
		}
	}

	public void dismissDialog() {
		if (null != mDialog && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	/**
	 * 更新voice图片
	 *
	 * @param level
	 *            1~7
	 */
	public void updateVoiceLevel(int level) {
		if (null != mDialog && mDialog.isShowing()) {
			// 获取资源ID
			int resId = mContext.getResources().getIdentifier("v" + level,
					"drawable", mContext.getPackageName());
			imgVoice.setImageResource(resId);
		}
	}
}
