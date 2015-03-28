package com.hlb.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.hlb.app.AppConstants;
import com.hlb.utils.Utils;
import com.hlb.R;

/********************************************************************************
 * MoodItemView * 功能：由MoodLogActivity中传递过来的Uri标识， 在数据库中查询心情记录的内容，再将 *
 * 心情内容显示在当前Activity中 *
 ******************************************************************************/

public class MoodItemView extends Activity {
	private TextView titleTextView = null;
	private TextView backTextView = null;
	private TextView ediTextView = null;

	private TextView dateView = null;
	private TextView moodView = null;
	private ImageView moodIconView = null;
	private long timeStamp = 0;
	private String moodItem = null;
	private Uri moodUri = null;
	private int icon_id = 0;
	private Cursor moodCursor = null;// 由Uri在数据库中查询后得到的结果集

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置布局文件
		setContentView(R.layout.mood_viewer);
		// 控件初始化
		setView();
	}

	/**
	 * 创建menu菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 设置菜单
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mooditemview_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 设置菜单详细功能
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		// 删除当前心情记录
		case R.id.item_delete:
			// 由Uri地址删除当前心情记录
			getContentResolver().delete(moodUri, null, null);
			finish();
			return true;
			// 返回到MoodLogActivity界面
		case R.id.item_back:
			finish();
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	/**
	 * 控件初始化
	 */
	@SuppressWarnings("deprecation")
	public void setView() {
		titleTextView = (TextView) findViewById(R.id.ivTitleName);
		titleTextView.setText("查看记事");

		backTextView = (TextView) findViewById(R.id.ivTitleBtnLeft);
		backTextView.setText("返回");
		backTextView.setVisibility(View.VISIBLE);
		backTextView.setOnClickListener(new MyClickListener());

		ediTextView = (TextView) findViewById(R.id.ivTitleBtnRight);
		ediTextView.setText("编辑");
		ediTextView.setVisibility(View.VISIBLE);
		ediTextView.setOnClickListener(new MyClickListener());

		dateView = (TextView) findViewById(R.id.mood_view_date);
		moodView = (TextView) findViewById(R.id.mood_view_text);
		moodIconView = (ImageView) findViewById(R.id.mood_view_image);

		// 由MoodLogActivity传递的Intent得到心情记录的Uri
		Intent intent = getIntent();
		moodUri = intent.getData();
		// 通过Uri在数据库中查询，得到查询结果集cursor对象
		moodCursor = managedQuery(moodUri, AppConstants.PROJECTION, null, null,
				null);
		// 找出选中条目的内容，并在当前Activity中显示
		if (moodCursor != null) {
			moodCursor.moveToNext();
			// 得到心情内容
			moodItem = moodCursor.getString(moodCursor
					.getColumnIndex(AppConstants.MoodColumns.MOODITEM));
			// 得到心情记录的时间戳
			timeStamp = moodCursor.getLong(moodCursor
					.getColumnIndex(AppConstants.MoodColumns.TIMESTAMP));
			// 得到该条心情的评分
			icon_id = moodCursor.getInt(moodCursor
					.getColumnIndex(AppConstants.MoodColumns.ICON_ID));
		} else {// 查询结果为空,结束当前Activity
			finish();
		}
		// 由时间戳得到心情记录时间的字符串
		String moodItemDate = Utils.getMoodItemDate(timeStamp);
		// 在控件中显示心情内容
		dateView.setText(moodItemDate);
		moodView.setText(Utils.convertNormalStringToSpannableString(
				MoodItemView.this, moodItem), BufferType.SPANNABLE);
		moodIconView.setImageResource(icon_id);
	}

	private class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = null;
			switch (v.getId()) {
			case R.id.ivTitleBtnLeft:
				intent = new Intent();
				intent.setClass(MoodItemView.this, MoodLogActivity.class);
				startActivity(intent);
				finish();
				break;
			case R.id.ivTitleBtnRight:
				intent = new Intent(Intent.ACTION_INSERT, moodUri);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setView();
	}

}
