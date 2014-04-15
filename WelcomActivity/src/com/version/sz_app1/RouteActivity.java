package com.version.sz_app1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import eric.sql.db.DatabaseHelper;

public class RouteActivity extends ListActivity {

	private Intent intent;
	List<String> data = new ArrayList<String>();
	private Button   routebutton;
	private Button   favorroute;
	private Button   backbtn;
	private EditText routeinput;
	private static String[] strlist;
	private static int strsize;
	private ProgressBar firstBar;
	private static int i=0;
	private static String[] sendrouteinfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		
		routebutton =(Button)findViewById(R.id.button1);
		favorroute  =(Button)findViewById(R.id.button2);
		routeinput  =(EditText)findViewById(R.id.editText1);
		firstBar = (ProgressBar)findViewById(R.id.progressBar1);
		backbtn  = (Button)findViewById(R.id.buttonback);
		
		
		routebutton.setText(R.string.route_select); 
		favorroute.setText(R.string.favor);
		backbtn.setText(R.string.back);
		firstBar.setVisibility(View.GONE); 
		
		routebutton.setOnClickListener(new searchbus());
		favorroute.setOnClickListener(new addfavorroute());
		backbtn.setOnClickListener(new backmain());
		
		strlist= new String[100];
		sendrouteinfo = new String[5];
		strsize=0;
		System.out.println("|||||||"+data);
//		data=null;
		intent = getIntent();
		if(intent!=null&&intent.getStringExtra("routestring")!=null)
		{
			new Thread() {
	            @Override
				public void run() {                        
	                    try {
	                    	
	                    	i=0;
	                		String dom=intent.getStringExtra("routestring");
	                		String realbusstring=URLEncoder.encode(dom);
	                		String routeurl  ="http://wap.139sz.cn/bus/search?st=1&paging=y&kw="+realbusstring;
	                		System.out.println(routeurl);
	                		firstBar.setVisibility(View.VISIBLE);
	                    	getWebCon(routeurl,data);  
	                        //   连接网络获取数据
	                    
	                    	
	                    } catch (Exception e) {
	                            // 在GUI显示错误提示
	                            // tv.setText("Error: " + e.getMessage());
	                            
	                    }
	                    
	                    Message msg_listData = new Message();
	                    handler.sendMessage(msg_listData);
	            }
			}.start();
		}
		
		setListAdapter(new ArrayAdapter<String>(RouteActivity.this, android.R.layout.simple_list_item_1,data));	
		System.out.println("|||||||"+data);
	}
	
	class backmain implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(RouteActivity.this,SelectActivity.class);
			RouteActivity.this.startActivity(intent);
		}
		
	}
	
	class addfavorroute implements OnClickListener{

		@Override
		public void onClick(View v) {
			String sinput=routeinput.getText().toString();
			Pattern p = Pattern.compile("[a-zA-Z0-9\\s\u4e00-\u9fa5]+");
			Matcher m = p.matcher(sinput);
			if(m.matches()==true)
			{//生成ContentValues对象
				ContentValues values = new ContentValues();
				//想该对象当中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
				values.put("type", 1);
				values.put("name",sinput);
				DatabaseHelper dbHelper = new DatabaseHelper(RouteActivity.this,"test_db",2);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				//调用insert方法，就可以将数据插入到数据库当中
				db.insert("fav", null, values);
				Toast.makeText(RouteActivity.this, "保存成功",  
	                    Toast.LENGTH_LONG).show(); 
			}
			else
			{
				Toast.makeText(RouteActivity.this, "不符合存储要求,保存失败",  
	                    Toast.LENGTH_LONG).show(); 
			}
			
			
		}
		
	}
	
	
	
	 class searchbus implements OnClickListener{

			@Override
			public void onClick(View v) {
				data.clear();
				i=0;
				Toast.makeText(RouteActivity.this, "正在查询请稍后",  
	                    Toast.LENGTH_LONG).show(); 
				System.out.println("开始查询路线..............."+"data:"+data);
				firstBar.setVisibility(View.VISIBLE);
				new Thread() {
		            @Override
					public void run() {                        
		                    try {
	//	                    	data=null;
		                    	System.out.println(data);
		                		String strroute =null;     
		    					String routeurl = null;
		    					String strrouteutf=null;
		    					strroute=routeinput.getText().toString();
		    					
		    					strrouteutf=URLEncoder.encode(strroute);
		    					routeurl="http://wap.139sz.cn/bus/search?st=1&paging=y&kw="+strrouteutf;
		    					System.out.println("----------------------------");
		    					System.out.println(routeurl);
		                 		firstBar.setVisibility(View.VISIBLE);
		    					getWebCon(routeurl,data); 
		                        //   连接网络获取数据
		    					System.out.println("!!!!!!"+data);
//		    					setListAdapter(new ArrayAdapter<String>(RouteActivity.this, android.R.layout.simple_list_item_1,data));	
		                    
		                    	
		                    } catch (Exception e) {
		                            // 在GUI显示错误提示
		                            // tv.setText("Error: " + e.getMessage());
		                            
		                    }
//		                    setListAdapter(new ArrayAdapter<String>(RouteActivity.this, android.R.layout.simple_list_item_1,data));	
		                    Message msg_listData = new Message();
		                    handler.sendMessage(msg_listData);
		            }
				}.start();
				
			}
	    }

		private Handler handler = new Handler() {               

	        @Override
			public void handleMessage(Message message) {
	        	
	        	firstBar.setVisibility(View.GONE);       	
	        }
	}; 

		    public static String getWebCon(String domain,List<String>data) {
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
//					System.out.println(sb.toString());
					in.close();
				} catch (Exception e) { // Report any errors that arise
					sb.append(e.toString());
					System.err.println(e);
					System.err.println("Usage:   java   HttpClient   <URL>   [<filename>]");
				}
				return sb.toString();
			}	 
		    
