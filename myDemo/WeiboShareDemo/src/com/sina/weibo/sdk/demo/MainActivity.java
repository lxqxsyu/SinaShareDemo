package com.sina.weibo.sdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.subao.husubao.share.sina.SinaShareManager;

public class MainActivity extends Activity {
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button = (Button) findViewById(R.id.share_weibo);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SinaShareManager ssm = SinaShareManager.getInstance(MainActivity.this);
				ssm.shareBySina(ssm.new ShareContentPic(R.drawable.ic_launcher));
			}
		});
	}
}
