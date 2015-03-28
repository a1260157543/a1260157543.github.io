package com.hlb.activity;

import java.lang.ref.WeakReference;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

import com.hlb.app.AppConstants;
import com.hlb.app.AppConstants.MoodColumns;
import com.hlb.app.AppConstants.Notifications;
import com.hlb.chart.ChartTabActivity;
import com.hlb.fragment.LeftFragment;
import com.hlb.slidingmenu.BaseSlidingFragmentActivity;
import com.hlb.slidingmenu.SlidingMenu;
import com.hlb.utils.MoodPW;
import com.hlb.utils.Utils;
import com.hlb.R;
public class MoodLogActivity extends BaseSlidingFragmentActivity {
	/** Called when the activity is first created. */

	// �������������ʾ������
	private static final long DAYSDIF = 3;
	// ListView����ʾ����������
	private static int NUM_LIMITS = 20;
	// ������������Dialog�ؼ�����ʱ��ID
	private static final int SEARCH_DIALOG_ID = 0;
	private static final int EXIT_DIALOG_ID = 1;
	private static final int PW_INITIAL_ID = 2;
	private static final int PW_MODIFIED_ID = 3;
	private static final int PW_LOGIN_ID = 4;
	private static final int PW_CANCEL_ID = 5;
	// SharedPreferences���ڴ洢�û����õ�����
	private SharedPreferences mPreferences = null;
	// ������������ʹ�õĿؼ�
	private ListView noteListView = null;
	private ImageButton addMoodButton = null;
	private TextView titleTextView = null;
	private TextView queryButton = null;
	private TextView refreshButton = null;
	private MoodCursorAdapter adapter = null;
	private ImageButton chartButton = null;
	// ��־����
	private int pw_error_counts = 0;// ���ڼ�¼���������������Ĵ���
	private int pw_flag = 0; // 1:���������� 0:����������
	// DAYSDIF��ʱ���㵽����
	private static final long MILLDIFF;
	static {
		MILLDIFF = DAYSDIF * 24 * 60 * 60 * 1000;
	}

	private SlidingMenu mSlidingMenu = null;
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ò����ļ�
		setContentView(R.layout.main);
		// �ؼ���ʼ��
		setView();
		// �õ����ڴ洢�����SharedPreferences����
		mPreferences = getSharedPreferences(AppConstants.String_Const.LOG_IN,
				MODE_PRIVATE);

