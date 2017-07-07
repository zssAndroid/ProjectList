package com.zss.viewpagerindicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VpSimpleFragment extends Fragment {
	private String mTitle;//接收用户传过来的title
	private static final String BUNDLE_TITLE = "title";//设置bundle的key

	/**
	 * fragment一般使用newInstance方法new出实例
	 *
	 * @param title
	 * @return
	 */
	public static VpSimpleFragment newInstance(String title) {
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_TITLE, title);

		VpSimpleFragment fragment = new VpSimpleFragment();
		/**
		 * 提供结构参数给这个Fragment。只能在Fragment被依附到Activity之前被调用(这句话可以这样理解，
		 * setArgument方法的使用必须要在FragmentTransaction 的commit之前使用。 )，也就是说
		 * 你应该在构造fragment之后立刻调用它。这里提供的参数将在fragment destroy 和creation被保留。
		 *
		 *
		 * 官方推荐Fragment.setArguments(Bundle bundle)这种方式来传递参数，而不推荐通过构造方法直接来传递参数
		 * 这是因为假如Activity重新创建（横竖屏切换）时，会重新构建它所管理的Fragment，原先的Fragment的字段值将会全
		 * 部丢失，但是通过Fragment.setArguments(Bundle bundle)方法设置的bundle会保留下来。所以尽量使用
		 * Fragment.setArguments(Bundle bundle)方式来传递参数
		 */
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		if(bundle != null){
			mTitle = bundle.getString(BUNDLE_TITLE);
		}
		//显示title在fragment上
		TextView textView = new TextView(getActivity());
		textView.setText(mTitle);
		textView.setGravity(Gravity.CENTER);

		return textView;
	}
}
