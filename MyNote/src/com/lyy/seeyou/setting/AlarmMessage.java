package com.lyy.seeyou.setting;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.hlb.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class AlarmMessage extends Activity {
@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	new AlertDialog.Builder(this)//�����Ի���
	.setIcon(R.drawable.bg_h1)//����ͼƬ
	.setTitle("����ʱ���ѵ���")//���öԻ������
	.setMessage("������������ʱ�䣺"+ new SimpleDateFormat("yyyy �� MM �� dd �� HH ʱ mm�� ss ��")
	.format(new Date())).setPositiveButton("�ر�", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			AlarmMessage.this.finish();//�رնԻ����������
			}
		}).show();
	}
}