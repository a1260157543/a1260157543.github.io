package com.hlb.chart;

import com.hlb.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/*********************************************************************************************
 * ChartTabActivity																			 *
 * 功能: 心情统计分析的主Activity，继承了TabActivity,并添加LineChartActivity和BarChartActivity *
 ********************************************************************************************/

@SuppressWarnings("deprecation")
public class ChartTabActivity extends TabActivity implements
		TabHost.OnTabChangeListener {
	//设置String常量
	private static final String TAB_LINE = "Line_chart";
	private static final String TAB_BAR = "Bar_chart";
	private static final String LINE_TITLE = "心情曲线";
	private static final String BAR_TITLE =  "心情统计";
	//TabHost对象
	private TabHost mTabHost;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setupTabs();
		setContentView(mTabHost);
	}
	
	//设置ChartTabActivity,将LineChartActivity和BarChartActivity添加到TabHost当中
	public void setupTabs() {
		//得到TabHost对象
		this.mTabHost = getTabHost();
		//设置监听器
		this.mTabHost.setOnTabChangedListener(this);
		//添加LineChartActivity对象
		setupXYChartTab();
		//添加BarChartActivity对象
		setupBarChartTab();
	}

	//将LineChartActivity添加到TabHost当中
	private void setupXYChartTab() {
		Intent localIntent = new Intent(this, LineChartActivity.class);
		this.mTabHost.addTab(this.mTabHost.newTabSpec(TAB_LINE)
				.setIndicator(LINE_TITLE,getResources().getDrawable(R.drawable.xyline_icon)).setContent(localIntent));
	}
	//将BarChartActivity对象添加到TabHost当中
	private void setupBarChartTab() {
		Intent localIntent = new Intent(this, BarChartActivity.class);
		this.mTabHost.addTab(this.mTabHost.newTabSpec(TAB_BAR)
				.setIndicator(BAR_TITLE,getResources().getDrawable(R.drawable.bar_icon)).setContent(localIntent));
	}
	
	public void onTabChanged(String tabId) {
	}
}