		// ����Ƿ�����������
		if (mPreferences.getString(AppConstants.String_Const.PASSWORD, "0")
				.equals("0")) {// û����������
		} else {// ����������
			// ������������Ի���
			showDialog(PW_LOGIN_ID);
			pw_flag = 1;// �������־��Ϊ1����ʾ����������
		}

		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);

		// �õ�provider����û�У�����MoodColumns.CONTENT_URI������Ҫ���ʵ�ContentProvider
		Intent intent = getIntent();
		if (intent.getData() == null) {
			intent.setData(MoodColumns.CONTENT_URI);
		}
		// ע��һ��2s������˵�
		noteListView.setOnCreateContextMenuListener(this);
	}

	/* ��ʼ���ؼ� */
	public void setView() {
		noteListView = (ListView) findViewById(R.id.mood_note_list);
		noteListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				Uri moodUri = ContentUris.withAppendedId(getIntent().getData(),
						id);
				// ����Intent������Uri��Intent.ACTION_VIEW��ϵͳָ������MoodItemView
				// Activity
				Intent intent = new Intent(Intent.ACTION_VIEW, moodUri);
				startActivity(intent);
			}
		});

		titleTextView = (TextView) findViewById(R.id.ivTitleName);
		titleTextView.setText("�����б�");

		addMoodButton = (ImageButton) findViewById(R.id.addMoodButton);
		addMoodButton.setOnClickListener(new addMoodOnClickListener());
		addMoodButton.getBackground().setAlpha(0);

		refreshButton = (TextView) findViewById(R.id.ivTitleBtnLeft);
		refreshButton.setText("ˢ��");
		refreshButton
				.setBackgroundResource(R.drawable.top_button_right_selector);
		refreshButton.setVisibility(View.VISIBLE);
		refreshButton.setOnClickListener(new RefreshButtonListener());

		queryButton = (TextView) findViewById(R.id.ivTitleBtnRight);
		queryButton.setText("��ѯ");
		queryButton.setVisibility(View.VISIBLE);
		queryButton.setOnClickListener(new QueryButtonListener());

		chartButton = (ImageButton) findViewById(R.id.mood_tendency);
		chartButton.getBackground().setAlpha(0);
		// �����������ư�ť�ļ��������û�����󣬽�������ͳ�Ʒ�������ChartTabActivity
		chartButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MoodLogActivity.this, ChartTabActivity.class);
				startActivity(intent);
			}
		});
		initSlidingMenu();
	}

	private void initSlidingMenu() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;// ��ȡ��Ļ�ֱ��ʿ��
		// TODO Auto-generated method stub
		setBehindContentView(R.layout.main_left_layout);
		FragmentTransaction mFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment mFrag = new LeftFragment();
		mFragementTransaction.replace(R.id.main_left_fragment, mFrag);
		mFragementTransaction.commit();
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.LEFT);// �������󻬻����һ����������Ҷ����Ի������������Ҷ����Ի�
		mSlidingMenu.setShadowWidth(mScreenWidth / 40);// ������Ӱ���
		mSlidingMenu.setBehindOffset(mScreenWidth / 8);// ���ò˵����
		mSlidingMenu.setFadeDegree(0.35f);// ���õ��뵭���ı���
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);// ������˵���ӰͼƬ
		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.right_shadow);// �����Ҳ˵���ӰͼƬ
		mSlidingMenu.setFadeEnabled(true);// ���û���ʱ�˵����Ƿ��뵭��
		mSlidingMenu.setBehindScrollScale(0.333f);// ���û���ʱ��קЧ��
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (pw_flag == 0) // û����������
			// ��û���������������£���������ֱ����ʾ�����ʮ�������¼
			setCursorView();
	}

	/**
	 * ����Menu�˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// ���ò˵�
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.moodlog_option_munu, menu);
		// return super.onCreateOptionsMenu(menu);
		return false;
	}

	/**
	 * ����˵�ѡ��ʱ�Ĳ���
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		/* ����/�޸����빦�� */
		case R.id.menu_setting: {
			// �û���û����������
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY))
				showDialog(PW_INITIAL_ID);// ������������Ի���
			// �����Ѿ���������
			else {
				showDialog(PW_MODIFIED_ID); // �����޸�����Ի���
			}
			return true;
		}
		/* ȡ�����뱣������ */
		case R.id.menu_cancel_pw: {
			// �û���û����������
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY)) {
				// ��ʾ�û�����û�������뱣������
				Toast.makeText(MoodLogActivity.this,
						AppConstants.Notifications.PW_WITHOUT,
						Toast.LENGTH_SHORT).show();
			} else {// �û��Ѿ���������
				showDialog(PW_CANCEL_ID);// ����ȡ������Ի���
			}
			return true;
		}
		/* ˢ��MoodLogActivity��ʾ���µ�ʮ�������¼ */
		case R.id.menu_refreshen: {
			// �����ݿ��в�ѯ���µ������¼�����ؽ��������ˢ������������ʾ������
			Cursor freshenCursor = managedQuery(getIntent().getData(),
					AppConstants.PROJECTION, null, null,
					MoodColumns.DEFAULT_SORT_ORDER + MoodColumns.DEFAULT_LIMIT);
			MoodLogActivity.this.adapter.changeCursor(freshenCursor);
			return true;
		}
		default:
			return super.onMenuItemSelected(featureId, item);
		}
	}

	/**
	 * ���������Ĳ˵�
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {
			e.printStackTrace();
			return;
		}

		// ����ȷ���Ѿ�ѡ������־�б��е�һ����־����ûѡ����ֱ�ӷ��ء�Cursorָ��ѡ�е���־��
		Cursor cursor = (Cursor) noteListView.getAdapter().getItem(
				info.position);
		if (cursor == null) {
			return;
		}
		// �Ӳ����ļ��еõ�MenuInflater����
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.moodlog_context_menu, menu);

		// ����Context menu ����
		menu.setHeaderTitle(AppConstants.String_Const.CONTEXT_MENU_HEADER);
	}

	/**
	 * ��������Ļ��ѡ��һ��ѡ��2S֮��Ĳ���
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		} catch (ClassCastException e) {
			e.printStackTrace();
			return false;
		}
		// ƴ��ѡ�����Uri
		Uri moodUri = ContentUris
				.withAppendedId(getIntent().getData(), info.id);

		switch (item.getItemId()) {
		// ������Ĳ鿴������MoodItemView Activity
		case R.id.context_view:
			startActivity(new Intent(Intent.ACTION_VIEW, moodUri));
			return true;
			// ɾ��ѡ�е������¼
		case R.id.context_delete:
			getContentResolver().delete(moodUri, null, null);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	/**
	 * �������������а���KEYCODE_BACKʱ�Ĳ������������˳�����ȷ�϶Ի���
	 */
	@SuppressWarnings("deprecation")
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// �����˳�����ȷ�϶Ի���
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(EXIT_DIALOG_ID);
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ���жԻ�����Ҫ����ʱ�����ø÷���������showDialog()�д����ID������Ӧ�ĶԻ���
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		if (id == SEARCH_DIALOG_ID) { // �������������Ի���
			dialog = buildSearchDialog();
			return dialog;
		} else if (id == EXIT_DIALOG_ID) { // �����˳�ȷ�϶Ի���
			dialog = buildConfirmExitDialog();
			return dialog;
		} else if (id == PW_INITIAL_ID) { // ����������������Ի���
			dialog = buildInitialPasswordDialog();
			return dialog;
		} else if (id == PW_MODIFIED_ID) { // ���������޸�����Ի���
			dialog = buildChangePasswordDialog();
			return dialog;
		} else if (id == PW_LOGIN_ID) { // ������������Ի���
			dialog = buildInputPasswordDialog();
			return dialog;
		} else if (id == PW_CANCEL_ID) { // ����ȡ�����뱣���Ի���
			dialog = buildCancelPasswordDialog();
			return dialog;
		} else
			return super.onCreateDialog(id);
	}

	/**
	 * �����˳�����ʱ�����Ի���
	 * 
	 * @return ���ش����ĶԻ������
	 */
	public Dialog buildConfirmExitDialog() {
		// ��ʼ�趨AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle(R.string.exit_dialog);
		// �趨PositiveButton����ʱ�Ĳ���
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// �˳����򣬹ر�MoodLogActivity
						MoodLogActivity.this.finish();
					}
				});
		// �趨NegativeButton����ʱ�Ĳ���
		builder.setNegativeButton(R.string.cancel, null);
		// ����AlertDialog
		AlertDialog exitAlertDialog = builder.create();
		return exitAlertDialog;
	}

	/* ********************************************************************************
	 * ���ö��������������Ĺ��ܣ�����ͨ��ѡ�����ڼ����ݹ��������������AlertDialog����ɡ� *
	 * ������DatePickerʵ��������ʼ����ֹ���ڵ�ѡ�����ɹؼ��ֵ�������ͬ����������� *
	 * �����ɹ����ɽ������MoodLogAcitity��ListView����ʾ������� *
	 * **************************************************
	 */

	/**
	 * ����ʱ�Ի�����CheckBox�Ƿ񼤻�ļ�����
	 * 
	 */
	class DateChangeListener implements CompoundButton.OnCheckedChangeListener {

		private View localView = null;

		public DateChangeListener(View v) {
			localView = v;
		}

		// ÿ�ε��CheckBox��״̬�ı�
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// 0 : VISIBLE 8: GONE;
			if (localView.getVisibility() == 0) {
				localView.setVisibility(8);
				return;
			} else {
				localView.setVisibility(0);
				return;
			}
		}
	}

	/**
	 * ����ʱAlertDialogȷ�ϼ��ļ�����
	 */
	class PositiveDialogListener implements DialogInterface.OnClickListener {
		private View localView = null;
		private DatePicker startDatePicker = null;
		private DatePicker endDatePicker = null;
		private EditText localEditText = null;
		private CheckBox localCheckBox = null;

		public PositiveDialogListener(View v, DatePicker start, DatePicker end,
				EditText editText, CheckBox checkBox) {
			localView = v;
			startDatePicker = start;
			endDatePicker = end;
			localEditText = editText;
			localCheckBox = checkBox;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			int flag = 0;// �����ж�DatePicker����ı�־
			long startStamp = 0;
			long endStamp = 0;
			String dateFilter = null;
			String contentFilter = null;
			String queryFilter = null;

			// DatePicker��ʾ����������ѡ��
			if (localView.getVisibility() == 0) {
				// �õ���DatePicker�����õ�������ʼ����
				int startYear = startDatePicker.getYear();
				int startMonth = startDatePicker.getMonth();
				int startDay = startDatePicker.getDayOfMonth();
				int endYear = endDatePicker.getYear();
				int endMonth = endDatePicker.getMonth();
				int endDay = endDatePicker.getDayOfMonth();
				// ���õ�������ת����ʱ��������������ݿ�������
				Calendar localCalendar = Calendar.getInstance();
				localCalendar.set(startYear, startMonth, startDay, 0, 0);
				startStamp = localCalendar.getTimeInMillis();
				localCalendar.set(endYear, endMonth, endDay, 23, 59);
				endStamp = localCalendar.getTimeInMillis();
				// ��ʼ���ڴ��ڽ������ڣ���ʾ����
				if (startStamp > endStamp) {
					flag = 1;// DatePicker������ѡ�������־λ��Ϊ1
				} else {
					flag = 0;
					// ȷ�������ݿ�������ʱ����������
					// ʱ�����������
					dateFilter = MoodColumns.TIMESTAMP + " >= " + startStamp
							+ " and " + MoodColumns.TIMESTAMP + " <= "
							+ endStamp;
				}
			}

			// �õ����ݹ�������������ַ���
			String content = localEditText.getText().toString();
			if (!TextUtils.isEmpty(content))
				// ���ݹ�������������
				contentFilter = MoodColumns.MOODITEM + " like \"%" + content
						+ "%\"";
			if ((!TextUtils.isEmpty(dateFilter))
					&& (!TextUtils.isEmpty(contentFilter)))
				queryFilter = dateFilter + " and " + contentFilter;
			else if (!TextUtils.isEmpty(dateFilter)
					&& TextUtils.isEmpty(contentFilter))
				queryFilter = dateFilter;
			else if (!TextUtils.isEmpty(contentFilter)
					&& TextUtils.isEmpty(dateFilter))
				queryFilter = contentFilter;
			// DatePicker������ѡ�������ʾ������Ϣ
			if (flag == 1) {
				Toast.makeText(MoodLogActivity.this,
						Notifications.DATEPICKERERROR, Toast.LENGTH_LONG)
						.show();
			} else if (!TextUtils.isEmpty(queryFilter)) {// �����û�ѡ��������ʵ�ֲ�ѯ����
				Cursor queryCursor = MoodLogActivity.this.getContentResolver()
						.query(getIntent().getData(), AppConstants.PROJECTION,
								queryFilter, null,
								MoodColumns.DEFAULT_SORT_ORDER);
				MoodLogActivity.this.adapter.changeCursor(queryCursor);
				localCheckBox.setChecked(false);
				localEditText.setText("");
				dialog.dismiss();
				queryCursor.close();
			} else if (flag == 0 && TextUtils.isEmpty(queryFilter)) {// �����κβ�������NegativeButton������ͬ
				localCheckBox.setChecked(false);
				localEditText.setText("");
				dialog.dismiss();
			}
		}
	}

	// �����Ի�����ȡ����ť�ļ�����
	class NegativeDialogListener implements DialogInterface.OnClickListener {
		private CheckBox localCheckBox = null;
		private EditText localEditText = null;

		public NegativeDialogListener(CheckBox checkBox, EditText editText) {
			localCheckBox = checkBox;
			localEditText = editText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// �ر�CheckBox
			localCheckBox.setChecked(false);
			localEditText.setText("");
			// �رնԻ���
			dialog.dismiss();
		}
	}

	// ȡ�����ضԻ���ļ�����
	class CancelDialogListener implements DialogInterface.OnCancelListener {
		private CheckBox localCheckBox = null;
		private EditText localEditText = null;

		public CancelDialogListener(CheckBox checkBox, EditText editText) {
			localCheckBox = checkBox;
			localEditText = editText;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// �ر�CheckBox
			localCheckBox.setChecked(false);
			localEditText.setText("");
			// �رնԻ���
			dialog.dismiss();
		}
	}

	/**
	 * ���û�����������ťʱ�����������Ի��򣬿�ѡ������ʱ�䷶Χ��ͨ������ؼ�������
	 * 
	 * @return �����������Ի���
	 */
	public Dialog buildSearchDialog() {
		// ��dlg_search����ʵ���������ҵ����еĿؼ�
		View localView1 = LayoutInflater.from(this).inflate(
				R.layout.dlg_search, null);
		CheckBox localCheckBox = (CheckBox) localView1
				.findViewById(R.id.datePicker_checkBox);
		View localView2 = localView1.findViewById(R.id.datePicker_layout);
		DatePicker startDatePicker = (DatePicker) localView1
				.findViewById(R.id.start_datePicker);
		DatePicker endDatePicker = (DatePicker) localView1
				.findViewById(R.id.end_datePicker);
		// �������ݹ�����
		EditText localEditText = (EditText) localView1
				.findViewById(R.id.query_content_filter);

		// ΪDatePicker���õ�CheckBox���ü������������CheckBoxʱ������DatePicker��ʾ����
		localCheckBox.setOnCheckedChangeListener(new DateChangeListener(
				localView2));

		// �õ������Ի������
		AlertDialog localAlertDialog = creatDialog(localView1,
				R.string.mood_query, new PositiveDialogListener(localView2,
						startDatePicker, endDatePicker, localEditText,
						localCheckBox), new NegativeDialogListener(
						localCheckBox, localEditText),
				new CancelDialogListener(localCheckBox, localEditText));
		return localAlertDialog;
	}

	/* *******************************************************************************
	 * ���ó�ʼ�������뱣�����ܣ���AlertDialogʵ�֡� * ���û���������룬���Menu��ѡ��������öԻ���������������һ��ʱ���������ַ���
	 * * ����MD5���ܺ�洢��SharedPreference��,������ӳɹ��� *
	 * *********************************
	 * **********************************************
	 */
	/**
	 * ���ó�ʼ��������Ի���ȷ�ϰ�ť������
	 */
	class PWInitialPositiveListener implements DialogInterface.OnClickListener {
		private EditText pwInitialText = null;// ��һ����������EditText
		private EditText pwConfirmText = null;// �ڶ���ȷ������EditText

		public PWInitialPositiveListener(EditText pwEditText,
				EditText pwConfirmEditText) {
			pwInitialText = pwEditText;
			pwConfirmText = pwConfirmEditText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			String pwInitialStr = pwInitialText.getText().toString();
			String pwConfirmStr = pwConfirmText.getText().toString();
			String pwString = null;
			// ���������������Ч
			if (MoodPW.PWValidityCheck(MoodLogActivity.this, pwInitialStr,
					pwConfirmStr)) {
				// ��֤��Ч�����������MD5����
				pwString = MoodPW.encryptmd5(MoodPW.MD5(pwConfirmStr));
				// ���������SharedPreferenced��
				Editor editor = mPreferences.edit();
				editor.putString(AppConstants.String_Const.PASSWORD, pwString);
				editor.commit();
				// ��ʾ�������óɹ�
				Toast.makeText(MoodLogActivity.this,
						AppConstants.Notifications.PW_SETTING_SUCCESS,
						Toast.LENGTH_SHORT).show();
				// �ؼ��ĸ�λ
				pwInitialText.setText("");
				pwConfirmText.setText("");
				// �رնԻ���
				dialog.dismiss();
			}
		}
	}

	/**
	 * ���ó�ʼ��������Ի���ȡ����ť������
	 */
	class PWInitialNegativeListener implements DialogInterface.OnClickListener {
		private EditText pwInitialText = null;
		private EditText pwConfirmText = null;

		public PWInitialNegativeListener(EditText pwEditText,
				EditText pwConfirmEditText) {
			pwInitialText = pwEditText;
			pwConfirmText = pwConfirmEditText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// ��տؼ�����
			pwInitialText.setText("");
			pwConfirmText.setText("");
			// �رնԻ���
			dialog.dismiss();
		}
	}

	/**
	 * ���ó�ʼ��������ʱȡ���Ի���ļ�����
	 */
	class PWInitialCancelListener implements DialogInterface.OnCancelListener {
		private EditText pwInitialText = null;
		private EditText pwConfirmText = null;

		public PWInitialCancelListener(EditText pwEditText, EditText editText) {
			pwInitialText = pwEditText;
			pwConfirmText = editText;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// ��տؼ�����
			pwInitialText.setText("");
			pwConfirmText.setText("");
			// �رնԻ���
			dialog.dismiss();
		}
	}

	/**
	 * ������ʼ��������ʱ�ĶԻ���
	 * 
	 * @return ��ʼ��������Ի������
	 */
	public Dialog buildInitialPasswordDialog() {
		// ��dlg_setpassword����ʵ���������ҵ����еĿؼ�
		View localView = LayoutInflater.from(this).inflate(
				R.layout.dlg_setpassword, null);
		EditText pwInitialEditText = (EditText) localView
				.findViewById(R.id.setting_password_edittext);
		EditText pwConfirmEditText = (EditText) localView
				.findViewById(R.id.setting_confirm_edittext);

		// �õ���������Ի������
		AlertDialog localAlertDialog = creatDialog(localView,
				R.string.mood_set_password, new PWInitialPositiveListener(
						pwInitialEditText, pwConfirmEditText),
				new PWInitialNegativeListener(pwInitialEditText,
						pwConfirmEditText), new PWInitialCancelListener(
						pwInitialEditText, pwConfirmEditText));

		return localAlertDialog;
	}

	/* **************************************************************************
	 * ������������ʱ�����뱣�����ܣ���AlertDialog��� * �����������룬����ʱ��Ҫ �û���ȷ����������ܳɹ���½������������������� *
	 * �����Զ��رա� *
	 * ***************************************************************
	 * ***********
	 */

	/**
	 * ��������������������Ի���ȷ�ϰ�ť������
	 */
	class PWInputPositiveListener implements DialogInterface.OnClickListener {
		private EditText pwInputText = null;// ��������EditText

		public PWInputPositiveListener(EditText pwEditText) {
			pwInputText = pwEditText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			String pwInputStr = pwInputText.getText().toString();
			if (!TextUtils.isEmpty(pwInputStr)) {// ��������ǿ�
				// ��������Ƿ�������ȷ
				if (checkPW(pwInputStr)) {
					Toast.makeText(MoodLogActivity.this,
							AppConstants.Notifications.PW_SUCCESS,
							Toast.LENGTH_SHORT).show();
					pw_error_counts = 0;// ��½��������ɹ�������������־λ����
					dialog.dismiss();
					// ��½�ɹ������ó�ʼҳ��
					MoodLogActivity.this.setCursorView();
				} else {
					if (pw_error_counts == 3)// ���������������� �����˳�����
						finish();
					else {// ���������� �������ʾ��Ϣ
						Toast.makeText(
								MoodLogActivity.this,
								AppConstants.Notifications.PW_ERROR + " ������"
										+ (3 - pw_error_counts) + "�λ���",
								Toast.LENGTH_SHORT).show();
						pw_error_counts++;// ����������������
						pwInputText.setText("");// EditText����
					}
				}

			} else {// ��������Ϊ��
				if (pw_error_counts == 3)// ���������������� �����˳�����
					finish();
				else {// ��������Ϊ�յ���ʾ
					Toast.makeText(
							MoodLogActivity.this,
							AppConstants.Notifications.PW_EMPTY + " ������"
									+ (3 - pw_error_counts) + "�λ���",
							Toast.LENGTH_SHORT).show();
					pw_error_counts++;// ����������������
					pwInputText.setText("");// EditText����
				}
			}
		}
	}

	/**
	 * ��������������������Ի���ȡ����ť�ļ�����
	 */
	class PWInputNegativeListener implements DialogInterface.OnClickListener {
		private EditText pwInputText = null;// ���������EditText�ؼ�

		public PWInputNegativeListener(EditText pwEditText) {
			pwInputText = pwEditText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			pwInputText.setText("");
			pw_error_counts = 0;// ���������־λ����
			finish();
		}
	}

	/**
	 * ������������ʱ��������ʱȡ���Ի���ļ�����
	 */
	class PWInputCancelListener implements DialogInterface.OnCancelListener {
		private EditText pwInputText = null;

		public PWInputCancelListener(EditText pwEditText) {
			pwInputText = pwEditText;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			pwInputText.setText("");
			pw_error_counts = 0;// ���������־λ����
			finish();
		}
	}

	/**
	 * ��������������������ʱ�ĶԻ���
	 * 
	 * @return ��������Ի���Ķ���
	 */
	public Dialog buildInputPasswordDialog() {
		// ��dlg_setpassword����ʵ���������ҵ����еĿؼ�
		View localView = LayoutInflater.from(this).inflate(
				R.layout.dlg_inputpassword, null);
		EditText pwInputText = (EditText) localView
				.findViewById(R.id.input_pw_edittext);

		// �õ�����������������Ի������
		AlertDialog localAlertDialog = creatDialog(localView, R.string.log_in,
				new PWInputPositiveListener(pwInputText),
				new PWInputNegativeListener(pwInputText),
				new PWInputCancelListener(pwInputText));

		return localAlertDialog;
	}

	/* *******************************************************************************
	 * ����ȡ�����뱣�����ܣ���AlertDialogʵ�֡������� * ������������ʱ��ѡ�иù��ܲ˵��ᵯ��ȡ�����뱣���Ի�����Ҫ��ȷ����ԭ�ȵ����롡��
	 * *�� ���ܳɹ�ȡ�����뱣�����ܡ� *
	 * *******************************************************
	 * ************************
	 */

	/**
	 * ����ȡ�����뱣�����ܶԻ����ȷ�ϰ�ť������
	 */
	class CanclePWPositiveListener implements DialogInterface.OnClickListener {
		private EditText pwInputText = null;

		public CanclePWPositiveListener(EditText pwEditText) {
			pwInputText = pwEditText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			String pwInputStr = pwInputText.getText().toString();
			if (!TextUtils.isEmpty(pwInputStr)) {// ��������ǿ�
				// ��������Ƿ�������ȷ
				if (checkPW(pwInputStr)) {
					// ɾ��SharedPreferences�����õ�����
					Editor editor = mPreferences.edit();
					editor.remove(AppConstants.String_Const.PASSWORD);
					editor.commit();
					// ���û���ʾ���óɹ�
					Toast.makeText(MoodLogActivity.this,
							AppConstants.Notifications.PW_CANCEL,
							Toast.LENGTH_SHORT).show();
					pw_error_counts = 0;
					pwInputText.setText("");
					dialog.dismiss();
				} else {
					if (pw_error_counts == 3) {// ���������������� ����ȡ����ǰ�Ի���
						pw_error_counts = 0;
						pwInputText.setText("");// EditText����
						dialog.dismiss();
					} else {// ���������� �������ʾ��Ϣ
						Toast.makeText(
								MoodLogActivity.this,
								AppConstants.Notifications.PW_ERROR + " ������"
										+ (3 - pw_error_counts) + "�λ���",
								Toast.LENGTH_SHORT).show();
						pw_error_counts++;// ����������������
						pwInputText.setText("");// EditText����
					}
				}

			} else {// ��������Ϊ��
				if (pw_error_counts == 3) {// ���������������� ����ȡ����ǰ�Ի���
					pw_error_counts = 0;
					pwInputText.setText("");// EditText����
					dialog.dismiss();// �رնԻ���
				} else {
					Toast.makeText(
							MoodLogActivity.this,
							AppConstants.Notifications.PW_EMPTY + " ������"
									+ (3 - pw_error_counts) + "�λ���",
							Toast.LENGTH_SHORT).show();
					pw_error_counts++;// ����������������
					pwInputText.setText("");// EditText����
				}
			}
		}
	}

	/**
	 * ����ȡ�����뱣������ʱȡ���Ի���ļ�����
	 */
	class CanclePWNegativeListener implements DialogInterface.OnClickListener {
		private EditText pwInputText = null;// ��������EditText

		public CanclePWNegativeListener(EditText pwEditText) {
			pwInputText = pwEditText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			pwInputText.setText("");// �ؼ���������
			pw_error_counts = 0;// ���������־λ����
			dialog.dismiss();// �رնԻ���
		}
	}

	/**
	 * ����ȡ�����뱣�����ܶԻ���ļ�����
	 */
	class PWCancelListener implements DialogInterface.OnCancelListener {
		private EditText pwInputText = null;;// ��������EditText

		public PWCancelListener(EditText pwEditText) {
			pwInputText = pwEditText;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			pwInputText.setText("");// �ؼ���������
			pw_error_counts = 0;// ���������־λ����
			dialog.dismiss();// �رնԻ���
		}
	}

	/**
	 * ����ȡ���������빦�ܵĶԻ���
	 * 
	 * @return ȡ������ĶԻ������
	 */
	public Dialog buildCancelPasswordDialog() {
		// ��dlg_setpassword����ʵ���������ҵ����еĿؼ�
		View localView = LayoutInflater.from(this).inflate(
				R.layout.dlg_inputpassword, null);
		EditText pwInputText = (EditText) localView
				.findViewById(R.id.input_pw_edittext);

		// �õ�ȡ���������빦�ܶԻ������
		AlertDialog localAlertDialog = creatDialog(localView,
				R.string.cancel_pw, new CanclePWPositiveListener(pwInputText),
				new CanclePWNegativeListener(pwInputText),
				new PWCancelListener(pwInputText));

		return localAlertDialog;
	}

	/* *************************************************************************
	 * �����޸�����Ի�����AlertDialogʵ�� * ���Ѿ����������룬����˵��й��ܰ�ť�����öԻ��� *
	 * ��Ҫ��ȷ��������벢��������������һ�²�����ȷ�������롣 *
	 * *************************************************************************
	 */

	/**
	 * ���ó�ʼ��������Ի���ȷ�ϰ�ť������
	 */
	class PWChangePositiveListener implements DialogInterface.OnClickListener {
		private EditText pwOldText = null;// ����������EditText
		private EditText pwNewText = null;// ����������EditText
		private EditText pwConfirmText = null;// ������ȷ��EditText

		public PWChangePositiveListener(EditText pwOldEditText,
				EditText pwNewEditText, EditText pwConfirmEditText) {
			pwOldText = pwOldEditText;
			pwNewText = pwNewEditText;
			pwConfirmText = pwConfirmEditText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			String pwOldStr = pwOldText.getText().toString();
			String pwNewStr = pwNewText.getText().toString();
			String pwConfirmStr = pwConfirmText.getText().toString();
			String pwString = null;
			// �������붼�ǿ�
			if (!TextUtils.isEmpty(pwOldStr) && !TextUtils.isEmpty(pwNewStr)
					&& !TextUtils.isEmpty(pwConfirmStr)) {
				// �������ľ������Ƿ���ȷ
				if (checkPW(pwOldStr)) {
					// �����������������Ч
					if (MoodPW.PWValidityCheck(MoodLogActivity.this, pwNewStr,
							pwConfirmStr)) {
						// ��֤��Ч�����������MD5����
						pwString = MoodPW.encryptmd5(MoodPW.MD5(pwConfirmStr));
						// ɾ��ԭ���룬�ٽ����������SharedPreferenced��
						Editor editor = mPreferences.edit();
						editor.remove(AppConstants.String_Const.PASSWORD);
						editor.commit();
						editor.putString(AppConstants.String_Const.PASSWORD,
								pwString);
						editor.commit();
						// ��������ɹ������û���ʾ��Ϣ
						Toast.makeText(MoodLogActivity.this,
								AppConstants.Notifications.PW_CHANGE_SUCCESS,
								Toast.LENGTH_SHORT).show();
						// EditText�ؼ�����
						pwOldText.setText("");
						pwNewText.setText("");
						pwConfirmText.setText("");
						dialog.dismiss();
					}
				} else {// ������������
					if (pw_error_counts == 3) {// ���������������� �����˳�����
						pw_error_counts = 0;
						// EditText�ؼ�����
						pwOldText.setText("");
						pwNewText.setText("");
						pwConfirmText.setText("");
						dialog.dismiss();
					} else {
						// ���û���ʾ�����������
						Toast.makeText(
								MoodLogActivity.this,
								AppConstants.Notifications.PW_ERROR + " ������"
										+ (3 - pw_error_counts) + "�λ���",
								Toast.LENGTH_SHORT).show();
						pw_error_counts++;// ����������������
						// �ؼ���������
						pwOldText.setText("");
						pwNewText.setText("");
						pwConfirmText.setText("");
					}

				}
			} else {// ����������Ϊ�յ����
				// ���û���ʾ
				Toast.makeText(MoodLogActivity.this,
						AppConstants.Notifications.PW_EMPTY, Toast.LENGTH_SHORT)
						.show();
				// �ؼ���������
				pwOldText.setText("");
				pwNewText.setText("");
				pwConfirmText.setText("");
			}
		}
	}

	/**
	 * �����޸�����ʱȡ����ť�ļ�����
	 */
	class PWChangeNegativeListener implements DialogInterface.OnClickListener {
		private EditText pwOldText = null;// ����������EditText
		private EditText pwNewText = null;// ����������EditText
		private EditText pwConfirmText = null;// ������ȷ��EditText

		public PWChangeNegativeListener(EditText pwOldEditText,
				EditText pwNewEditText, EditText pwConfirmEditText) {
			pwOldText = pwOldEditText;
			pwNewText = pwNewEditText;
			pwConfirmText = pwConfirmEditText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// EditText�ؼ���������
			pwOldText.setText("");
			pwNewText.setText("");
			pwConfirmText.setText("");
			pw_error_counts = 0;// ���������־λ����
			dialog.dismiss();
		}
	}

	/**
	 * ����ȡ���޸�����Ի���ļ�����
	 */
	class PWChangeCancelListener implements DialogInterface.OnCancelListener {
		private EditText pwOldText = null;// ����������EditText
		private EditText pwNewText = null;// ����������EditText
		private EditText pwConfirmText = null;// ������ȷ��EditText

		public PWChangeCancelListener(EditText pwOldEditText,
				EditText pwNewEditText, EditText pwConfirmEditText) {
			pwOldText = pwOldEditText;
			pwNewText = pwNewEditText;
			pwConfirmText = pwConfirmEditText;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// EditText�ؼ���������
			pwOldText.setText("");
			pwNewText.setText("");
			pwConfirmText.setText("");
			pw_error_counts = 0;// ���������־λ����
			dialog.dismiss();
		}
	}

	/**
	 * �����޸�����Ի���
	 * 
	 * @return �޸�����Ի������
	 */
	public Dialog buildChangePasswordDialog() {
		// ��dlg_setpassword����ʵ���������ҵ����еĿؼ�
		View localView = LayoutInflater.from(this).inflate(
				R.layout.dlg_changepassword, null);
		EditText oldPWText = (EditText) localView
				.findViewById(R.id.old_pw_edittext);
		EditText newPWText = (EditText) localView
				.findViewById(R.id.new_pw_edittext);
		EditText confirmNewPWText = (EditText) localView
				.findViewById(R.id.confirm_pw_edittext);

		// �õ��޸�����Ի������
		AlertDialog localAlertDialog = creatDialog(localView,
				R.string.change_password, new PWChangePositiveListener(
						oldPWText, newPWText, confirmNewPWText),
				new PWChangeNegativeListener(oldPWText, newPWText,
						confirmNewPWText), new PWChangeCancelListener(
						oldPWText, newPWText, confirmNewPWText));

		return localAlertDialog;
	}

	/**
	 * ���趨�İ����������õ�AlertDialog����
	 * 
	 * @param localView
	 *            ���ֶ���
	 * @param title
	 *            �Ի������
	 * @param posivtiveListener
	 *            ȷ����ť����������
	 * @param negativeListener
	 *            ȡ����ť����������
	 * @param cancelListener
	 *            ȡ��������ť����������
	 * @return AlertDialog����
	 */
	public AlertDialog creatDialog(View localView, int dialogTitle,
			DialogInterface.OnClickListener posivtiveListener,
			DialogInterface.OnClickListener negativeListener,
			DialogInterface.OnCancelListener cancelListener) {
		// ��ʼ�趨AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(
				localView).setTitle(dialogTitle);
		// �趨PositiveButton����ʱ�Ĳ���
		builder.setPositiveButton(R.string.ok, posivtiveListener);
		// �趨NegativeButton����ʱ�Ĳ���
		builder.setNegativeButton(R.string.cancel, negativeListener);
		// �趨���·���ʱ�Ĳ���
		builder.setOnCancelListener(cancelListener);
		// ����AlertDialog
		AlertDialog localAlertDialog = builder.create();
		// ͨ�������޸�AlertControler��handler�����ã�����һ��������AlertDialog�͹ر�
		localAlertDialog = reflectDialog(localAlertDialog);
		return localAlertDialog;
	}

	/* *******************************************************************
	 * MoodLogActivity ���õ����������ܳ�Ա�������ڲ��� *
	 * *******************************************************************
	 */

	/**
	 * ����������鰴ť������
	 */
	class addMoodOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// ����NewMoodAddit Activity
			Intent intent = new Intent(Intent.ACTION_INSERT, getIntent()
					.getData());
			startActivity(intent);
		}
	}

	/**
	 * ˢ�°�ť������
	 */
	class RefreshButtonListener implements OnClickListener {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			// �����ݿ��в�ѯ���µ������¼�����ؽ��������ˢ������������ʾ������
			Cursor freshenCursor = managedQuery(getIntent().getData(),
					AppConstants.PROJECTION, null, null,
					MoodColumns.DEFAULT_SORT_ORDER + MoodColumns.DEFAULT_LIMIT);
			MoodLogActivity.this.adapter.changeCursor(freshenCursor);
		}
	}

	/**
	 * ����������ť������
	 */
	class QueryButtonListener implements OnClickListener {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			// �������������Ի���
			showDialog(SEARCH_DIALOG_ID);
		}
	}

	/**
	 * �̳�SimpleCursorAdapter����д���е�bindView���������ڶ�̬����ʾListView
	 * 
	 */
	class MoodCursorAdapter extends SimpleCursorAdapter {
		@SuppressWarnings("deprecation")
		public MoodCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			super.bindView(view, context, cursor);
			TextView moodDate = (TextView) view.findViewById(R.id.mood_time);
			ImageView moodIcon = (ImageView) view
					.findViewById(R.id.moodIconView);
			TextView moodItem = (TextView) view.findViewById(R.id.mood_item);
			// �õ���������
			String mood = cursor.getString(cursor
					.getColumnIndex(MoodColumns.MOODITEM));
			// ������������ʱ�������������ʾΪ"..."
			// �޸�������ʾ��������������ͼƬ��ʾ
			// HoLyBird
			if (mood.length() > NUM_LIMITS)
				mood = mood.substring(0, NUM_LIMITS - 1) + "...";
			moodItem.setText(Utils.convertNormalStringToSpannableString(
					MoodLogActivity.this, mood), BufferType.SPANNABLE);
			// setViewText(moodItem, mood);
			// �õ�ʱ���
			long timeStamp = cursor.getLong(cursor
					.getColumnIndex(MoodColumns.TIMESTAMP));
			// �õ�ѡ������ͼƬ��resID
			int icon_id = cursor.getInt(cursor
					.getColumnIndex(MoodColumns.ICON_ID));
			// �޸�ͼƬid��ʹ֮����Ӧ����ͼƬ��Ӧ
			// HoLyBird
			// ��ʱ����õ����ڣ�����TextView������
			setViewText(moodDate, getMoodItemDate(timeStamp));
			// ��ID�õ�ͼƬ����ListView����ʾ
			moodIcon.setImageResource(icon_id);
		}

		@Override
		public void setViewText(TextView v, String text) {
			super.setViewText(v, text);
		}
	}

	/**
	 * ����������listView��ʼ��ʾ���ݣ���ʾ���ݿ��д洢������ʮ�������¼
	 */
	@SuppressWarnings("deprecation")
	public void setCursorView() {
		// ���øմ�Ӧ��ʱ��ʾ10������������¼�������ز�ѯ���cursor
		Cursor cursor = managedQuery(
				MoodLogActivity.this.getIntent().getData(),
				AppConstants.PROJECTION, null, null,
				MoodColumns.DEFAULT_SORT_ORDER + MoodColumns.DEFAULT_LIMIT);
		// ��ʾ��Ϣ
		if (cursor.getCount() == 0) {
			// ��һ��ʹ�ã���û����������¼
			Toast.makeText(MoodLogActivity.this, Notifications.NO_MOOD,
					Toast.LENGTH_LONG).show();
		} else if (DaysDifCheck(cursor)) { // ����֮��û�и�������
			Toast.makeText(MoodLogActivity.this,
					Notifications.APART_A_LONG_TIME, Toast.LENGTH_LONG).show();
		}

		// ����SimpleCursorAdapter���������������е������¼��ʾ��ListView��
		adapter = new MoodCursorAdapter(MoodLogActivity.this,
				R.layout.list_item, cursor, new String[] { MoodColumns.ICON_ID,
						MoodColumns.MOODITEM, MoodColumns.TIMESTAMP },
				new int[] { R.id.moodIconView, R.id.mood_item, R.id.mood_time });
		// ����ListAdapter
		noteListView.setAdapter(adapter);
	}

	/**
	 * ��֤�����������ȷ��
	 * 
	 * @param inputPWStr
	 * @return true: ����������ȷ false: �����������
	 */
	public boolean checkPW(String inputPWStr) {
		// �������ַ�������MD5����
		String pwString = MoodPW.encryptmd5(MoodPW.MD5(inputPWStr));
		// �õ�Preferences�д洢������
		String pw = mPreferences.getString(AppConstants.String_Const.PASSWORD,
				AppConstants.String_Const.EMPTY);
		// ͨ���Ƚ�ȷ�������Ƿ�������ȷ
		if (pwString.equals(pw))
			return true;// ����������ȷ
		else
			return false;// �����������
	}

	/**
	 * ͨ�������޸�AlertControler��handler�����ã���ֹһ��������AlertDialog�͹ر�
	 * 
	 * @param alertDialog
	 * @return AlertDialog����
	 */
	public AlertDialog reflectDialog(AlertDialog alertDialog) {
		AlertDialog localAlertDialog = alertDialog;
		try {

			Field field = localAlertDialog.getClass()
					.getDeclaredField("mAlert");
			field.setAccessible(true);
			// ���mAlert������ֵ
			Object obj = field.get(localAlertDialog);
			field = obj.getClass().getDeclaredField("mHandler");
			field.setAccessible(true);
			// �޸�mHandler������ֵ��ʹ���µ�ButtonHandler��
			field.set(obj, new ButtonHandler(localAlertDialog));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localAlertDialog;
	}

	/**
	 * �����޸�AlertControler�е�ButtonHandler���� ����ԭ��AlertDialog��ÿ�ΰ���֮�󲻹ܽ�����Ի���رյĲ�����
	 */
	private static class ButtonHandler extends Handler {

		private WeakReference<DialogInterface> mDialog;

		public ButtonHandler(DialogInterface dialog) {
			mDialog = new WeakReference<DialogInterface>(dialog);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case DialogInterface.BUTTON_POSITIVE:
			case DialogInterface.BUTTON_NEGATIVE:
			case DialogInterface.BUTTON_NEUTRAL:
				((DialogInterface.OnClickListener) msg.obj).onClick(
						mDialog.get(), msg.what);
				break;
			}
		}
	}

	/**
	 * ���ص����Ӧ3��ǰ����UnixTime
	 * 
	 * @return ����UnixTime
	 */
	public long getQueryDateLimit() {
		int year = 0, month = 0, day = 0;
		long limitMills = 0;
		long mills = 0;
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		// �õ���ǰ���ꡢ�¡���
		Calendar c = Calendar.getInstance();
		try {
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// �õ����������ʱ���ַ���
		String timeStr = new String(year + "-" + month + "-" + day
				+ " 00:00:00");
		// �ɵ��������ʱ���ַ����������Ӧ��UnixTime
		try {
			// ��yyyy-MM-dd HH:mm:ss��ʽ�ַ����õ���Ӧ��Date����
			Date dateTemp = sDateFormat.parse(timeStr);
			// ��Date����õ�UnixTime
			mills = dateTemp.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// �õ�3��ǰUnixTime
		limitMills = mills - MILLDIFF;
		return limitMills;
	}

	/**
	 * �������֮��������±��Ƿ��и���
	 * 
	 * @param cursor
	 * @return true:û�и���; false:�и���
	 */
	public Boolean DaysDifCheck(Cursor cursor) {
		long currentMills = 0;
		long queryTimeLimit = 0;

		// �õ��ӵ�������3��ǰ����Ӧ��ʱ���
		queryTimeLimit = getQueryDateLimit();

		cursor.moveToFirst();

		// �õ���ǰ���������¼��ʱ���
		currentMills = cursor.getLong(cursor
				.getColumnIndex(MoodColumns.TIMESTAMP));
		cursor.moveToPrevious();
		if (currentMills <= queryTimeLimit) {
			return true;// �Ѿ�������û�и���
		} else {
			return false;// ����֮���и���
		}
	}

	/**
	 * �ɱ�׼ʱ����õ�����ʱ���µ�ʱ��
	 * 
	 * @param timeStamp
	 *            ʱ���
	 * @return ��׼ʱ���ʽ�ַ���
	 */
	public String getMoodItemDate(long timeStamp) {
		// �����ݿ��б�׼ʱ�任�㵽��������ʱ���µ�ʱ��
		long unixTime = timeStamp + TimeZone.getDefault().getRawOffset();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("MM-dd HH:mm");
		Date date = new Date(unixTime);
		String moodItemDate = sDateFormat.format(date);
		return moodItemDate;
	}

}