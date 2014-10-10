package com.subao.husubao.share.sina.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class SinaShareUtil {
	/**
	 * ��ȡ����΢����AppKey(������manifest�ļ�������)
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
