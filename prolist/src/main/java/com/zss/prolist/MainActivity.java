package com.zss.prolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lib.R;
import com.zss.prolist.adapter.AdapterAcitivty;
import com.zss.prolist.down.DownLoadActivity;
import com.zss.prolist.downservice.DownServiceActivity;
import com.zss.prolist.flowlayout.FlowLayoutActivity;
import com.zss.prolist.image_mt.ImageMain;
import com.zss.prolist.imagecycle.ImageCycleViewActivity;
import com.zss.prolist.listviewrefresh.ListViewRfreshActivity;
import com.zss.prolist.lockpattern.LockPatternActivity;
import com.zss.prolist.qq5_0.SlidingActivity;
import com.zss.prolist.startmenu.StartMenu;
import com.zss.prolist.startmenu.StartMenu2;
import com.zss.prolist.tabfragment.FragmentTab;
import com.zss.prolist.title_bar.TitleBarActivity;
import com.zss.prolist.tree.TreeActivity;
import com.zss.prolist.ui.TimerCount;
import com.zss.prolist.viewpager.ViewPagerActivity;
import com.zss.prolist.viewpager.ViewPagerCustormerActivity;
import com.zss.prolist.wechat.WeChatActivivty;
import com.zss.prolist.wechat_imageUp.ImageLoaderActivity;
import com.zss.prolist.wechat_talk.WeChatTalkActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

	// 文件下载路径
	public static final String DOWNLOAD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/downloads";

	private ListView lvItem;
	private MainAdapter adapter;
	private List<ListItem> lItems = new ArrayList<ListItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myInit();
	}

	private void myInit() {
		initView();
		initData();
		initListener();
	}

	private void initView() {
		lvItem = (ListView) findViewById(R.id.mainLv);
		adapter = new MainAdapter(this, lItems);
		lvItem.setAdapter(adapter);
	}

	private void initData() {
		ListItem bean;

		bean = new ListItem();
		bean.setItemTitle("仿微信聊天");
		bean.setCls(WeChatTalkActivity.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("仿微信图片上传");
		bean.setCls(ImageLoaderActivity.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("九宫格解锁");
		bean.setCls(LockPatternActivity.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("下拉刷新组件");
		bean.setCls(ListViewRfreshActivity.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("流式布局组件");
		bean.setCls(FlowLayoutActivity.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("轮播图片、广告");
		bean.setCls(ImageCycleViewActivity.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("断点续传service");
		bean.setCls(DownServiceActivity.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("万能适配器的实现");
		bean.setCls(AdapterAcitivty.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("任一级树形控件");
		bean.setCls(TreeActivity.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("图片处理(美图秀秀)");
		bean.setCls(ImageMain.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("QQ5.0侧滑菜单");
		bean.setCls(SlidingActivity.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("自定义title测试");
		bean.setCls(TitleBarActivity.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("ViewPager 切换动画  自定义viewpage实现向下兼容");
		bean.setCls(ViewPagerCustormerActivity.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("ViewPager切换动画");
		bean.setCls(ViewPagerActivity.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("Fragment实现Tab功能");
		bean.setCls(FragmentTab.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("仿微信界面");
		bean.setCls(WeChatActivivty.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("星型菜单(使用普通动画实现)");
		bean.setCls(StartMenu2.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("星型菜单(使用属性动画实现)");
		bean.setCls(StartMenu.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("倒计时");
		bean.setCls(TimerCount.class);
		lItems.add(bean);

		bean = new ListItem();
		bean.setItemTitle("多线程下载文件");
		bean.setCls(DownLoadActivity.class);
		lItems.add(bean);

		adapter.notifyDataSetChanged();
	}

	private void initListener() {
		lvItem.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				ListItem bean = lItems.get(position);
				if (bean.getCls() != null) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, bean.getCls());
					startActivity(intent);
				} else {
					Toast.makeText(MainActivity.this, "即将开通……",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
