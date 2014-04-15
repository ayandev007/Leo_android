package com.version.sz_app1;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import eric.sql.db.DatabaseHelper;

//public class SQLActivity extends ListActivity{
public class SQLActivity extends Activity{
    /** Called when the activity is first created. */
	//private Button updateRecordButton;
	//private Button historyQueryButton;
	private Button favouriteQueryButton;
	private List<String> data = new ArrayList<String>();
	private ListView  list;
	private boolean islongclick=false;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        // Check if the database exist
        
        setContentView(R.layout.activity_sql);

        list= (ListView)findViewById(R.id.list1);
        favouriteQueryButton = (Button)findViewById(R.id.showfav);
        favouriteQueryButton.setOnClickListener(new FavouriteQueryListener());//
        list.setOnItemClickListener(new onclick());
        list.setOnItemLongClickListener(new longclick());
        showlist();
        
        islongclick=false;
//        data.add("ssssss");
//        System.out.println(data);
//        setListAdapter(new ArrayAdapter<String>(SQLActivity.this, android.R.layout.simple_list_item_1,data));

    }
    
    class onclick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) 
		{
			if(islongclick==false)
			{// TODO Auto-generated method stub
				System.out.println("短按查询");
				DatabaseHelper dbHelper = new DatabaseHelper(SQLActivity.this,"test_db");
				SQLiteDatabase db = dbHelper.getReadableDatabase();
				String item = data.get(position);
				//System.out.println("query--->" + item);
				//itemutf=URLEncoder.encode(item);
				Cursor cursor = db.rawQuery("select type from fav where name='"+item+"';", null);
		    	while(cursor.moveToNext())
		    	{
					String type = cursor.getString(cursor.getColumnIndex("type"));
					System.out.println("query---!!!!!>" + type);
					int typeint = Integer.parseInt(type);
					if (typeint == 1)
					{
		    			Toast.makeText(SQLActivity.this, "点击查询收藏条目",  
		                        Toast.LENGTH_LONG).show(); 
		    			
	    				System.out.println("点击查询收藏条目");
	    				Intent intent = new Intent();
		    			intent.putExtra("routestring", item);
		    			intent.setClass(SQLActivity.this,RouteActivity.class);
		    			SQLActivity.this.startActivity(intent);
					}
					if (typeint == 2)
					{
		    			Toast.makeText(SQLActivity.this, "点击查询收藏条目",  
		                        Toast.LENGTH_LONG).show(); 
		    			
		    			Intent intent = new Intent();
		    			intent.putExtra("busstring", item);
		    			intent.setClass(SQLActivity.this,BusActivity.class);
		    			SQLActivity.this.startActivity(intent);
					}
		    	
		    		
		    	}
			}
			else
			{
				islongclick=false;
				Toast.makeText(SQLActivity.this, "删除成功",  
	                    Toast.LENGTH_LONG).show(); 
			}
		}
    	
    }
    
    class longclick implements OnItemLongClickListener{

    	@Override  
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,  
                int arg2, long arg3) {
    		islongclick=true;
//			Toast.makeText(SQLActivity.this, "长按删除收藏条目",  
//                    Toast.LENGTH_LONG).show(); 
			String item = data.get(arg2);
			System.out.println(item);
			
			DatabaseHelper dbHelper = new DatabaseHelper(SQLActivity.this,"test_db");
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			db.delete("fav", "name=?", new String[] {item});
			data.clear();
			showlist();
            return false;  
        }  
    	
    }
    
   
    class CreateListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			//创建一个DatabaseHelper对象
			DatabaseHelper dbHelper = new DatabaseHelper(SQLActivity.this,"test_db");
			//只有调用了DatabaseHelper对象的getReadableDatabase()方法，或者是getWritableDatabase()方法之后，才会创建，或打开一个数据库
			SQLiteDatabase db = dbHelper.getReadableDatabase();
		}
    }
    class UpdateListener implements OnClickListener{

		@Override
		public void onClick(View v) 
		{
			DatabaseHelper dbHelper = new DatabaseHelper(SQLActivity.this,"test_mars_db",2);
			SQLiteDatabase db = dbHelper.getReadableDatabase();
		}
    	
    }
    public void showlist()
    {
    	DatabaseHelper dbHelper = new DatabaseHelper(SQLActivity.this,"test_db");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select distinct * from fav;", null);
    	while(cursor.moveToNext())
    	{

			String name = cursor.getString(cursor.getColumnIndex("name"));
			//String name =  URLDecoder.decode(nameutf);
			data.add(name);
			System.out.println("query---!!!!!>" + name);
		}
    	System.out.println(data);
    	//list.setListAdapter(new ArrayAdapter<String>(SQLActivity.this, android.R.layout.simple_list_item_1,data));
    	
    	list.setAdapter(new ArrayAdapter<String>(SQLActivity.this, android.R.layout.simple_list_item_1,data));
    }
    
    class FavouriteQueryListener implements OnClickListener{

		@Override
		public void onClick(View v) 
		{
			Toast.makeText(SQLActivity.this, "点击查询，长按删除",  
                    Toast.LENGTH_LONG).show();
//			ListView  list = (ListView)findViewById(R.id.list);
			System.out.println("aaa------------------");
	    	data.clear();
			Log.d("myDebug", "myFirstDebugMsg");
			showlist();
		}
    }
}