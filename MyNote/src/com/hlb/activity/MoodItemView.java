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
 * MoodItemView * ���ܣ���MoodLogActivity�д��ݹ�����Uri��ʶ�� �����ݿ��в�ѯ�����¼�����ݣ��ٽ� *
 * ����������ʾ�ڵ�ǰActivity�� *
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
	private Cursor moodCursor = null;// ��Uri�����ݿ��в�ѯ��õ��Ľ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ò����ļ�
		setContentView(R.layout.mood_viewer);
		// �ؼ���ʼ��
		setView();
	}

	/**
	 * ����menu�˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// ���ò˵�
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mooditemview_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * ���ò˵���ϸ����
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		// ɾ����ǰ�����¼
		case R.id.item_delete:
			// ��Uri��ַɾ����ǰ�����¼
			getContentResolver().delete(moodUri, null, null);
			finish();
			return true;
			// ���ص�MoodLogActivity����
		case R.id.item_back:
			finish();
			return true;
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	/**
	 * �ؼ���ʼ��
	 */
	@SuppressWarnings("deprecation")
	public void setView() {
		titleTextView = (TextView) findViewById(R.id.ivTitleName);
		titleTextView.setText("�鿴����");

		backTextView = (TextView) findViewById(R.id.ivTitleBtnLeft);
		backTextView.setText("����");
		backTextView.setVisibility(View.VISIBLE);
		backTextView.setOnClickListener(new MyClickListener());

		ediTextView = (TextView) findViewById(R.id.ivTitleBtnRight);
		ediTextView.setText("�༭");
		ediTextView.setVisibility(View.VISIBLE);
		ediTextView.setOnClickListener(new MyClickListener());

		dateView = (TextView) findViewById(R.id.mood_view_date);
		moodView = (TextView) findViewById(R.id.mood_view_text);
		moodIconView = (ImageView) findViewById(R.id.mood_view_image);

		// ��MoodLogActivity���ݵ�Intent�õ������¼��Uri
		Intent intent = getIntent();
		moodUri = intent.getData();
		// ͨ��Uri�����ݿ��в�ѯ���õ���ѯ�����cursor����
		moodCursor = managedQuery(moodUri, AppConstants.PROJECTION, null, null,
				null);
		// �ҳ�ѡ����Ŀ�����ݣ����ڵ�ǰActivity����ʾ
		if (moodCursor != null) {
			moodCursor.moveToNext();
			// �õ���������
			moodItem = moodCursor.getString(moodCursor
					.getColumnIndex(AppConstants.MoodColumns.MOODITEM));
			// �õ������¼��ʱ���
			timeStamp = moodCursor.getLong(moodCursor
					.getColumnIndex(AppConstants.MoodColumns.TIMESTAMP));
			// �õ��������������
			icon_id = moodCursor.getInt(moodCursor
					.getColumnIndex(AppConstants.MoodColumns.ICON_ID));
		} else {// ��ѯ���Ϊ��,������ǰActivity
			finish();
		}
		// ��ʱ����õ������¼ʱ����ַ���
		String moodItemDate = Utils.getMoodItemDate(timeStamp);
		// �ڿؼ�����ʾ��������
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
