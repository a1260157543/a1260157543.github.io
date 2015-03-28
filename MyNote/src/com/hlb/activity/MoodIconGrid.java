package com.hlb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hlb.app.AppConstants.String_Const;
import com.hlb.R;

/**************************************************************************************
 * MoodIconGrid 																	  *
 * 功能: 以GridView的布局呈现可供选择的心情表情，用户点击选择合适的表情并返回NewMoodAddit *
 *************************************************************************************/

public class MoodIconGrid extends Activity {
	public static final String EXT_IdIcon = "id";
	public static final String EXT_NumberIcon = "num";
	private MoodIconAdapter moodIconAdapter; //MoodIconAdapter对象，用于将表情选择界面呈现

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.grid_icon);
		//得到布局对象
		GridView localGridView = (GridView) findViewById(R.id.iconGridView);
		//将MoodIconAdapter对象实例化
		this.moodIconAdapter = new MoodIconAdapter(getApplicationContext());
		//设置Adapter
		localGridView.setAdapter(this.moodIconAdapter);
		//设置界面选项点击监听器
		localGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int position, long id) {
				Intent intent = new Intent();
				//将选择的图片ID保存入intent中
				intent.putExtra(String_Const.MOOD_ICON, (int)id);
				//将结果返回，并关闭当前Activity
				MoodIconGrid.this.setResult(RESULT_OK, intent);
				MoodIconGrid.this.finish();
				return;
			}
		});
	}
}
