package com.hlb.chart;

import java.util.Calendar;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.hlb.app.AppConstants;
import com.hlb.app.AppConstants.MoodColumns;
import com.hlb.R;

/*********************************************************************************************
 * BarChartActivity																		     *
 * 功能: 统计数据库中对应月份的所有心情记录，绘成显示百分比的柱形图，向用户显示该月份的心情分布          *
 *********************************************************************************************/

public class BarChartActivity extends Activity {
	//查找菜单弹出对话框的标题
	private static final String DIALOG_TITLE = "选择查找 年  月";
	//心情等级统计次数
	private int ratingTime1 = 0;
	private int ratingTime2 = 0;
	private int ratingTime3 = 0;
	private int ratingTime4 = 0;
	private int ratingTime5 = 0;
	// 年、月成员变量
	private int year = 0;
	private int month = 0;
	//保存当月心情分布图的View对象
	private View originBarChart = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		//设置Uri
		if (intent.getData() == null) {
			intent.setData(MoodColumns.CONTENT_URI);
		}
		//得到当月心情分布图的View对象并保存，用于用户查询其他月份后的刷新
		originBarChart = getNewlyChartView();
		//将当月心情分布柱形图显示
		setContentView(originBarChart);
		//将心情等级统计次数清零
		reset();
	}

	/**
	 * 通过对数据库搜索结果集作图，得到当月的心情分布柱形图
	 * 
	 * @return 对应的View对象
	 */
	@SuppressWarnings("deprecation")
	public View getNewlyChartView() {
		View newlyChartView = null;
		// 得到当月零点时间戳
		long startStamp = getSearchStartStamp();
        //SQLWhere条件
		String dateFilter = MoodColumns.TIMESTAMP + " >= " + startStamp;
        //搜索得到cursor
		Cursor cursor = managedQuery(getIntent().getData(),
				AppConstants.CHART_PROJECTION, dateFilter, null,
				MoodColumns._ID);
        
		if (cursor.getCount() == 0)
			Toast.makeText(this, AppConstants.Notifications.NO_RECORDS,
					Toast.LENGTH_LONG).show();
        //由搜索结果进行各个心情等级的统计
		getRatingTimes(cursor);
        //得到当月统计图表
		newlyChartView = getBarChartView(0);
		//关闭搜索结果集cursor
		cursor.close();
		return newlyChartView;
	}

	/**
	 * 由用户选定的年月查找当时的心情数据并作统计，更新图表
	 * 
	 * @param year
	 * @param monthOfYear
	 */
	@SuppressWarnings("deprecation")
	public void updateCharView(int year, int monthOfYear) {

		if (checkDate(year, monthOfYear)) {

			String dateFilter = getSearchRange(year, monthOfYear);

			Cursor cursor = managedQuery(getIntent().getData(),
					AppConstants.CHART_PROJECTION, dateFilter, null,
					MoodColumns._ID);

			// 输入的日期过早，没有心情记录，提示出错
			if (cursor.getCount() == 0)
				Toast.makeText(BarChartActivity.this,
						AppConstants.Notifications.DATEEARLY, Toast.LENGTH_LONG)
						.show();

			// 由查询得到cursor得到每个心情等级的统计数据
			getRatingTimes(cursor);
			// 由统计数据绘制成柱形图
			View BarChart = getBarChartView(1);
			// 更新界面
			setContentView(BarChart);
			// 将ratingTime1~5清零，用于下次更新图表的操作
			reset();
		}
	}

	/**
	 * 创建Menu菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 设置菜单
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.moodchart_view_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 设置菜单详细功能
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.chart_find: {// 选择查看的年、月
			showDatePicker();
			return true;
		}
		case R.id.chart_freshen: {// 刷新界面，显示保存的当月心情统计图
			setContentView(originBarChart);
			return true;
		}
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	/**
	 * 显示DatePicker对话框
	 */
	public void showDatePicker() {
		final Calendar cal = Calendar.getInstance();
		DatePickerDialog mDialog = new DatePickerDialog(this,
				new DatePickerListener(), cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		mDialog.setTitle(DIALOG_TITLE);
		mDialog.show();
	}

	/**
	 * 设置DatePickerDialog的监听器
	 */
	class DatePickerListener implements OnDateSetListener {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			BarChartActivity.this.year = year;
			BarChartActivity.this.month = monthOfYear;
			updateCharView(year, monthOfYear);// 更新当前图表
		}
	}

	/**
	 * 检查用户选定时间的有效性，并提示
	 * 
	 * @param year
	 * @param monthOfYear
	 * @return true:有效  false无效
	 */
	public boolean checkDate(int year, int monthOfYear) {
		int currentYear = 0, currentMonth = 0;
		Calendar c = Calendar.getInstance();
		try {
			currentYear = c.get(Calendar.YEAR);
			currentMonth = c.get(Calendar.MONTH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (year > currentYear
				|| ((year == currentYear) && (monthOfYear > currentMonth))) {
			Toast.makeText(BarChartActivity.this,
					AppConstants.Notifications.DATEPICKERWRONG,
					Toast.LENGTH_LONG).show();
			return false;
		} else
			return true;
	}

	/**
	 * 搜索后得到柱形图
	 * 
	 * @param flag
	 *            1:过往月份查询 0:初始当月查询
	 * @return 柱形图对象
	 */
	public GraphicalView getBarChartView(int flag) {
		// 图表数据集
		XYMultipleSeriesDataset dataset = getBarDataset();
		// 得到图表View
		GraphicalView barChartView = ChartFactory.getBarChartView(this,
				dataset, getBarRenderer(flag), Type.DEFAULT);

		return barChartView;
	}

	public XYMultipleSeriesDataset getBarDataset() {

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		CategorySeries series = new CategorySeries("当月心情统计%"); // 声明一个柱形图

		// 为柱形图添加值
		series.add(ratingTime1);
		series.add(ratingTime2);
		series.add(ratingTime3);
		series.add(ratingTime4);
		series.add(ratingTime5);

		dataset.addSeries(series.toXYSeries());// 添加该柱形图到数据设置列表

		return dataset;

	}

	// 柱状图渲染器
	public XYMultipleSeriesRenderer getBarRenderer(int flag) {

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.GREEN);// 颜色红色
		renderer.addSeriesRenderer(r);
		if (flag == 0)
			renderer.setChartTitle("当月心情统计");// 设置标题
		else
			renderer.setChartTitle(year + "年" + (month + 1) + "月" + "心情统计");// 设置标题
		renderer.setXTitle("心情等级");// x轴标题
		renderer.setYTitle("百分比");// y轴标题
		renderer.setAxisTitleTextSize(26);
		renderer.setChartTitleTextSize(32); // 设置图表标题文本大小
		renderer.setLabelsTextSize((float)26.0);
		renderer.setXAxisMin(0);// x轴最小值
		renderer.setXAxisMax(5.6);
		renderer.setYAxisMin(0);// y轴最小值
		renderer.setYAxisMax(100);
		renderer.setYLabels(5); // 设置合适的刻度,在轴上显示的数量是 MAX / labels
		renderer.setDisplayChartValues(true);// 是否在图上中显示值
		renderer.setChartValuesTextSize(25);
		renderer.setShowGrid(true);// 显示网格
		renderer.setXLabels(0);
		//设置横坐标显示，由1分到5分，对应显示Terrible到Perfect.
		renderer.addTextLabel(1, "Terrible");
		renderer.addTextLabel(2, "Bad");
		renderer.addTextLabel(3, "Fine");
		renderer.addTextLabel(4, "Good");
		renderer.addTextLabel(5, "Perfect");
		renderer.setBarSpacing(1.5);// 设置条形图之间的距离
		// 设置x,y轴显示的排列,默认是 Align.CENTER
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);
		// 设置坐标轴,轴的颜色
		renderer.setAxesColor(Color.WHITE);
		// 设置x,y轴上的刻度的颜色
		// renderer.setLabelsColor(Color.BLACK);
		// 设置页边空白的颜色
		renderer.setMarginsColor(Color.BLACK);
		// 设置是否显示,坐标轴的轴,默认为 true
		renderer.setShowAxes(true);
		renderer.setMargins(new int[] { 25, 35, 25, 0 }); // 设置4边留白

		return renderer;
	}

	/**
	 * 由查找得到的Cursor更新生成统计图表所需要的数据
	 * 
	 * @param cursor
	 */
	public void getRatingTimes(Cursor cursor) {
		int rating = 0;
		int counts = cursor.getCount();
		while (cursor.moveToNext()) {
			rating = (int) cursor.getFloat(cursor
					.getColumnIndex(MoodColumns.RATING));
			switch (rating) {
			case 1: {
				ratingTime1++;
				break;
			}
			case 2: {
				ratingTime2++;
				break;
			}
			case 3: {
				ratingTime3++;
				break;
			}
			case 4: {
				ratingTime4++;
				break;
			}
			case 5: {
				ratingTime5++;
				break;
			}
			default:
			}
		}
		if (counts != 0) {
			ratingTime1 = alignFactor(ratingTime1, counts);
			ratingTime2 = alignFactor(ratingTime2, counts);
			ratingTime3 = alignFactor(ratingTime3, counts);
			ratingTime4 = alignFactor(ratingTime4, counts);
			ratingTime5 = alignFactor(ratingTime5, counts);
		}
	}

	// 得到当月第一天零点时对应的时间戳
	public long getSearchStartStamp() {
		long startStamp = 0;
		int year = 0, month = 0, day = 0;
		Calendar c = Calendar.getInstance();
		try {
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = 1;// 从每个月的第一天开始统计
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.set(year, month, day, 0, 0);
		startStamp = c.getTimeInMillis();
		return startStamp;
	}

	/**
	 *  由DatePicker得到的年、月确定在SQlite中搜索的语句
	 * @param year
	 * @param monthOfYear
	 * @return
	 */
	public String getSearchRange(int year, int monthOfYear) {
		long startStamp = 0, endStamp = 0;
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, 1, 0, 0);
		startStamp = c.getTimeInMillis();
		if (monthOfYear != 11) {
			c.set(year, monthOfYear + 1, 1, 0, 0);
			endStamp = c.getTimeInMillis();
		} else {
			c.set(year + 1, 0, 1, 0, 0);
			endStamp = c.getTimeInMillis();
		}
		String dateFilter = MoodColumns.TIMESTAMP + " >= " + startStamp
				+ " and " + MoodColumns.TIMESTAMP + " < " + endStamp;
		return dateFilter;
	}

	public int alignFactor(int ratingTime, int counts) {
		int result = 0;
		int integer = 0;
		double intermedium = 0.0;
		intermedium = (double) ratingTime / (double) counts * 100;
		integer = (int) intermedium;
		if (intermedium > ((double) integer + 0.4))
			result = integer + 1;
		else
			result = integer;
		return result;
	}

	// 统计数据清零
	public void reset() {
		ratingTime1 = 0;
		ratingTime2 = 0;
		ratingTime3 = 0;
		ratingTime4 = 0;
		ratingTime5 = 0;
	}

}
