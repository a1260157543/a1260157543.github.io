package com.hlb.chart;

import com.hlb.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/*********************************************************************************************
 * ChartTabActivity																			 *
 * ����: ����ͳ�Ʒ�������Activity���̳���TabActivity,�����LineChartActivity��BarChartActivity *
 ********************************************************************************************/

@SuppressWarnings("deprecation")
public class ChartTabActivity extends TabActivity implements
		TabHost.OnTabChangeListener {
	//����String����
	private static final String TAB_LINE = "Line_chart";
	private static final String TAB_BAR = "Bar_chart";
	private static final String LINE_TITLE = "��������";
	private static final String BAR_TITLE =  "����ͳ��";
	//TabHost����
	private TabHost mTabHost;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setupTabs();
		setContentView(mTabHost);
	}
	
	//����ChartTabActivity,��LineChartActivity��BarChartActivity��ӵ�TabHost����
	public void setupTabs() {
		//�õ�TabHost����
		this.mTabHost = getTabHost();
		//���ü�����
		this.mTabHost.setOnTabChangedListener(this);
		//���LineChartActivity����
		setupXYChartTab();
		//���BarChartActivity����
		setupBarChartTab();
	}

	//��LineChartActivity��ӵ�TabHost����
	private void setupXYChartTab() {
		Intent localIntent = new Intent(this, LineChartActivity.class);
		this.mTabHost.addTab(this.mTabHost.newTabSpec(TAB_LINE)
				.setIndicator(LINE_TITLE,getResources().getDrawable(R.drawable.xyline_icon)).setContent(localIntent));
	}
	//��BarChartActivity������ӵ�TabHost����
	private void setupBarChartTab() {
		Intent localIntent = new Intent(this, BarChartActivity.class);
		this.mTabHost.addTab(this.mTabHost.newTabSpec(TAB_BAR)
				.setIndicator(BAR_TITLE,getResources().getDrawable(R.drawable.bar_icon)).setContent(localIntent));
	}
	
	public void onTabChanged(String tabId) {
	}
}