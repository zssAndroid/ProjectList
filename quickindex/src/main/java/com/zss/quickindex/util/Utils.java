package com.zss.quickindex.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 类名:      Utils
 * 创建者:    PoplarTang
 * 创建时间:  2016/11/2.
 * 描述：     TODO
 */

public class Utils {

    private static Toast toast;

    public static void showToast(Context context, String s) {
        if(toast == null){
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }

        toast.setText(s);
        toast.show();
    }
}
