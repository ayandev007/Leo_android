package com.version.sz_app1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class SelectActivity extends Activity {

	private Button routeSelect;
	private Button busSelect;
	private ImageButton FavoriteButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("!!!!!!!!!!!!!!!!");
		setContentView(R.layout.activity_select);
		
		routeSelect = (Button)findViewById(R.id.button1);
		routeSelect.setText(R.string.route_select);
		routeSelect.setOnClickListener(new Select_route());
		
		busSelect   = (Button)findViewById(R.id.button2);
		busSelect.setText(R.string.bus_select);
		busSelect.setOnClickListener(new Select_bus());	
		
		FavoriteButton= (ImageButton)findViewById(R.id.imageButton1);
		FavoriteButton.setOnClickListener(new turntofavor());
	}
	
	class turntofavor implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(SelectActivity.this,SQLActivity.class);
			SelectActivity.this.startActivity(intent);
		}
		
	}
	class Select_route implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(SelectActivity.this,RouteActivity.class);
			SelectActivity.this.startActivity(intent);
		}
		
	}

	class Select_bus implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(SelectActivity.this,BusActivity.class);
			SelectActivity.this.startActivity(intent);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_select, menu);
		return true;
	}

}