//		    private static void parse(String line,List<String>data) {
//
//				Pattern p =Pattern.compile("href=.*>(.*)</span><span>(.*)</span></a>");
////		    	Pattern p =Pattern.compile("href=(.*)><.*>(.*)</span><span>(.*)</span></a>");
//				Matcher m=p.matcher(line);
////				Matcher n=q.matcher(line);
//				int i=0;
//				while(m.find())
//				{
////					System.out.println(m.group(1)+m.group(2));
////					String str=m.group(1);
////					data.add(m.group(1)+m.group(2));
////					strlist[i++]=str.substring(1,str.length()-1);
////					strsize++;
//					data.add(m.group(1)+m.group(2));
////					data.add(m.group(2));
//					
//					
//					
//					
////					System.out.println(str.substring(1,str.length()-1));
//					System.out.println(m.group(1));
//					System.out.println(m.group(2));
//				}
//
//			}

		    private static void parse(String line,List<String>data) {
		    	Pattern p =Pattern.compile("dataGuid=(\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}).*(ftime=.*:\\d{2}).*>(.*)</span><span>.*：(.*)</span></a>");
//		    	Pattern p =Pattern.compile("dataGuid=(\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}).*(ftime=\\d{2}.*:\\d{2}).*>(.*)</span><span>.*：(.*)</span></a>");
//		    	Pattern p =Pattern.compile("dataGuid=(\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}).*>(.*)</span><span>.*：(.*)</span></a>");		    	
//		    	Pattern p =Pattern.compile("dataGuid=(\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}).*>(.*)</span><span>(.*)</span></a>");
//		    	Pattern p =Pattern.compile("href=(.*)><.*>(.*)</span><span>(.*)</span></a>");
				Matcher m=p.matcher(line);
//				Matcher n=q.matcher(line);
//				System.out.println("解析页面了!!!!!!");
				while(m.find())
				{
					
					data.add(m.group(3)+":to:"+m.group(4).substring(0,m.group(4).length()-1));
//					data.add(m.group(3).substring(0,m.group(3).length()-1));
//					strlist[i]=m.group(1);
					strsize++;
					System.out.println(i);
					strlist[i]="http://wap.sz-map.com/real?st=1&dataGuid="
								+m.group(1)+"&rt=n&"+m.group(2)+
								"&sln=" +
								URLEncoder.encode(m.group(3))+
								"&line_kw="+
								"m.group(3)+" +
								"&endSN="+URLEncoder.encode(m.group(4).substring(0,m.group(4).length()-1));
					sendrouteinfo[i]=m.group(3);
					i++;
					System.out.println("AAAAAAAAAAAAAAAAA");
					System.out.println(m.group(1));
					System.out.println(m.group(2));
					System.out.println(m.group(3));
					System.out.println(m.group(4).substring(0,m.group(4).length()-1));
				}
	//			System.out.println(strsize);
			}	    
		    
		    
		    
	 @Override  
	 protected void onListItemClick(ListView l, View v, int position, long id) {  
		    // TODO Auto-generated method stub  
		     super.onListItemClick(l, v, position, id);  
		     //显示单击条目ID号  
		      System.out.println("id = " + id);  
		        //显示所单击条目的位置数  
		      System.out.println("position = " + position);  
		  	Toast.makeText(RouteActivity.this, "正在查询请稍后",  
                    Toast.LENGTH_LONG).show(); 
		    String url,routeinfo;
		    url=null; 
		    routeinfo=null;
		  	Intent intent = new Intent();
		  	url=strlist[position];
		  	routeinfo=sendrouteinfo[position];
	//	  	System.out.println(url);
			intent.putExtra("urlstring", url);
			intent.putExtra("routeinfostring", routeinfo);
			intent.setClass(RouteActivity.this,ListrouteActivity.class);
			RouteActivity.this.startActivity(intent);
		
	}
  

	  
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_route, menu);
		return true;
	}

}
