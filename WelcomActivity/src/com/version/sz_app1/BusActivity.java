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

public class BusActivity extends ListActivity {

	private Intent intent; 
	List<String> data = new ArrayList<String>();
	private ProgressBar firstBar;
	private Button   busbutton;
	private Button   favorbus;
	private Button   backbtn;
	private EditText businput;
	private static String[] strbuslist;
	private static int strbussize;
	private static int ibus=0;
	private static String[] sendbusinfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus);
		
		busbutton =(Button)findViewById(R.id.button1);
		favorbus  =(Button)findViewById(R.id.button2);
		businput  =(EditText)findViewById(R.id.editText1);
		firstBar = (ProgressBar)findViewById(R.id.progressBar1);
		backbtn  =(Button)findViewById(R.id.button3);
		
		
		busbutton.setText(R.string.bus_select);
		favorbus.setText(R.string.favor);
		backbtn.setText(R.string.back);
		firstBar.setVisibility(View.GONE); 

		busbutton.setOnClickListener(new searchstation());
		favorbus.setOnClickListener(new addfavorbus());
		backbtn.setOnClickListener(new backmain());
		strbuslist= new String[5];
		sendbusinfo = new String[5];
		strbussize=0;
		
//		data=null;
		intent =getIntent();
		if(intent!=null&&intent.getStringExtra("busstring")!=null)
		{
			new Thread() {
	            @Override
				public void run() {                        
	                    try {
	                    	
	                    	ibus=0;
	                		String dom=intent.getStringExtra("busstring");
	                		String realbusstring=URLEncoder.encode(dom);
	                		String busurl  ="http://wap.139sz.cn/bus/search?st=0&paging=y&kw="+realbusstring;
	                		System.out.println(dom);
	                		firstBar.setVisibility(View.VISIBLE);
	                    	getWebCon(busurl,data);  
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
		setListAdapter(new ArrayAdapter<String>(BusActivity.this, android.R.layout.simple_list_item_1,data));
	}
	
	class backmain implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(BusActivity.this,SelectActivity.class);
			BusActivity.this.startActivity(intent);
			
		}
		
	}
	
	class addfavorbus implements OnClickListener{

		@Override
		public void onClick(View v) {
			String sinput=businput.getText().toString();
			Pattern p = Pattern.compile("[a-zA-Z0-9\\s\u4e00-\u9fa5]+");
			Matcher m = p.matcher(sinput);			
			//生成ContentValues对象
			if(m.matches()==true)
			{
					ContentValues values = new ContentValues();
				
				//想该对象当中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
				values.put("type", 2);
				values.put("name",sinput);
				DatabaseHelper dbHelper = new DatabaseHelper(BusActivity.this,"test_db",2);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				//调用insert方法，就可以将数据插入到数据库当中
				db.insert("fav", null, values);
				Toast.makeText(BusActivity.this, "保存成功",  
	                    Toast.LENGTH_LONG).show(); 
			}
			else
			{
				Toast.makeText(BusActivity.this, "不符合存储要求,保存失败",  
	                    Toast.LENGTH_LONG).show(); 
			}
		}
		
	}
	
	
	
	class searchstation implements OnClickListener{

		@Override
		public void onClick(View v) {
			ibus=0;
			data.clear();
			firstBar.setVisibility(View.VISIBLE);
			Toast.makeText(BusActivity.this, "正在查询请稍后",  
                    Toast.LENGTH_LONG).show(); 
			new Thread() {
	            @Override
				public void run() {                        
	                    try {
	//                    	data=null;
	                    	String strroute =null;
	    					String busurl = null;
	    					String strrouteutf=null;
	    					strroute=businput.getText().toString();
	    					
	    					strrouteutf=URLEncoder.encode(strroute);
	    					busurl  ="http://wap.139sz.cn/bus/search?st=0&paging=y&kw="+strrouteutf;
//	    					routeurl="http://wap.sz-map.com/search?st=1&paging=y&kw="+strrouteutf;
	    					System.out.println("----------------------------");
	    					System.out.println(busurl);
	    					getWebCon(busurl,data);
	                        //   连接网络获取数据
//	    					setListAdapter(new ArrayAdapter<String>(BusActivity.this, android.R.layout.simple_list_item_1,data));
	                    	
	                    } catch (Exception e) {
	                            // 在GUI显示错误提示
	                            // tv.setText("Error: " + e.getMessage());
	                            
	                    }
//	                    setListAdapter(new ArrayAdapter<String>(BusActivity.this, android.R.layout.simple_list_item_1,data));
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
		    
		    private static void parse(String line,List<String>data) {
		    	Pattern p =Pattern.compile("(sGuid=\\w{8}-\\w{4}-.*\\w{10})&amp.*(dataGuid=\\w{3}).*>(.*)</span>(.*)<span.*>(.*)</span>");
//		    	Pattern p =Pattern.compile("dataGuid=(\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}).*(ftime=.*:\\d{2}).*>(.*)</span><span>.*：(.*)</span></a>");
//		    	Pattern p =Pattern.compile("dataGuid=(\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}).*(ftime=\\d{2}.*:\\d{2}).*>(.*)</span><span>.*：(.*)</span></a>");
//		    	Pattern p =Pattern.compile("dataGuid=(\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}).*>(.*)</span><span>.*：(.*)</span></a>");		    	
//		    	Pattern p =Pattern.compile("dataGuid=(\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}).*>(.*)</span><span>(.*)</span></a>");
//		    	Pattern p =Pattern.compile("href=(.*)><.*>(.*)</span><span>(.*)</span></a>");
				Matcher m=p.matcher(line);
//				Matcher n=q.matcher(line);
				
				while(m.find())
				{
					
//					data.add(m.group(3)+":to:"+m.group(4).substring(0,m.group(4).length()-1));
//					data.add(m.group(3).substring(0,m.group(3).length()-1));
//					strbuslist[ibus]=m.group(1);
					strbussize++;
//					http://wap.sz-map.com/real?st=0&sGuid=9d93c4c0-d65d-651d-5561-574eceacf7d4
//					&rt=n&dataGuid=ENT&sln=中科大&address=位于仁爱路北&kw=中科大
					strbuslist[ibus]="http://wap.sz-map.com/real?st=0&"+
								m.group(1)+
							    "&rt=n&"+
							    m.group(2)+
								"&sln="+
								URLEncoder.encode(m.group(3))+
								"&address="+
								m.group(4).substring(1,m.group(4).length())+m.group(5)+
								"&kw="+URLEncoder.encode(m.group(3));
					sendbusinfo[ibus]=m.group(3);
					ibus++;
					
					
					
					System.out.println(m.group(1));
					System.out.println(m.group(2));
					System.out.println(m.group(3));
					System.out.println(m.group(4).substring(1,m.group(4).length())+m.group(5));
					
					data.add(m.group(3)+m.group(4).substring(1,m.group(4).length())+m.group(5));
					
					System.out.println("AAAAAAAAAAAAAAAAA");
					System.out.println(m.group(1));
					System.out.println(m.group(2));
					System.out.println(m.group(3));
					System.out.println(m.group(4).substring(0,m.group(4).length()-1));
				}

			}	
	
			 @Override  
			 protected void onListItemClick(ListView l, View v, int position, long id) {  
				    // TODO Auto-generated method stub  
				     super.onListItemClick(l, v, position, id);  
				     //显示单击条目ID号  
				      System.out.println("id = " + id);  
				        //显示所单击条目的位置数  
				      System.out.println("position = " + position);  
				      Toast.makeText(BusActivity.this, "正在查询请稍后",  
			                    Toast.LENGTH_LONG).show(); 
				    String url,businfo;
				    url=null; 
				    businfo= null;
				  	Intent intent = new Intent();
				  	url=strbuslist[position];
				  	businfo= sendbusinfo[position];
					intent.putExtra("urlstring", url);
					intent.putExtra("businfostring", businfo);
					intent.setClass(BusActivity.this,BuslistnActivity.class);
					BusActivity.this.startActivity(intent);
			}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_bus, menu);
		return true;
	}

}
