package com.version.sz_app1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListrouteActivity extends ListActivity {
	List<String> data = new ArrayList<String>();
	private ProgressBar firstBar;
	private Button button;
	private String dom;
	private String getrouteinfo;
	private TextView routelable;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listroute);
		firstBar = (ProgressBar)this.findViewById(R.id.progressBar2);
		button = (Button)findViewById(R.id.buttonrouteflash);
		routelable = (TextView)findViewById(R.id.textViewroute);
		
		button.setOnClickListener(new routeflash());
		firstBar.setVisibility(View.GONE); 
		button.setText(R.string.flash);
		
		Intent intent=getIntent();
		dom=intent.getStringExtra("urlstring");
		getrouteinfo = intent.getStringExtra("routeinfostring");
		
		routelable.setText("当前线路:"+getrouteinfo);
		
		
		showlist();
//		new Thread() {
//            @Override
//			public void run() {                        
//                    try {
////                    	Toast.makeText(ListrouteActivity.this, "正在查询请稍后",  
////                                Toast.LENGTH_LONG).show(); 
//                    	Intent intent=getIntent();
//                    	System.out.println("链接网站查询路线");
//                		String dom=intent.getStringExtra("urlstring");
//                		System.out.println("继续链接网站查询路线");
////                		System.out.println("dom:"+dom);
//                		firstBar.setVisibility(View.VISIBLE);
//                    	getWebCon(dom,data);  
//                        //   连接网络获取数据
//                    
//                    	
//                    } catch (Exception e) {
//                            // 在GUI显示错误提示
//                            // tv.setText("Error: " + e.getMessage());
//                            
//                    }
//                    
//                    Message msg_listData = new Message();
//                    handler.sendMessage(msg_listData);
//            }
//		}.start();
		setListAdapter(new ArrayAdapter<String>(ListrouteActivity.this, android.R.layout.simple_list_item_1,data));		
	}
	
	class routeflash implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			data.clear();
			firstBar.setVisibility(View.VISIBLE);
			showlist();
		}
		
	}
	
	public void showlist(){
		new Thread() {
            @Override
			public void run() {                        
                    try {
//                    	Toast.makeText(ListrouteActivity.this, "正在查询请稍后",  
//                                Toast.LENGTH_LONG).show(); 
//                    	Intent intent=getIntent();
//                    	System.out.println("链接网站查询路线");
 //               		String dom=intent.getStringExtra("urlstring");
                		System.out.println("继续链接网站查询路线");
                		System.out.println("dom:"+dom);
                		firstBar.setVisibility(View.VISIBLE);
                    	getWebCon(dom,data);  
                        //   连接网络获取数据
                    	System.out.println("data:"+data);
                    	
                    } catch (Exception e) {
                            // 在GUI显示错误提示
                            // tv.setText("Error: " + e.getMessage());
                            
                    }
                    
                    Message msg_listData = new Message();
                    handler.sendMessage(msg_listData);
            }
		}.start();
//		setListAdapter(new ArrayAdapter<String>(ListrouteActivity.this, android.R.layout.simple_list_item_1,data));		
	}
	
	
	private Handler handler = new Handler() {               

        @Override
		public void handleMessage(Message message) {
        	
        	firstBar.setVisibility(View.GONE);       	
        }
};
	
	public static String getWebCon(String domain,List<String>data) {
		// System.out.println("开始读取内容...("+domain+")");
		StringBuffer sb = new StringBuffer();
		try {
			java.net.URL url = new java.net.URL(domain);
			BufferedReader in = new BufferedReader(new InputStreamReader(url
					.openStream()));
			String line;
			while ((line = in.readLine()) != null) {
				parse(line,data);
				sb.append(line);
			}
//			System.out.println(sb.toString());
			in.close();
		} catch (Exception e) { // Report any errors that arise
			sb.append(e.toString());
			System.err.println(e);
			System.err.println("Usage:   java   HttpClient   <URL>   [<filename>]");
		}
		return sb.toString();
	}
    private static void parse(String line,List<String>data) {

		Pattern p=Pattern.compile("^([[\\s]]+)([\u4e00-\u9fa5]+)(.*)(</a>)");
//		Pattern q=Pattern.compile("苏E-[0-9]{5}.*\\d{2}:\\d{2}:\\d{2}");
		Pattern q=Pattern.compile("(苏E-\\w{5}).*：(.*:\\d{2}:\\d{2})");
		Matcher m=p.matcher(line);
		Matcher n=q.matcher(line);

			  while(m.find())
		     {
				data.add(m.group(2));
	//			System.out.println(m.group(2));
		     }
			  while(n.find())
			  {
				  data.add(n.group(1)+"进站:"+n.group(2));
	  //		  System.out.println(n.group());
			  }


	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_listroute, menu);
		return true;
	}

}
