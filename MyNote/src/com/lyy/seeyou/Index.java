package com.lyy.seeyou;

import com.hlb.R;
import com.lyy.seeyou.activity.MainActivity;
import com.lyy.seeyou.loading.Net;
import com.lyy.seeyou.loading.NetPage;
import com.lyy.seeyou.loading.What_viewpager;
import com.lyy.seeyou.traslation.Screen;

import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class Index extends Activity{

	LinearLayout layout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.index);
		layout=(LinearLayout) findViewById(R.id.layout);
		Screen screen=new Screen();
		screen.AotoBackground(this, layout,R.drawable.bg_v1,R.drawable.bg_h1);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);   //全屏显示
		//Toast.makeText(getApplicationContext(), "孩子！好好背诵！", Toast.LENGTH_LONG).show();
		//overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
		
	new Handler().postDelayed(new Runnable(){
		@Override
		public void run(){
			Intent intent = new Intent (Index.this,MainActivity.class);			
			startActivity(intent);			
			Index.this.finish();
		}
	}, 3000);
   }
	
}
