package com.hjs.mymztu.ui;

import android.app.Application;
import android.util.DisplayMetrics;

import com.hjs.mymztu.constants.AppGlobal;
import com.hjs.mymztu.utils.CrashHandler;
import com.hjs.mymztu.utils.FileUtils;

/**
 * Created by Administrator on 2016/3/29.
 */
public class MyApp extends Application {

    public static final boolean ISDEBUG = true; // 是否处于调试模式

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }

    private void initApplication() {
        initScreen();
        // 未捕获的异常处理
        if (!ISDEBUG) {
            CrashHandler.getInstance().init(getApplicationContext());
        }
    }

    private void initScreen() {
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        AppGlobal.screenWidth = display.widthPixels;
        AppGlobal.screenHeight = display.heightPixels;
        AppGlobal.screenDensityDpi = display.densityDpi;
        AppGlobal.screenDensityDpiRadio = display.density;
        AppGlobal.scaledDensity = display.scaledDensity;
        AppGlobal.externalFileDir = FileUtils.getFilesPath(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
