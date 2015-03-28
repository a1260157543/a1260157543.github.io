package com.lyy.seeyou.traslation;

import com.hlb.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

public class Image extends Activity {
	private ZoomControls zoomcontrols = null ;//缩放组件
	private TextView text = null ;
	private ImageView i;
	private int size = 10 ; // 是保存文字大小
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	super.setContentView(R.layout.friend);
	this.zoomcontrols = (ZoomControls) super.findViewById(R.id.zommcontrols) ;
	this.text = (TextView) super.findViewById(R.id.text) ;
	this.i=(ImageView) findViewById(R.id.i);
	this.zoomcontrols.setOnZoomInClickListener(new OnZoomInClickListenerImpl()) ;//设置放大监听
	this.zoomcontrols.setOnZoomOutClickListener(new
			OnZoomOutClickListenerImpl()) ; //设置缩小监听
			}
			private class OnZoomInClickListenerImpl implements OnClickListener {
			@Override
			public void onClick(View v) {
				Image.this.size = size + 2 ;
				Image.this.text.setTextSize(size) ;
				Image.this.i.setAlpha(size);
				}
			}
			private class OnZoomOutClickListenerImpl implements OnClickListener {
			@Override
				public void onClick(View v) {
				Image.this.size = size - 2 ;
				Image.this.text.setTextSize(size) ;
				Image.this.i.setAlpha(size);
				}
	}
}
