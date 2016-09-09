package com.hjs.mymztu.utils;

import android.util.Log;

import com.hjs.mymztu.ui.MyApp;

/**
 * @描述 输出打印工具类
 */
public class XLog {

	public static boolean isDebugMode() {
		return MyApp.ISDEBUG;
	}
	
	public static void print(Object _obj) {
		if (isDebugMode())
			System.out.println(_obj);
	}

	public static void v(String tag, Object _obj) {
		if (isDebugMode())
			Log.v(tag, _obj.toString());
	}
	
	public static void d(String tag, Object _obj) {
		if (isDebugMode())
			Log.d(tag, _obj.toString());
	}
	
	public static void i(String tag, Object _obj) {
		if (isDebugMode())
			Log.i(tag, _obj.toString());
	}
	
	public static void w(String tag, Object _obj) {
		if (isDebugMode())
			Log.w(tag, _obj.toString());
	}
	
	public static void e(String tag, Object _obj) {
		if (isDebugMode())
			Log.e(tag, _obj.toString());
	}
}
