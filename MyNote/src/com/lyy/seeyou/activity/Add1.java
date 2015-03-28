package com.lyy.seeyou.activity;

import com.hlb.R;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Add1 extends Activity{
	
	private ImageView face = null;
	private Button start = null ;
	private AnimationDrawable draw = null ;//动画操作
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab_friends);
		
		this.face = (ImageView) super.findViewById(R.id.face);
		this.start = (Button) super.findViewById(R.id.start);
		this.start.setOnClickListener(new OnClickListenerImpl()) ;
	}
	
	 private class OnClickListenerImpl implements OnClickListener {
	    	@Override
	    	public void onClick(View v) {
	    	//设置动画资源
	    		Add1.this.face.setBackgroundResource(R.anim.allface) ;
	    		Add1.this.draw  =  (AnimationDrawable)
	    				Add1.this.face
	    	.getBackground();//取得背景的 Drawable
	    		Add1.this.draw.setOneShot(false) ;//动画执行次数
	    	Add1.this.draw.start() ;//开始动画
	    	}
	    	}
}
