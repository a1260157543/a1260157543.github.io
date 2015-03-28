package com.hlb.chart;

import java.util.Calendar;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
 * LineChartActivity																		 *
 * 功能: 将数据库中存储的对应月份所有心情记录得分绘成曲线图，向用户显示该月份的心情走势                              *
 *********************************************************************************************/

public class LineChartActivity extends Activity {
	//查找菜单弹出对话框的标题
	private static final String DIALOG_TITLE = "选择查找 年  月";
	// 标识创建当月心情轨迹
	private static final int DEFAULT_DATE = 0;
	// 标识创建用户指定月份的心情轨迹
	private static final int USER_DATE = 1;
	// 年、月的成员变量
	private int year = 0;
	private int month = 0;
	//保存当月心情曲线的View对象
	private View originLineChart = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		//设置Uri
		if (intent.getData() == null) {
			intent.setData(MoodColumns.CONTENT_URI);
		}
		// 得到当月心情曲线
		originLineChart = getNewlyChartView();
		// 设置布局
		setContentView(originLineChart);

	}

	/**
	 * 通过对数据库搜索结果集作图，得到当月的心情曲线
	 * 
	 * @return 当月心情曲线的View对象
	 */
	@SuppressWarnings("deprecation")
	public View getNewlyChartView() {
		View newlyChartView = null;
		// 得到当月零点时间戳
		long startStamp = getSearchStartStamp();
		// SQLWhere条件
		String dateFilter = MoodColumns.TIMESTAMP + " >= " + startStamp;
		// 搜索得到cursor
		Cursor cursor = managedQuery(getIntent().getData(),
				AppConstants.CHART_PROJECTION, dateFilter, null,
				MoodColumns._ID);
		// 尚没有心情记录
		if (cursor.getCount() == 0)
			Toast.makeText(this, AppConstants.Notifications.NO_RECORDS,
					Toast.LENGTH_LONG).show();
		// 由搜索结果得到当月心情曲线
		newlyChartView = getLineChartView(cursor, DEFAULT_DATE);
		//关闭搜索得到的cursor
		cursor.close();
		//返回心情曲线的View对象
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

		if (checkDate(year, monthOfYear)) {// 检查用户选择日期的有效性

			// 由用户选择的年、月得到搜索该月数据集需要的Where子句
			String dateFilter = getSearchRange(year, monthOfYear);

			Cursor cursor = managedQuery(getIntent().getData(),
					AppConstants.CHART_PROJECTION, dateFilter, null,
					MoodColumns._ID);

			// 输入的日期过早，没有心情记录，提示出错
			if (cursor.getCount() == 0)
				Toast.makeText(LineChartActivity.this,
						AppConstants.Notifications.DATEEARLY, Toast.LENGTH_LONG)
						.show();

			// 由搜索结果得到对应月份的心情曲线
			View lineChart = getLineChartView(cursor, USER_DATE);
			// 更新界面
			setContentView(lineChart);
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
		case R.id.chart_freshen: {// 刷新显示当前月份的心情轨迹
			setContentView(originLineChart);
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
			LineChartActivity.this.year = year;
			LineChartActivity.this.month = monthOfYear;
			updateCharView(year, monthOfYear);// 更新当前图表
		}
	}

	/**
	 * 检查用户选定时间的有效性，并提示
	 * @param year
	 * @param monthOfYear
	 * @return  true：有效    false: 无效
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
			Toast.makeText(LineChartActivity.this,
					AppConstants.Notifications.DATEPICKERWRONG,
					Toast.LENGTH_LONG).show();
			return false;
		} else
			return true;
	}

	/**
	 * 由搜索后的结果集得到对应月份的心情轨迹
	 * 
	 * @param cursor
	 * @param flag
	 *            0:当月 1:由用户选择的年、月
	 * @return 对应月份心情曲线的View对象
	 */
	public View getLineChartView(Cursor cursor, int flag) {
		int i = 0;
		float rating = (float) 0.0;
		double[] xLine = null;
		double[] yLine = null;
		String title = null;
		int xMax = 0;
		if (cursor.getCount() != 0) {
			// 当月心情记录的总条数
			int cursorLength = cursor.getCount();
			xMax = cursorLength - 1;
			// 得到横轴坐标
			xLine = new double[cursorLength];
			yLine = new double[cursorLength];
			double d = 0.0;
			for (i = 0; i < cursorLength; i++) {
				xLine[i] = d++;
			}
			i = 0;
			// 得到纵轴坐标,即每条心情分数
			while (cursor.moveToNext()) {
				rating = cursor.getFloat(cursor
						.getColumnIndex(MoodColumns.RATING));
				yLine[i++] = (double) rating;
			}
		} else {// 结果集为空
			xLine = new double[] { 0.0 };
			yLine = new double[] { 0.0 };
			xMax = 5;// 缺省状态下横轴最大值
		}
		// 设置曲线标题
		if (flag == DEFAULT_DATE)
			title = "当月心情轨迹";
		else
			title = year + "年" + (month + 1) + "月" + "心情轨迹";

		XYMultipleSeriesDataset dataset = buildDataset(title, xLine, yLine);

		int color[] = new int[] { Color.GREEN };
		PointStyle[] style = new PointStyle[] { PointStyle.DIAMOND };
		XYMultipleSeriesRenderer renderer = buildRenderer(color, style, true);

		setChartSettings(renderer, title, "心情数目", "心情指数 ", 0.0, xMax, 1.0, 6.0,
				Color.WHITE, Color.WHITE);

		View chart = ChartFactory.getLineChartView(this, dataset, renderer);

		return chart;
	}

	/**
	 * 构建二维曲线需要的数据集
	 * @param title 标题
	 * @param xValues 横坐标数据
	 * @param yValues 纵坐标数据
	 * @return  XYMultipleSeriesDataset对象
	 */
	protected XYMultipleSeriesDataset buildDataset(String title,
			double[] xValues, double[] yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		XYSeries series = new XYSeries(title); // 根据每条线的名称创建

		int seriesLength = xValues.length; // 待画曲线的点数

		for (int k = 0; k < seriesLength; k++) {
			series.add(xValues[k], yValues[k]);
		}

		dataset.addSeries(series);

		return dataset;

	}

	/**
	 * 创建曲线图形
	 */
	protected XYMultipleSeriesRenderer buildRenderer(int[] color,
			PointStyle[] style, boolean fill) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

		int length = color.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(color[i]);
			r.setPointStyle(style[i]);
			r.setFillPoints(fill);
			r.setFillBelowLine(true);
		    r.setFillBelowLineColor(Color.argb(100, 0, 255, 0));
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	/**
	 * 对图表的各项设置
	 */
	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
		renderer.setAxisTitleTextSize(27);// 设置坐标轴标题文本大小
		renderer.setChartTitleTextSize(32); // 设置图表标题文本大小
		renderer.setLabelsTextSize(27); // 设置轴标签文本大小
		renderer.setShowGrid(true);
		renderer.setXLabels(5);
		renderer.setYLabels(6);
		renderer.setMargins(new int[] { 25, 35, 25, 0 }); // 设置4边留白
		renderer.setApplyBackgroundColor(true);
	}

	/**
	 * 由DatePicker得到的年、月确定在SQlite中搜索的语句
	 * 
	 * @param year
	 * @param monthOfYear
	 * @return SQlite搜索的WHERE语句
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

	/**
	 * 得到当月第一天零点时的时间戳
	 * 
	 * @return 计算出的Unix时间戳
	 */
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
}
