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
	 * ����
	 */
	public static final int SINA_SHARE_WAY_TEXT = 1;
	/**
	 * ͼƬ
	 */
	public static final int SINA_SHARE_WAY_PIC = 2;
	/**
	 * ����
	 */	
	public static final int SINA_SHARE_WAY_WEBPAGE = 3;
	
	private static String sinaAppKey;
	public static final String SCOPE = 
	            "email,direct_messages_read,direct_messages_write,"
	            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
	            + "follow_app_official_microblog," + "invitation_write";
	private static SinaShareManager instance;
    /** ΢������Ľӿ�ʵ�� */
    private IWeiboShareAPI sinaAPI;
    private Context context;
	
	private SinaShareManager(Context context){
		this.context = context;
		//��ʼ������
		sinaAppKey = SinaShareUtil.getSinaAppKey(context);
		//��ʼ�����˷������
		if(sinaAppKey != null){
			initSinaShare(context);
		}
	}
	
	/**
	 * ��ȡSinaShareManagerʵ��
	 * ���̰߳�ȫ������UI�߳��в���
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
	 * ����΢��������
	 * @param shareContent ���������
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
	 * ��������
	 */
	private void shareText(ShareContent shareContent){
		//��ʼ��΢���ķ�����Ϣ
		WeiboMessage weiboMessage = new WeiboMessage();
		weiboMessage.mediaObject = getTextObj(shareContent.getContent());
		//��ʼ���ӵ�������΢������Ϣ����
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		request.transaction = buildTransaction("sinatext");
		request.message = weiboMessage;
		//����������Ϣ��΢��������΢���������
		sinaAPI.sendRequest(request);
	}
	
	/*
	 * ����ͼƬ
	 */
	private void sharePicture(ShareContent shareContent){
		WeiboMessage weiboMessage = new WeiboMessage();
		weiboMessage.mediaObject = getImageObj(shareContent.getPicResource());
		//��ʼ���ӵ�������΢������Ϣ����
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		request.transaction = buildTransaction("sinatext");
		request.message = weiboMessage;
		//����������Ϣ��΢��������΢���������
		sinaAPI.sendRequest(request);
	}
	
	private void shareWebPage(ShareContent shareContent){
		WeiboMessage weiboMessage = new WeiboMessage();
		weiboMessage.mediaObject = getWebpageObj(shareContent);
		//��ʼ���ӵ�������΢������Ϣ����
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		request.transaction = buildTransaction("sinatext");
		request.message = weiboMessage;
		//����������Ϣ��΢��������΢���������
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
	 * ���÷������ֵ�����
	 * @author Administrator
	 *
	 */
	public class ShareContentText extends ShareContent{
		private String content;
		
		/**
		 * �������������
		 * @param text �������������
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
	 * ���÷���ͼƬ������
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
	 * ���÷������ӵ�����
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
     * �����ı���Ϣ����
     * 
     * @return �ı���Ϣ����
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
        
        // ���� Bitmap ���͵�ͼƬ����Ƶ������
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), shareContent.getPicResource());
        mediaObject.setThumbImage(bmp);
        mediaObject.actionUrl = shareContent.getURL();
        return mediaObject;
    }
	
	private void initSinaShare(Context context){
		// ����΢�� SDK �ӿ�ʵ��
        sinaAPI = WeiboShareSDK.createWeiboAPI(context, sinaAppKey);
        //���汾֧�����
        checkSinaVersin(context);
        sinaAPI.registerApp();
	}

	private void checkSinaVersin(final Context context) {
		// ��ȡ΢���ͻ��������Ϣ�����Ƿ�װ��֧�� SDK �İ汾
        boolean isInstalledWeibo = sinaAPI.isWeiboAppInstalled();
        //int supportApiLevel = sinaAPI.getWeiboAppSupportAPI(); 
        
        // ���δ��װ΢���ͻ��ˣ���������΢����Ӧ�Ļص�
        if (!isInstalledWeibo) {
           sinaAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
                @Override
                public void onCancel() {
                    Toast.makeText(context, 
                            "ȡ������", 
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
	}
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
}
