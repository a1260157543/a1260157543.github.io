package com.lyy.seeyou.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarmReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		Intent it = new Intent(context, AlarmMessage.class);//定义要操作的 Intent
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//传递一个新的任务标记
		context.startActivity(it);//启动 Intent
	}
}
