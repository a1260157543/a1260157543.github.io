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
	new AlertDialog.Builder(this)//建立对话框
	.setIcon(R.drawable.bg_h1)//设置图片
	.setTitle("闹钟时间已到！")//设置对话框标题
	.setMessage("闹钟响起，现在时间："+ new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH 时 mm分 ss 秒")
	.format(new Date())).setPositiveButton("关闭", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			AlarmMessage.this.finish();//关闭对话框后程序结束
			}
		}).show();
	}
}