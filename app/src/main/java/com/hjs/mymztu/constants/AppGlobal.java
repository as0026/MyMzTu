package com.hjs.mymztu.constants;


/**
 * App全局常量/变量
 */
public class AppGlobal {
	// 屏幕宽度
	public static int screenWidth = 720;
	// 屏幕高度
	public static int screenHeight = 1280;
	// 屏幕密度dpi
	public static int screenDensityDpi = 320;
	// 屏幕密度dpi比例
	public static float screenDensityDpiRadio = 2;
	// 字体缩放比例
	public static float scaledDensity = 2;
	// 黄金比例
	public static final float GODEN_RADIO = 0.58f;
	// appIcon图标基础尺寸
	public static final int APPICON_SIZE = 72;
	//状态栏的高度
	public static int StatusHeight = (int) (20*screenDensityDpiRadio);
	// SharePreferences数据文件
	public static String share_xml = "mztu_data";
	// 默认的存储文件夹
	public static String externalFileDir;
	//默认页数
	public final static int pageSize = 10;
	
}
