package com.subao.husubao.share.sina.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class SinaShareUtil {
	/**
	 * 获取新浪微博的AppKey(必须在manifest文件中配置)
	 * @param context
	 * @return
	 */
	public static String getSinaAppKey(Context context){
		try {
			ApplicationInfo appInfo = context.getPackageManager()
                     .getApplicationInfo(context.getPackageName(),
                     PackageManager.GET_META_DATA);
			//return appInfo.metaData.getString("SINA_APP_KEY");
			return String.valueOf(appInfo.metaData.getInt("SINA_APP_KEY"));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
