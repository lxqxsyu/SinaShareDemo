package com.subao.husubao.share.sina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.subao.husubao.share.sina.util.SinaShareUtil;

public abstract class SinaCallbackActivity extends Activity implements IWeiboHandler.Response{
	/**
	 * 分享成功
	 */
	public static final int CALLBACK_CODE_SUCCESS = 0;
	/**
	 * 取消分享
	 */
	public static final int CALLBACK_CODE_CANCEL = 1;
	/**
	 * 拒绝访问
	 */
	public static final int CALLBACK_CODE_DENY = 2;
	/**
	 * 未知
	 */
	public static final int CALLBACK_CODE_UNKNOWN = 3;
	
	private IWeiboShareAPI sinaAPI;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String appkey = SinaShareUtil.getSinaAppKey(this);
		if(appkey != null){
			sinaAPI = WeiboShareSDK.createWeiboAPI(this,appkey);
			sinaAPI.handleWeiboResponse(getIntent(), this);
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		sinaAPI.handleWeiboResponse(intent, this);
	}
	
	@Override
	public void onResponse(BaseResponse baseResp) {
		 switch (baseResp.errCode) {
	        case WBConstants.ErrorCode.ERR_OK:
	            sinaResp(CALLBACK_CODE_SUCCESS);
	            break;
	        case WBConstants.ErrorCode.ERR_CANCEL:
	            sinaResp(CALLBACK_CODE_CANCEL);
	            break;
	        case WBConstants.ErrorCode.ERR_FAIL:
	        	sinaResp(CALLBACK_CODE_DENY);
	            break;
	        default:
	        	sinaResp(CALLBACK_CODE_UNKNOWN);
	        	break;
	        }
	}
	
	/**
	 * 新浪微博分享后的返回状态
	 * @param respCode 状态有：CALLBACK_CODE_SUCCESS ...
	 */
	public abstract void sinaResp(int respCode);

}
