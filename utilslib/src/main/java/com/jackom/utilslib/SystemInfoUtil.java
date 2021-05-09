package com.jackom.utilslib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * <p>
 * able to get IMEI/phone SN
 */
public class SystemInfoUtil {
	private static String serial = null;

	/**
	 * 获取SN
	 *
	 * @return
	 */
	public static String getSn(Context ctx) {
		if (TextUtils.isEmpty(serial)) {
			try {
				Class<?> c = Class.forName("android.os.SystemProperties");
				Method get = c.getMethod("get", String.class);
				serial = (String) get.invoke(c, "ro.serialNo");
			} catch (Exception ignored) {
			}
		}
		return serial;
	}

	public static int getVersionCode(Context context) {
		PackageManager manager = context.getPackageManager();
		int code = 0;
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			code = info.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return code;
	}

	public static String getVersionName(Context context) {
		PackageManager manager = context.getPackageManager();
		String versionName = null;
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			versionName = info.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
//			该方法经过验证，发现其只能获取AndroidManifest.xml中应用名称是这样赋值的方式：
//			android:label="@string/app_name"
//			而无法兼容如下方式：
//			android:label="${APP_NAME}"，其中APP_NAME在打包过程中会直接赋值为应用名称。
//			PackageManager packageManager = context.getPackageManager();
//			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
//			int labelRes = packageInfo.applicationInfo.labelRes;
//			return context.getResources().getString(labelRes);

			//而兼容如下方式：	android:label="${APP_NAME}" 和 android:label="@string/app_name"
			PackageManager packageManager = context.getPackageManager();
			return String.valueOf(packageManager.getApplicationLabel(context.getApplicationInfo()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@SuppressLint("RestrictedApi")
	public static void showStatusBar(Activity context) {
		context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	//如果是沉浸式的，全屏前就没有状态栏
	@SuppressLint("RestrictedApi")
	public static void hideStatusBar(Activity context) {
		context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@SuppressLint("NewApi")
	public static void hideSystemUI(Activity context) {
		int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		}

		SYSTEM_UI = context.getWindow().getDecorView().getSystemUiVisibility();
		context.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
	}

	public static int SYSTEM_UI = 0;
	@SuppressLint("NewApi")
	public static void showSystemUI(Activity context) {
		int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
		context.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI);
	}

	/**
	 * 获取进程号对应的进程名
	 *
	 * @param pid 进程号
	 * @return 进程名
	 */
	public static String getProcessName(int pid) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
			String processName = reader.readLine();
			if (!TextUtils.isEmpty(processName)) {
				processName = processName.trim();
			}
			return processName;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 得到进程名称
	 * @param context
	 * @return
	 */
	public static String getProcessName(Context context) {
		if (context == null) return null;
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if (null == manager) {
			return null;
		}
		for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
			if (processInfo.pid == android.os.Process.myPid()) {
				return processInfo.processName;
			}
		}
		return null;
	}

}
