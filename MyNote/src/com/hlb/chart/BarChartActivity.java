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
 * ����: ͳ�����ݿ��ж�Ӧ�·ݵ����������¼�������ʾ�ٷֱȵ�����ͼ�����û���ʾ���·ݵ�����ֲ�          *
 *********************************************************************************************/

public class BarChartActivity extends Activity {
	//���Ҳ˵������Ի���ı���
	private static final String DIALOG_TITLE = "ѡ����� ��  ��";
	//����ȼ�ͳ�ƴ���
	private int ratingTime1 = 0;
	private int ratingTime2 = 0;
	private int ratingTime3 = 0;
	private int ratingTime4 = 0;
	private int ratingTime5 = 0;
	// �ꡢ�³�Ա����
	private int year = 0;
	private int month = 0;
	//���浱������ֲ�ͼ��View����
	private View originBarChart = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		//����Uri
		if (intent.getData() == null) {
			intent.setData(MoodColumns.CONTENT_URI);
		}
		//�õ���������ֲ�ͼ��View���󲢱��棬�����û���ѯ�����·ݺ��ˢ��
		originBarChart = getNewlyChartView();
		//����������ֲ�����ͼ��ʾ
		setContentView(originBarChart);
		//������ȼ�ͳ�ƴ�������
		reset();
	}

	/**
	 * ͨ�������ݿ������������ͼ���õ����µ�����ֲ�����ͼ
	 * 
	 * @return ��Ӧ��View����
	 */
	@SuppressWarnings("deprecation")
	public View getNewlyChartView() {
		View newlyChartView = null;
		// �õ��������ʱ���
		long startStamp = getSearchStartStamp();
        //SQLWhere����
		String dateFilter = MoodColumns.TIMESTAMP + " >= " + startStamp;
        //�����õ�cursor
		Cursor cursor = managedQuery(getIntent().getData(),
				AppConstants.CHART_PROJECTION, dateFilter, null,
				MoodColumns._ID);
        
		if (cursor.getCount() == 0)
			Toast.makeText(this, AppConstants.Notifications.NO_RECORDS,
					Toast.LENGTH_LONG).show();
        //������������и�������ȼ���ͳ��
		getRatingTimes(cursor);
        //�õ�����ͳ��ͼ��
		newlyChartView = getBarChartView(0);
		//�ر����������cursor
		cursor.close();
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

		if (checkDate(year, monthOfYear)) {

			String dateFilter = getSearchRange(year, monthOfYear);

			Cursor cursor = managedQuery(getIntent().getData(),
					AppConstants.CHART_PROJECTION, dateFilter, null,
					MoodColumns._ID);

			// ��������ڹ��磬û�������¼����ʾ����
			if (cursor.getCount() == 0)
				Toast.makeText(BarChartActivity.this,
						AppConstants.Notifications.DATEEARLY, Toast.LENGTH_LONG)
						.show();

			// �ɲ�ѯ�õ�cursor�õ�ÿ������ȼ���ͳ������
			getRatingTimes(cursor);
			// ��ͳ�����ݻ��Ƴ�����ͼ
			View BarChart = getBarChartView(1);
			// ���½���
			setContentView(BarChart);
			// ��ratingTime1~5���㣬�����´θ���ͼ��Ĳ���
			reset();
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
		case R.id.chart_freshen: {// ˢ�½��棬��ʾ����ĵ�������ͳ��ͼ
			setContentView(originBarChart);
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
			BarChartActivity.this.year = year;
			BarChartActivity.this.month = monthOfYear;
			updateCharView(year, monthOfYear);// ���µ�ǰͼ��
		}
	}

	/**
	 * ����û�ѡ��ʱ�����Ч�ԣ�����ʾ
	 * 
	 * @param year
	 * @param monthOfYear
	 * @return true:��Ч  false��Ч
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
	 * ������õ�����ͼ
	 * 
	 * @param flag
	 *            1:�����·ݲ�ѯ 0:��ʼ���²�ѯ
	 * @return ����ͼ����
	 */
	public GraphicalView getBarChartView(int flag) {
		// ͼ�����ݼ�
		XYMultipleSeriesDataset dataset = getBarDataset();
		// �õ�ͼ��View
		GraphicalView barChartView = ChartFactory.getBarChartView(this,
				dataset, getBarRenderer(flag), Type.DEFAULT);

		return barChartView;
	}

	public XYMultipleSeriesDataset getBarDataset() {

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		CategorySeries series = new CategorySeries("��������ͳ��%"); // ����һ������ͼ

		// Ϊ����ͼ���ֵ
		series.add(ratingTime1);
		series.add(ratingTime2);
		series.add(ratingTime3);
		series.add(ratingTime4);
		series.add(ratingTime5);

		dataset.addSeries(series.toXYSeries());// ��Ӹ�����ͼ�����������б�

		return dataset;

	}

	// ��״ͼ��Ⱦ��
	public XYMultipleSeriesRenderer getBarRenderer(int flag) {

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.GREEN);// ��ɫ��ɫ
		renderer.addSeriesRenderer(r);
		if (flag == 0)
			renderer.setChartTitle("��������ͳ��");// ���ñ���
		else
			renderer.setChartTitle(year + "��" + (month + 1) + "��" + "����ͳ��");// ���ñ���
		renderer.setXTitle("����ȼ�");// x�����
		renderer.setYTitle("�ٷֱ�");// y�����
		renderer.setAxisTitleTextSize(26);
		renderer.setChartTitleTextSize(32); // ����ͼ������ı���С
		renderer.setLabelsTextSize((float)26.0);
		renderer.setXAxisMin(0);// x����Сֵ
		renderer.setXAxisMax(5.6);
		renderer.setYAxisMin(0);// y����Сֵ
		renderer.setYAxisMax(100);
		renderer.setYLabels(5); // ���ú��ʵĿ̶�,��������ʾ�������� MAX / labels
		renderer.setDisplayChartValues(true);// �Ƿ���ͼ������ʾֵ
		renderer.setChartValuesTextSize(25);
		renderer.setShowGrid(true);// ��ʾ����
		renderer.setXLabels(0);
		//���ú�������ʾ����1�ֵ�5�֣���Ӧ��ʾTerrible��Perfect.
		renderer.addTextLabel(1, "Terrible");
		renderer.addTextLabel(2, "Bad");
		renderer.addTextLabel(3, "Fine");
		renderer.addTextLabel(4, "Good");
		renderer.addTextLabel(5, "Perfect");
		renderer.setBarSpacing(1.5);// ��������ͼ֮��ľ���
		// ����x,y����ʾ������,Ĭ���� Align.CENTER
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);
		// ����������,�����ɫ
		renderer.setAxesColor(Color.WHITE);
		// ����x,y���ϵĿ̶ȵ���ɫ
		// renderer.setLabelsColor(Color.BLACK);
		// ����ҳ�߿հ׵���ɫ
		renderer.setMarginsColor(Color.BLACK);
		// �����Ƿ���ʾ,���������,Ĭ��Ϊ true
		renderer.setShowAxes(true);
		renderer.setMargins(new int[] { 25, 35, 25, 0 }); // ����4������

		return renderer;
	}

	/**
	 * �ɲ��ҵõ���Cursor��������ͳ��ͼ������Ҫ������
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

	// �õ����µ�һ�����ʱ��Ӧ��ʱ���
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

	/**
	 *  ��DatePicker�õ����ꡢ��ȷ����SQlite�����������
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

	// ͳ����������
	public void reset() {
		ratingTime1 = 0;
		ratingTime2 = 0;
		ratingTime3 = 0;
		ratingTime4 = 0;
		ratingTime5 = 0;
	}

}
