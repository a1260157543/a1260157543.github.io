package com.lyy.seeyou.setting;

import java.util.Calendar;

import com.hlb.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class MyAlarmManagerDemo extends Activity {
	private AlarmManager alarm = null; // ���ӷ���
	private Button set = null;
	private Button delete = null;
	private TextView msg = null;
	private TimePicker time = null;
	private int hourOfDay = 0 ;
	private int minute = 0;
	private Calendar calendar = Calendar.getInstance() ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.alarm);
		this.set = (Button) super.findViewById(R.id.set);
		this.delete = (Button) super.findViewById(R.id.delete);
		this.msg = (TextView) super.findViewById(R.id.msg);
		this.time = (TimePicker) super.findViewById(R.id.time);
		this.alarm = (AlarmManager) super.getSystemService(Context.ALARM_SERVICE) ;
		this.set.setOnClickListener(new SetOnClickListener()) ;
		this.delete.setOnClickListener(new DeleteOnClickListener()) ;
		this.time.setIs24HourView(true) ;
		this.time.setOnTimeChangedListener(new OnTimeChangedListenerImpl()) ;
	}
	private class OnTimeChangedListenerImpl implements OnTimeChangedListener{
		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		MyAlarmManagerDemo.this.calendar.setTimeInMillis(System.currentTimeMillis()) ;
		MyAlarmManagerDemo.this.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay) ;
		MyAlarmManagerDemo.this.calendar.set(Calendar.MINUTE, minute) ;
		MyAlarmManagerDemo.this.calendar.set(Calendar.SECOND, 0) ;
		MyAlarmManagerDemo.this.calendar.set(Calendar.MILLISECOND, 0) ;
		MyAlarmManagerDemo.this.hourOfDay = hourOfDay ;
		MyAlarmManagerDemo.this.minute = minute ;
		}
	}
	private class SetOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
		Intent intent = new Intent(MyAlarmManagerDemo.this,
		MyAlarmReceiver.class);
		intent.setAction("org.lxh.action.setalarm") ;
		PendingIntent sender = PendingIntent.getBroadcast(
		MyAlarmManagerDemo.this, 0, intent,
		PendingIntent.FLAG_UPDATE_CURRENT);
		MyAlarmManagerDemo.this.alarm.set(AlarmManager.RTC_WAKEUP,
		MyAlarmManagerDemo.this.calendar.getTimeInMillis(), sender);
		MyAlarmManagerDemo.this.msg.setText("���������ʱ���ǣ�"
				+ MyAlarmManagerDemo.this.hourOfDay + "ʱ"
				+ MyAlarmManagerDemo.this.minute + "�֡�");
				Toast.makeText(MyAlarmManagerDemo.this, "�������óɹ���",
				Toast.LENGTH_LONG).show();
				}
			}
			private class DeleteOnClickListener implements OnClickListener{
				@Override
				public void onClick(View v) {
				if (MyAlarmManagerDemo.this.alarm != null) {
				Intent intent = new Intent(MyAlarmManagerDemo.this,
				MyAlarmReceiver.class);
				intent.setAction("org.lxh.action.setalarm") ;
				PendingIntent sender = PendingIntent.getBroadcast(
				MyAlarmManagerDemo.this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
				MyAlarmManagerDemo.this.alarm.cancel(sender) ; // ȡ��
				MyAlarmManagerDemo.this.msg.setText("��ǰû���������ӡ�") ;
				Toast.makeText(MyAlarmManagerDemo.this, "����ɾ���ɹ���",
				Toast.LENGTH_LONG).show();
				}
			}
		}
	}
