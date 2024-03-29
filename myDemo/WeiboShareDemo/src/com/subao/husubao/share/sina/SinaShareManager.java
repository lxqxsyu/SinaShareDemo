package com.subao.husubao.share.sina;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;
import com.subao.husubao.share.sina.util.SinaShareUtil;

public class SinaShareManager{
	/**
	 * 文字
	 */
	public static final int SINA_SHARE_WAY_TEXT = 1;
	/**
	 * 图片
	 */
	public static final int SINA_SHARE_WAY_PIC = 2;
	/**
	 * 链接
	 */	
	public static final int SINA_SHARE_WAY_WEBPAGE = 3;
	
	private static String sinaAppKey;
	public static final String SCOPE = 
	            "email,direct_messages_read,direct_messages_write,"
	            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
	            + "follow_app_official_microblog," + "invitation_write";
	private static SinaShareManager instance;
    /** 微博分享的接口实例 */
    private IWeiboShareAPI sinaAPI;
    private Context context;
	
	private SinaShareManager(Context context){
		this.context = context;
		//初始化数据
		sinaAppKey = SinaShareUtil.getSinaAppKey(context);
		//初始化新浪分享代码
		if(sinaAppKey != null){
			initSinaShare(context);
		}
	}
	
	/**
	 * 获取SinaShareManager实例
	 * 非线程安全，请在UI线程中操作
	 * @param context
	 * @return
	 */
	public static SinaShareManager getInstance(Context context){
		if(instance == null){
			instance = new SinaShareManager(context);
		}
		return instance;
	}
	
	/**
	 * 新浪微博分享方法
	 * @param shareContent 分享的内容
	 */
	public void shareBySina(ShareContent shareContent){
		switch (shareContent.getShareWay()) {
		case SINA_SHARE_WAY_TEXT:
			shareText(shareContent);
			break;
		case SINA_SHARE_WAY_PIC:
			sharePicture(shareContent);
			break;
		case SINA_SHARE_WAY_WEBPAGE:
			shareWebPage(shareContent);
			break;
		}
	}
	
	/*
	 * 分享文字
	 */
	private void shareText(ShareContent shareContent){
		//初始化微博的分享消息
		WeiboMessage weiboMessage = new WeiboMessage();
		weiboMessage.mediaObject = getTextObj(shareContent.getContent());
		//初始化从第三方到微博的消息请求
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		request.transaction = buildTransaction("sinatext");
		request.message = weiboMessage;
		//发送请求信息到微博，唤起微博分享界面
		sinaAPI.sendRequest(request);
	}
	
	/*
	 * 分享图片
	 */
	private void sharePicture(ShareContent shareContent){
		WeiboMessage weiboMessage = new WeiboMessage();
		weiboMessage.mediaObject = getImageObj(shareContent.getPicResource());
		//初始化从第三方到微博的消息请求
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		request.transaction = buildTransaction("sinatext");
		request.message = weiboMessage;
		//发送请求信息到微博，唤起微博分享界面
		sinaAPI.sendRequest(request);
	}
	
	private void shareWebPage(ShareContent shareContent){
		WeiboMessage weiboMessage = new WeiboMessage();
		weiboMessage.mediaObject = getWebpageObj(shareContent);
		//初始化从第三方到微博的消息请求
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		request.transaction = buildTransaction("sinatext");
		request.message = weiboMessage;
		//发送请求信息到微博，唤起微博分享界面
		sinaAPI.sendRequest(request);
	}
	
	private abstract class ShareContent{
		protected abstract int getShareWay();
		protected abstract String getContent();
		protected abstract String getTitle();
		protected abstract String getURL();
		protected abstract int getPicResource();
		
	}
	
	/**
	 * 设置分享文字的内容
	 * @author Administrator
	 *
	 */
	public class ShareContentText extends ShareContent{
		private String content;
		
		/**
		 * 构造分享文字类
		 * @param text 分享的文字内容
		 */
		public ShareContentText(String content){
			this.content = content;
		}

		@Override
		protected String getContent() {

			return content;
		}

		@Override
		protected String getTitle() {
			return null;
		}

		@Override
		protected String getURL() {
			return null;
		}

		@Override
		protected int getPicResource() {
			return -1;
		}

		@Override
		protected int getShareWay() {
			return SINA_SHARE_WAY_TEXT;
		}
		
	}
	
	/**
	 * 设置分享图片的内容
	 * @author Administrator
	 *
	 */
	public class ShareContentPic extends ShareContent{
		private int picResource;
		public ShareContentPic(int picResource){
			this.picResource = picResource;
		}
		
		@Override
		protected String getContent() {
			return null;
		}

		@Override
		protected String getTitle() {
			return null;
		}

		@Override
		protected String getURL() {
			return null;
		}

		@Override
		protected int getPicResource() {
			return picResource;
		}

		@Override
		protected int getShareWay() {
			return SINA_SHARE_WAY_PIC;
		}
	}
	
	/**
	 * 设置分享链接的内容
	 * @author Administrator
	 *
	 */
	public class ShareContentWebpage extends ShareContent{
		private String title;
		private String content;
		private String url;
		private int picResource;
		public ShareContentWebpage(String title, String content, 
				String url, int picResource){
			this.title = title;
			this.content = content;
			this.url = url;
			this.picResource = picResource;
		}

		@Override
		protected String getContent() {
			return content;
		}

		@Override
		protected String getTitle() {
			return title;
		}

		@Override
		protected String getURL() {
			return url;
		}

		@Override
		protected int getPicResource() {
			return picResource;
		}

		@Override
		protected int getShareWay() {
			return SINA_SHARE_WAY_WEBPAGE;
		}
		
	}
	
    /**
     * 创建文本消息对象。
     * 
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }
    
    private ImageObject getImageObj(int picResource){
    	 ImageObject imageObject = new ImageObject();
    	 Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), picResource);
         imageObject.setImageObject(bmp);
         return imageObject;
    }
    
    private WebpageObject getWebpageObj(ShareContent shareContent){
    	WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = shareContent.getTitle();
        mediaObject.description = shareContent.getContent();
        
        // 设置 Bitmap 类型的图片到视频对象里
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), shareContent.getPicResource());
        mediaObject.setThumbImage(bmp);
        mediaObject.actionUrl = shareContent.getURL();
        return mediaObject;
    }
	
	private void initSinaShare(Context context){
		// 创建微博 SDK 接口实例
        sinaAPI = WeiboShareSDK.createWeiboAPI(context, sinaAppKey);
        //检查版本支持情况
        checkSinaVersin(context);
        sinaAPI.registerApp();
	}

	private void checkSinaVersin(final Context context) {
		// 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
        boolean isInstalledWeibo = sinaAPI.isWeiboAppInstalled();
        //int supportApiLevel = sinaAPI.getWeiboAppSupportAPI(); 
        
        // 如果未安装微博客户端，设置下载微博对应的回调
        if (!isInstalledWeibo) {
           sinaAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
                @Override
                public void onCancel() {
                    Toast.makeText(context, 
                            "取消下载", 
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
	}
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
}
