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
 * ����: �����ݿ��д洢�Ķ�Ӧ�·����������¼�÷ֻ������ͼ�����û���ʾ���·ݵ���������                              *
 *********************************************************************************************/

public class LineChartActivity extends Activity {
	//���Ҳ˵������Ի���ı���
	private static final String DIALOG_TITLE = "ѡ����� ��  ��";
	// ��ʶ������������켣
	private static final int DEFAULT_DATE = 0;
	// ��ʶ�����û�ָ���·ݵ�����켣
	private static final int USER_DATE = 1;
	// �ꡢ�µĳ�Ա����
	private int year = 0;
	private int month = 0;
	//���浱���������ߵ�View����
	private View originLineChart = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		//����Uri
		if (intent.getData() == null) {
			intent.setData(MoodColumns.CONTENT_URI);
		}
		// �õ�������������
		originLineChart = getNewlyChartView();
		// ���ò���
		setContentView(originLineChart);

	}

	/**
	 * ͨ�������ݿ������������ͼ���õ����µ���������
	 * 
	 * @return �����������ߵ�View����
	 */
	@SuppressWarnings("deprecation")
	public View getNewlyChartView() {
		View newlyChartView = null;
		// �õ��������ʱ���
		long startStamp = getSearchStartStamp();
		// SQLWhere����
		String dateFilter = MoodColumns.TIMESTAMP + " >= " + startStamp;
		// �����õ�cursor
		Cursor cursor = managedQuery(getIntent().getData(),
				AppConstants.CHART_PROJECTION, dateFilter, null,
				MoodColumns._ID);
		// ��û�������¼
		if (cursor.getCount() == 0)
			Toast.makeText(this, AppConstants.Notifications.NO_RECORDS,
					Toast.LENGTH_LONG).show();
		// ����������õ�������������
		newlyChartView = getLineChartView(cursor, DEFAULT_DATE);
		//�ر������õ���cursor
		cursor.close();
		//�����������ߵ�View����
		return newlyChartView;
	}

	/**
	 * ���û�ѡ�������²��ҵ�ʱ���������ݲ���ͳ�ƣ�����ͼ��
	 * 
	 * @param year
	 * @param monthOfYear
	 */
	@SuppressWarnings("deprecation")
	public void updateCharView(int year, int monthOfYear) {

		if (checkDate(year, monthOfYear)) {// ����û�ѡ�����ڵ���Ч��

			// ���û�ѡ����ꡢ�µõ������������ݼ���Ҫ��Where�Ӿ�
			String dateFilter = getSearchRange(year, monthOfYear);

			Cursor cursor = managedQuery(getIntent().getData(),
					AppConstants.CHART_PROJECTION, dateFilter, null,
					MoodColumns._ID);

			// ��������ڹ��磬û�������¼����ʾ����
			if (cursor.getCount() == 0)
				Toast.makeText(LineChartActivity.this,
						AppConstants.Notifications.DATEEARLY, Toast.LENGTH_LONG)
						.show();

			// ����������õ���Ӧ�·ݵ���������
			View lineChart = getLineChartView(cursor, USER_DATE);
			// ���½���
			setContentView(lineChart);
		}
	}

	/**
	 * ����Menu�˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// ���ò˵�
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.moodchart_view_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * ���ò˵���ϸ����
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.chart_find: {// ѡ��鿴���ꡢ��
			showDatePicker();
			return true;
		}
		case R.id.chart_freshen: {// ˢ����ʾ��ǰ�·ݵ�����켣
			setContentView(originLineChart);
			return true;
		}
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}
	/**
	 * ��ʾDatePicker�Ի���
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
	 * ����DatePickerDialog�ļ�����
	 */
	class DatePickerListener implements OnDateSetListener {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			LineChartActivity.this.year = year;
			LineChartActivity.this.month = monthOfYear;
			updateCharView(year, monthOfYear);// ���µ�ǰͼ��
		}
	}

	/**
	 * ����û�ѡ��ʱ�����Ч�ԣ�����ʾ
	 * @param year
	 * @param monthOfYear
	 * @return  true����Ч    false: ��Ч
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
	 * ��������Ľ�����õ���Ӧ�·ݵ�����켣
	 * 
	 * @param cursor
	 * @param flag
	 *            0:���� 1:���û�ѡ����ꡢ��
	 * @return ��Ӧ�·��������ߵ�View����
	 */
	public View getLineChartView(Cursor cursor, int flag) {
		int i = 0;
		float rating = (float) 0.0;
		double[] xLine = null;
		double[] yLine = null;
		String title = null;
		int xMax = 0;
		if (cursor.getCount() != 0) {
			// ���������¼��������
			int cursorLength = cursor.getCount();
			xMax = cursorLength - 1;
			// �õ���������
			xLine = new double[cursorLength];
			yLine = new double[cursorLength];
			double d = 0.0;
			for (i = 0; i < cursorLength; i++) {
				xLine[i] = d++;
			}
			i = 0;
			// �õ���������,��ÿ���������
			while (cursor.moveToNext()) {
				rating = cursor.getFloat(cursor
						.getColumnIndex(MoodColumns.RATING));
				yLine[i++] = (double) rating;
			}
		} else {// �����Ϊ��
			xLine = new double[] { 0.0 };
			yLine = new double[] { 0.0 };
			xMax = 5;// ȱʡ״̬�º������ֵ
		}
		// �������߱���
		if (flag == DEFAULT_DATE)
			title = "��������켣";
		else
			title = year + "��" + (month + 1) + "��" + "����켣";

		XYMultipleSeriesDataset dataset = buildDataset(title, xLine, yLine);

		int color[] = new int[] { Color.GREEN };
		PointStyle[] style = new PointStyle[] { PointStyle.DIAMOND };
		XYMultipleSeriesRenderer renderer = buildRenderer(color, style, true);

		setChartSettings(renderer, title, "������Ŀ", "����ָ�� ", 0.0, xMax, 1.0, 6.0,
				Color.WHITE, Color.WHITE);

		View chart = ChartFactory.getLineChartView(this, dataset, renderer);

		return chart;
	}

	/**
	 * ������ά������Ҫ�����ݼ�
	 * @param title ����
	 * @param xValues ����������
	 * @param yValues ����������
	 * @return  XYMultipleSeriesDataset����
	 */
	protected XYMultipleSeriesDataset buildDataset(String title,
			double[] xValues, double[] yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		XYSeries series = new XYSeries(title); // ����ÿ���ߵ����ƴ���

		int seriesLength = xValues.length; // �������ߵĵ���

		for (int k = 0; k < seriesLength; k++) {
			series.add(xValues[k], yValues[k]);
		}

		dataset.addSeries(series);

		return dataset;

	}

	/**
	 * ��������ͼ��
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
	 * ��ͼ��ĸ�������
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
		renderer.setAxisTitleTextSize(27);// ��������������ı���С
		renderer.setChartTitleTextSize(32); // ����ͼ������ı���С
		renderer.setLabelsTextSize(27); // �������ǩ�ı���С
		renderer.setShowGrid(true);
		renderer.setXLabels(5);
		renderer.setYLabels(6);
		renderer.setMargins(new int[] { 25, 35, 25, 0 }); // ����4������
		renderer.setApplyBackgroundColor(true);
	}

	/**
	 * ��DatePicker�õ����ꡢ��ȷ����SQlite�����������
	 * 
	 * @param year
	 * @param monthOfYear
	 * @return SQlite������WHERE���
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
	 * �õ����µ�һ�����ʱ��ʱ���
	 * 
	 * @return �������Unixʱ���
	 */
	public long getSearchStartStamp() {
		long startStamp = 0;
		int year = 0, month = 0, day = 0;
		Calendar c = Calendar.getInstance();
		try {
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = 1;// ��ÿ���µĵ�һ�쿪ʼͳ��
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.set(year, month, day, 0, 0);
		startStamp = c.getTimeInMillis();
		return startStamp;
	}
}
