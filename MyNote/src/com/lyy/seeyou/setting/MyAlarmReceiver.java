package com.lyy.seeyou.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarmReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		Intent it = new Intent(context, AlarmMessage.class);//����Ҫ������ Intent
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//����һ���µ�������
		context.startActivity(it);//���� Intent
	}
}
