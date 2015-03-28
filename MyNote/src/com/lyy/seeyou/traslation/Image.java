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
	private ZoomControls zoomcontrols = null ;//�������
	private TextView text = null ;
	private ImageView i;
	private int size = 10 ; // �Ǳ������ִ�С
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	super.setContentView(R.layout.friend);
	this.zoomcontrols = (ZoomControls) super.findViewById(R.id.zommcontrols) ;
	this.text = (TextView) super.findViewById(R.id.text) ;
	this.i=(ImageView) findViewById(R.id.i);
	this.zoomcontrols.setOnZoomInClickListener(new OnZoomInClickListenerImpl()) ;//���÷Ŵ����
	this.zoomcontrols.setOnZoomOutClickListener(new
			OnZoomOutClickListenerImpl()) ; //������С����
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
