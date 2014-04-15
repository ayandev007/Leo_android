package com.version.sz_app1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class WelcomActivity extends Activity {
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_welcom);

Thread splashTread = new Thread() {
	@Override
	public void run() {
		try {
				sleep(3000);//这里进行操作
				} catch (InterruptedException e) {
					// do nothing
				} finally 
				{
					finish();//调用这个Activity 的finish方法后，在主界面按手机 上的放回键就会直接退出程序
					// 启动主应用
					Intent intent = new Intent();
					intent.setClass(WelcomActivity.this, SelectActivity.class);
					startActivity(intent);
					stop();
				}
			}
		};
		splashTread.start();
	}	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_welcom, menu);
		return true;
}

}
