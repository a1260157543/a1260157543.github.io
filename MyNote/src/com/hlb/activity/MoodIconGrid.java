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
 * ����: ��GridView�Ĳ��ֳ��ֿɹ�ѡ���������飬�û����ѡ����ʵı��鲢����NewMoodAddit *
 *************************************************************************************/

public class MoodIconGrid extends Activity {
	public static final String EXT_IdIcon = "id";
	public static final String EXT_NumberIcon = "num";
	private MoodIconAdapter moodIconAdapter; //MoodIconAdapter�������ڽ�����ѡ��������

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.grid_icon);
		//�õ����ֶ���
		GridView localGridView = (GridView) findViewById(R.id.iconGridView);
		//��MoodIconAdapter����ʵ����
		this.moodIconAdapter = new MoodIconAdapter(getApplicationContext());
		//����Adapter
		localGridView.setAdapter(this.moodIconAdapter);
		//���ý���ѡ����������
		localGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int position, long id) {
				Intent intent = new Intent();
				//��ѡ���ͼƬID������intent��
				intent.putExtra(String_Const.MOOD_ICON, (int)id);
				//��������أ����رյ�ǰActivity
				MoodIconGrid.this.setResult(RESULT_OK, intent);
				MoodIconGrid.this.finish();
				return;
			}
		});
	}
}
