package com.zss.prolist.tool;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

	/**
	 *获取一个SharedPreference对象
	 * 
	 * @return
	 */
	public static SharedPreferences getSharedPre(Context context) {
		return context.getApplicationContext().getSharedPreferences(
				Constants.SHARED_NAME, Context.MODE_PRIVATE);
	}
}
