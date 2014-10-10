package com.sina.weibo.sdk.demo;

import com.subao.husubao.share.sina.SinaCallbackActivity;

public class CallBackActivity extends SinaCallbackActivity{

	@Override
	public void sinaResp(int respCode) {
		System.out.println("respCode = " + respCode);
	}

}
