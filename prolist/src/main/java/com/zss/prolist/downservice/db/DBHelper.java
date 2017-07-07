package com.zss.prolist.downservice.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类(单例模式)<br/>
 * 将helper设置成单例模式,防止多线程访问数据库的时候造成数据异常
 *
 * @author async
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "lib.db";
	private final static int VERSION = 1;

	public static String TABLE_THREAD_INFO = "thread_info";

	private final String SQL_CREATE = "create table " + TABLE_THREAD_INFO + "("
			+ "id integer primary key autoincrement,"
			+ "threadid integer,url string,start integer,"
			+ "end integer,progress integer)";
	private final String SQL_DROP = "drop table if exists thread_info";

	private static DBHelper helper;

	private DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	public static DBHelper getInstance(Context context) {
		if (helper == null) {
			helper = new DBHelper(context);
		}
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DROP);
		db.execSQL(SQL_CREATE);
	}
}
