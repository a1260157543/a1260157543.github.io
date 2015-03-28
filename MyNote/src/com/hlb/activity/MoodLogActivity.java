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

	// 触发添加心情提示的天数
	private static final long DAYSDIF = 3;
	// ListView中显示的字数上限
	private static int NUM_LIMITS = 20;
	// 主界面中所有Dialog控件启动时的ID
	private static final int SEARCH_DIALOG_ID = 0;
	private static final int EXIT_DIALOG_ID = 1;
	private static final int PW_INITIAL_ID = 2;
	private static final int PW_MODIFIED_ID = 3;
	private static final int PW_LOGIN_ID = 4;
	private static final int PW_CANCEL_ID = 5;
	// SharedPreferences用于存储用户设置的密码
	private SharedPreferences mPreferences = null;
	// 声明主界面中使用的控件
	private ListView noteListView = null;
	private ImageButton addMoodButton = null;
	private TextView titleTextView = null;
	private TextView queryButton = null;
	private TextView refreshButton = null;
	private MoodCursorAdapter adapter = null;
	private ImageButton chartButton = null;
	// 标志常量
	private int pw_error_counts = 0;// 用于记录密码连续输入错误的次数
	private int pw_flag = 0; // 1:有设置密码 0:无密码设置
	// DAYSDIF天时间差换算到毫秒
	private static final long MILLDIFF;
	static {
		MILLDIFF = DAYSDIF * 24 * 60 * 60 * 1000;
	}

	private SlidingMenu mSlidingMenu = null;
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置布局文件
		setContentView(R.layout.main);
		// 控件初始化
		setView();
		// 得到用于存储密码的SharedPreferences对象
		mPreferences = getSharedPreferences(AppConstants.String_Const.LOG_IN,
				MODE_PRIVATE);

		// 检查是否设置了密码
		if (mPreferences.getString(AppConstants.String_Const.PASSWORD, "0")
				.equals("0")) {// 没有设置密码
		} else {// 已设置密码
			// 调用输入密码对话框
			showDialog(PW_LOGIN_ID);
			pw_flag = 1;// 将密码标志置为1，表示有设置密码
		}

		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);

		// 得到provider，若没有，则由MoodColumns.CONTENT_URI设置需要访问的ContentProvider
		Intent intent = getIntent();
		if (intent.getData() == null) {
			intent.setData(MoodColumns.CONTENT_URI);
		}
		// 注册一个2s长点击菜单
		noteListView.setOnCreateContextMenuListener(this);
	}

	/* 初始化控件 */
	public void setView() {
		noteListView = (ListView) findViewById(R.id.mood_note_list);
		noteListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				Uri moodUri = ContentUris.withAppendedId(getIntent().getData(),
						id);
				// 创建Intent对象，由Uri和Intent.ACTION_VIEW向系统指明启动MoodItemView
				// Activity
				Intent intent = new Intent(Intent.ACTION_VIEW, moodUri);
				startActivity(intent);
			}
		});

		titleTextView = (TextView) findViewById(R.id.ivTitleName);
		titleTextView.setText("记事列表");

		addMoodButton = (ImageButton) findViewById(R.id.addMoodButton);
		addMoodButton.setOnClickListener(new addMoodOnClickListener());
		addMoodButton.getBackground().setAlpha(0);

		refreshButton = (TextView) findViewById(R.id.ivTitleBtnLeft);
		refreshButton.setText("刷新");
		refreshButton
				.setBackgroundResource(R.drawable.top_button_right_selector);
		refreshButton.setVisibility(View.VISIBLE);
		refreshButton.setOnClickListener(new RefreshButtonListener());

		queryButton = (TextView) findViewById(R.id.ivTitleBtnRight);
		queryButton.setText("查询");
		queryButton.setVisibility(View.VISIBLE);
		queryButton.setOnClickListener(new QueryButtonListener());

		chartButton = (ImageButton) findViewById(R.id.mood_tendency);
		chartButton.getBackground().setAlpha(0);
		// 设置心情走势按钮的监听器，用户点击后，进入心情统计分析界面ChartTabActivity
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
		int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
		// TODO Auto-generated method stub
		setBehindContentView(R.layout.main_left_layout);
		FragmentTransaction mFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment mFrag = new LeftFragment();
		mFragementTransaction.replace(R.id.main_left_fragment, mFrag);
		mFragementTransaction.commit();
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置是左滑还是右滑，还是左右都可以滑，我这里左右都可以滑
		mSlidingMenu.setShadowWidth(mScreenWidth / 40);// 设置阴影宽度
		mSlidingMenu.setBehindOffset(mScreenWidth / 8);// 设置菜单宽度
		mSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);// 设置左菜单阴影图片
		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.right_shadow);// 设置右菜单阴影图片
		mSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		mSlidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (pw_flag == 0) // 没有设置密码
			// 当没有设置密码的情况下，开启程序，直接显示最近的十条心情记录
			setCursorView();
	}

	/**
	 * 设置Menu菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 设置菜单
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.moodlog_option_munu, menu);
		// return super.onCreateOptionsMenu(menu);
		return false;
	}

	/**
	 * 点击菜单选项时的操作
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		/* 设置/修改密码功能 */
		case R.id.menu_setting: {
			// 用户还没有设置密码
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY))
				showDialog(PW_INITIAL_ID);// 启动设置密码对话框
			// 用于已经设置密码
			else {
				showDialog(PW_MODIFIED_ID); // 启动修改密码对话框
			}
			return true;
		}
		/* 取消密码保护功能 */
		case R.id.menu_cancel_pw: {
			// 用户还没有设置密码
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY)) {
				// 提示用户，还没设置密码保护功能
				Toast.makeText(MoodLogActivity.this,
						AppConstants.Notifications.PW_WITHOUT,
						Toast.LENGTH_SHORT).show();
			} else {// 用户已经设置密码
				showDialog(PW_CANCEL_ID);// 启动取消密码对话框
			}
			return true;
		}
		/* 刷新MoodLogActivity显示最新的十条心情记录 */
		case R.id.menu_refreshen: {
			// 在数据库中查询最新的心情记录，返回结果集用于刷新主界面中显示的心情
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
	 * 设置上下文菜单
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

		// 首先确认已经选中了日志列表中的一个日志，若没选择，则直接返回。Cursor指向选中的日志项
		Cursor cursor = (Cursor) noteListView.getAdapter().getItem(
				info.position);
		if (cursor == null) {
			return;
		}
		// 从布局文件中得到MenuInflater对象
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.moodlog_context_menu, menu);

		// 设置Context menu 标题
		menu.setHeaderTitle(AppConstants.String_Const.CONTEXT_MENU_HEADER);
	}

	/**
	 * 设置在屏幕中选中一个选项2S之后的操作
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
		// 拼接选中项的Uri
		Uri moodUri = ContentUris
				.withAppendedId(getIntent().getData(), info.id);

		switch (item.getItemId()) {
		// 对心情的查看，启动MoodItemView Activity
		case R.id.context_view:
			startActivity(new Intent(Intent.ACTION_VIEW, moodUri));
			return true;
			// 删除选中的心情记录
		case R.id.context_delete:
			getContentResolver().delete(moodUri, null, null);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	/**
	 * 设置在主界面中按下KEYCODE_BACK时的操作，将弹出退出程序确认对话框
	 */
	@SuppressWarnings("deprecation")
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 弹出退出程序确认对话框
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(EXIT_DIALOG_ID);
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 当有对话框需要创建时，调用该方法，根据showDialog()中传入的ID启动相应的对话框
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		if (id == SEARCH_DIALOG_ID) { // 弹出心情搜索对话框
			dialog = buildSearchDialog();
			return dialog;
		} else if (id == EXIT_DIALOG_ID) { // 弹出退出确认对话框
			dialog = buildConfirmExitDialog();
			return dialog;
		} else if (id == PW_INITIAL_ID) { // 弹出创建设置密码对话框
			dialog = buildInitialPasswordDialog();
			return dialog;
		} else if (id == PW_MODIFIED_ID) { // 弹出创建修改密码对话框
			dialog = buildChangePasswordDialog();
			return dialog;
		} else if (id == PW_LOGIN_ID) { // 弹出输入密码对话框
			dialog = buildInputPasswordDialog();
			return dialog;
		} else if (id == PW_CANCEL_ID) { // 弹出取消密码保护对话框
			dialog = buildCancelPasswordDialog();
			return dialog;
		} else
			return super.onCreateDialog(id);
	}

	/**
	 * 创建退出程序时弹出对话框
	 * 
	 * @return 返回创建的对话框对象
	 */
	public Dialog buildConfirmExitDialog() {
		// 开始设定AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle(R.string.exit_dialog);
		// 设定PositiveButton按下时的操作
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 退出程序，关闭MoodLogActivity
						MoodLogActivity.this.finish();
					}
				});
		// 设定NegativeButton按下时的操作
		builder.setNegativeButton(R.string.cancel, null);
		// 创建AlertDialog
		AlertDialog exitAlertDialog = builder.create();
		return exitAlertDialog;
	}

	/* ********************************************************************************
	 * 设置对心情内容搜索的功能，可以通过选择日期及内容过滤完成搜索，由AlertDialog来完成。 *
	 * 由两个DatePicker实现搜索起始和终止日期的选择，再由关键字的搜索共同完成搜索功能 *
	 * 搜索成功后，由结果集在MoodLogAcitity的ListView中显示搜索结果 *
	 * **************************************************
	 */

	/**
	 * 搜索时对话框中CheckBox是否激活的监听器
	 * 
	 */
	class DateChangeListener implements CompoundButton.OnCheckedChangeListener {

		private View localView = null;

		public DateChangeListener(View v) {
			localView = v;
		}

		// 每次点击CheckBox，状态改变
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
	 * 搜索时AlertDialog确认键的监听器
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
			int flag = 0;// 用于判断DatePicker出错的标志
			long startStamp = 0;
			long endStamp = 0;
			String dateFilter = null;
			String contentFilter = null;
			String queryFilter = null;

			// DatePicker显示，进行日期选择
			if (localView.getVisibility() == 0) {
				// 得到在DatePicker中设置的搜索起始日期
				int startYear = startDatePicker.getYear();
				int startMonth = startDatePicker.getMonth();
				int startDay = startDatePicker.getDayOfMonth();
				int endYear = endDatePicker.getYear();
				int endMonth = endDatePicker.getMonth();
				int endDay = endDatePicker.getDayOfMonth();
				// 将得到的日期转换成时间戳，用于在数据库中搜索
				Calendar localCalendar = Calendar.getInstance();
				localCalendar.set(startYear, startMonth, startDay, 0, 0);
				startStamp = localCalendar.getTimeInMillis();
				localCalendar.set(endYear, endMonth, endDay, 23, 59);
				endStamp = localCalendar.getTimeInMillis();
				// 起始日期大于结束日期，提示出错
				if (startStamp > endStamp) {
					flag = 1;// DatePicker中日期选择出错，标志位置为1
				} else {
					flag = 0;
					// 确定在数据库中搜索时的搜索条件
					// 时间戳限制条件
					dateFilter = MoodColumns.TIMESTAMP + " >= " + startStamp
							+ " and " + MoodColumns.TIMESTAMP + " <= "
							+ endStamp;
				}
			}

			// 得到内容过滤器中输入的字符串
			String content = localEditText.getText().toString();
			if (!TextUtils.isEmpty(content))
				// 内容过滤器限制条件
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
			// DatePicker中日期选择出错，提示错误信息
			if (flag == 1) {
				Toast.makeText(MoodLogActivity.this,
						Notifications.DATEPICKERERROR, Toast.LENGTH_LONG)
						.show();
			} else if (!TextUtils.isEmpty(queryFilter)) {// 根据用户选择，启动并实现查询功能
				Cursor queryCursor = MoodLogActivity.this.getContentResolver()
						.query(getIntent().getData(), AppConstants.PROJECTION,
								queryFilter, null,
								MoodColumns.DEFAULT_SORT_ORDER);
				MoodLogActivity.this.adapter.changeCursor(queryCursor);
				localCheckBox.setChecked(false);
				localEditText.setText("");
				dialog.dismiss();
				queryCursor.close();
			} else if (flag == 0 && TextUtils.isEmpty(queryFilter)) {// 不做任何操作，与NegativeButton操作相同
				localCheckBox.setChecked(false);
				localEditText.setText("");
				dialog.dismiss();
			}
		}
	}

	// 搜索对话框中取消按钮的监听器
	class NegativeDialogListener implements DialogInterface.OnClickListener {
		private CheckBox localCheckBox = null;
		private EditText localEditText = null;

		public NegativeDialogListener(CheckBox checkBox, EditText editText) {
			localCheckBox = checkBox;
			localEditText = editText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// 关闭CheckBox
			localCheckBox.setChecked(false);
			localEditText.setText("");
			// 关闭对话框
			dialog.dismiss();
		}
	}

	// 取消搜素对话框的监听器
	class CancelDialogListener implements DialogInterface.OnCancelListener {
		private CheckBox localCheckBox = null;
		private EditText localEditText = null;

		public CancelDialogListener(CheckBox checkBox, EditText editText) {
			localCheckBox = checkBox;
			localEditText = editText;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// 关闭CheckBox
			localCheckBox.setChecked(false);
			localEditText.setText("");
			// 关闭对话框
			dialog.dismiss();
		}
	}

	/**
	 * 当用户按下搜索按钮时，创建搜索对话框，可选择搜索时间范围并通过输入关键字搜索
	 * 
	 * @return 创建的搜索对话框
	 */
	public Dialog buildSearchDialog() {
		// 将dlg_search布局实例化，并找到其中的控件
		View localView1 = LayoutInflater.from(this).inflate(
				R.layout.dlg_search, null);
		CheckBox localCheckBox = (CheckBox) localView1
				.findViewById(R.id.datePicker_checkBox);
		View localView2 = localView1.findViewById(R.id.datePicker_layout);
		DatePicker startDatePicker = (DatePicker) localView1
				.findViewById(R.id.start_datePicker);
		DatePicker endDatePicker = (DatePicker) localView1
				.findViewById(R.id.end_datePicker);
		// 心情内容过滤器
		EditText localEditText = (EditText) localView1
				.findViewById(R.id.query_content_filter);

		// 为DatePicker设置的CheckBox设置监听器，当点击CheckBox时将两个DatePicker显示出来
		localCheckBox.setOnCheckedChangeListener(new DateChangeListener(
				localView2));

		// 得到搜索对话框对象
		AlertDialog localAlertDialog = creatDialog(localView1,
				R.string.mood_query, new PositiveDialogListener(localView2,
						startDatePicker, endDatePicker, localEditText,
						localCheckBox), new NegativeDialogListener(
						localCheckBox, localEditText),
				new CancelDialogListener(localCheckBox, localEditText));
		return localAlertDialog;
	}

	/* *******************************************************************************
	 * 设置初始设置密码保护功能，由AlertDialog实现。 * 如果没有设置密码，点击Menu中选项会启动该对话框。两次密码输入一致时，对输入字符串
	 * * 经由MD5加密后存储在SharedPreference中,密码添加成功。 *
	 * *********************************
	 * **********************************************
	 */
	/**
	 * 设置初始设置密码对话框确认按钮监听器
	 */
	class PWInitialPositiveListener implements DialogInterface.OnClickListener {
		private EditText pwInitialText = null;// 第一次输入密码EditText
		private EditText pwConfirmText = null;// 第二次确认密码EditText

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
			// 两次输入的密码有效
			if (MoodPW.PWValidityCheck(MoodLogActivity.this, pwInitialStr,
					pwConfirmStr)) {
				// 验证有效，对密码进行MD5加密
				pwString = MoodPW.encryptmd5(MoodPW.MD5(pwConfirmStr));
				// 将密码存入SharedPreferenced中
				Editor editor = mPreferences.edit();
				editor.putString(AppConstants.String_Const.PASSWORD, pwString);
				editor.commit();
				// 提示密码设置成功
				Toast.makeText(MoodLogActivity.this,
						AppConstants.Notifications.PW_SETTING_SUCCESS,
						Toast.LENGTH_SHORT).show();
				// 控件的复位
				pwInitialText.setText("");
				pwConfirmText.setText("");
				// 关闭对话框
				dialog.dismiss();
			}
		}
	}

	/**
	 * 设置初始设置密码对话框取消按钮监听器
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
			// 清空控件内容
			pwInitialText.setText("");
			pwConfirmText.setText("");
			// 关闭对话框
			dialog.dismiss();
		}
	}

	/**
	 * 设置初始设置密码时取消对话框的监听器
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
			// 清空控件内容
			pwInitialText.setText("");
			pwConfirmText.setText("");
			// 关闭对话框
			dialog.dismiss();
		}
	}

	/**
	 * 创建初始设置密码时的对话框
	 * 
	 * @return 初始设置密码对话框对象
	 */
	public Dialog buildInitialPasswordDialog() {
		// 将dlg_setpassword布局实例化，并找到其中的控件
		View localView = LayoutInflater.from(this).inflate(
				R.layout.dlg_setpassword, null);
		EditText pwInitialEditText = (EditText) localView
				.findViewById(R.id.setting_password_edittext);
		EditText pwConfirmEditText = (EditText) localView
				.findViewById(R.id.setting_confirm_edittext);

		// 得到设置密码对话框对象
		AlertDialog localAlertDialog = creatDialog(localView,
				R.string.mood_set_password, new PWInitialPositiveListener(
						pwInitialEditText, pwConfirmEditText),
				new PWInitialNegativeListener(pwInitialEditText,
						pwConfirmEditText), new PWInitialCancelListener(
						pwInitialEditText, pwConfirmEditText));

		return localAlertDialog;
	}

	/* **************************************************************************
	 * 设置启动程序时的密码保护功能，由AlertDialog完成 * 若设置了密码，启动时需要 用户正确输入密码才能成功登陆，若连续三次输入错误， *
	 * 程序将自动关闭。 *
	 * ***************************************************************
	 * ***********
	 */

	/**
	 * 设置启动程序输入密码对话框确认按钮监听器
	 */
	class PWInputPositiveListener implements DialogInterface.OnClickListener {
		private EditText pwInputText = null;// 输入密码EditText

		public PWInputPositiveListener(EditText pwEditText) {
			pwInputText = pwEditText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			String pwInputStr = pwInputText.getText().toString();
			if (!TextUtils.isEmpty(pwInputStr)) {// 输入密码非空
				// 检查密码是否输入正确
				if (checkPW(pwInputStr)) {
					Toast.makeText(MoodLogActivity.this,
							AppConstants.Notifications.PW_SUCCESS,
							Toast.LENGTH_SHORT).show();
					pw_error_counts = 0;// 登陆密码输入成功，对密码错误标志位清零
					dialog.dismiss();
					// 登陆成功，设置初始页面
					MoodLogActivity.this.setCursorView();
				} else {
					if (pw_error_counts == 3)// 连续三次输入密码 错误，退出程序
						finish();
					else {// 对密码输入 错误的提示信息
						Toast.makeText(
								MoodLogActivity.this,
								AppConstants.Notifications.PW_ERROR + " 您还有"
										+ (3 - pw_error_counts) + "次机会",
								Toast.LENGTH_SHORT).show();
						pw_error_counts++;// 输入错误次数的自增
						pwInputText.setText("");// EditText清零
					}
				}

			} else {// 输入密码为空
				if (pw_error_counts == 3)// 连续三次输入密码 错误，退出程序
					finish();
				else {// 输入密码为空的提示
					Toast.makeText(
							MoodLogActivity.this,
							AppConstants.Notifications.PW_EMPTY + " 您还有"
									+ (3 - pw_error_counts) + "次机会",
							Toast.LENGTH_SHORT).show();
					pw_error_counts++;// 输入错误次数的自增
					pwInputText.setText("");// EditText清零
				}
			}
		}
	}

	/**
	 * 设置启动程序输入密码对话框取消按钮的监听器
	 */
	class PWInputNegativeListener implements DialogInterface.OnClickListener {
		private EditText pwInputText = null;// 输入密码的EditText控件

		public PWInputNegativeListener(EditText pwEditText) {
			pwInputText = pwEditText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			pwInputText.setText("");
			pw_error_counts = 0;// 错误次数标志位清零
			finish();
		}
	}

	/**
	 * 设置启动程序时输入密码时取消对话框的监听器
	 */
	class PWInputCancelListener implements DialogInterface.OnCancelListener {
		private EditText pwInputText = null;

		public PWInputCancelListener(EditText pwEditText) {
			pwInputText = pwEditText;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			pwInputText.setText("");
			pw_error_counts = 0;// 错误次数标志位清零
			finish();
		}
	}

	/**
	 * 创建启动程序输入密码时的对话框
	 * 
	 * @return 输入密码对话框的对象
	 */
	public Dialog buildInputPasswordDialog() {
		// 将dlg_setpassword布局实例化，并找到其中的控件
		View localView = LayoutInflater.from(this).inflate(
				R.layout.dlg_inputpassword, null);
		EditText pwInputText = (EditText) localView
				.findViewById(R.id.input_pw_edittext);

		// 得到启动程序输入密码对话框对象
		AlertDialog localAlertDialog = creatDialog(localView, R.string.log_in,
				new PWInputPositiveListener(pwInputText),
				new PWInputNegativeListener(pwInputText),
				new PWInputCancelListener(pwInputText));

		return localAlertDialog;
	}

	/* *******************************************************************************
	 * 设置取消密码保护功能，由AlertDialog实现　　　　 * 当设置了密码时，选中该功能菜单会弹出取消密码保护对话框。需要正确输入原先的密码　　
	 * *　 才能成功取消密码保护功能。 *
	 * *******************************************************
	 * ************************
	 */

	/**
	 * 设置取消密码保护功能对话框的确认按钮监听器
	 */
	class CanclePWPositiveListener implements DialogInterface.OnClickListener {
		private EditText pwInputText = null;

		public CanclePWPositiveListener(EditText pwEditText) {
			pwInputText = pwEditText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			String pwInputStr = pwInputText.getText().toString();
			if (!TextUtils.isEmpty(pwInputStr)) {// 输入密码非空
				// 检查密码是否输入正确
				if (checkPW(pwInputStr)) {
					// 删除SharedPreferences中设置的密码
					Editor editor = mPreferences.edit();
					editor.remove(AppConstants.String_Const.PASSWORD);
					editor.commit();
					// 向用户提示设置成功
					Toast.makeText(MoodLogActivity.this,
							AppConstants.Notifications.PW_CANCEL,
							Toast.LENGTH_SHORT).show();
					pw_error_counts = 0;
					pwInputText.setText("");
					dialog.dismiss();
				} else {
					if (pw_error_counts == 3) {// 连续三次输入密码 错误，取消当前对话框
						pw_error_counts = 0;
						pwInputText.setText("");// EditText清零
						dialog.dismiss();
					} else {// 对密码输入 错误的提示信息
						Toast.makeText(
								MoodLogActivity.this,
								AppConstants.Notifications.PW_ERROR + " 您还有"
										+ (3 - pw_error_counts) + "次机会",
								Toast.LENGTH_SHORT).show();
						pw_error_counts++;// 输入错误次数的自增
						pwInputText.setText("");// EditText清零
					}
				}

			} else {// 输入密码为空
				if (pw_error_counts == 3) {// 连续三次输入密码 错误，取消当前对话框
					pw_error_counts = 0;
					pwInputText.setText("");// EditText清零
					dialog.dismiss();// 关闭对话框
				} else {
					Toast.makeText(
							MoodLogActivity.this,
							AppConstants.Notifications.PW_EMPTY + " 您还有"
									+ (3 - pw_error_counts) + "次机会",
							Toast.LENGTH_SHORT).show();
					pw_error_counts++;// 输入错误次数的自增
					pwInputText.setText("");// EditText清零
				}
			}
		}
	}

	/**
	 * 设置取消密码保护功能时取消对话框的监听器
	 */
	class CanclePWNegativeListener implements DialogInterface.OnClickListener {
		private EditText pwInputText = null;// 输入密码EditText

		public CanclePWNegativeListener(EditText pwEditText) {
			pwInputText = pwEditText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			pwInputText.setText("");// 控件内容清零
			pw_error_counts = 0;// 错误次数标志位清零
			dialog.dismiss();// 关闭对话框
		}
	}

	/**
	 * 设置取消密码保护功能对话框的监听器
	 */
	class PWCancelListener implements DialogInterface.OnCancelListener {
		private EditText pwInputText = null;;// 输入密码EditText

		public PWCancelListener(EditText pwEditText) {
			pwInputText = pwEditText;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			pwInputText.setText("");// 控件内容清零
			pw_error_counts = 0;// 错误次数标志位清零
			dialog.dismiss();// 关闭对话框
		}
	}

	/**
	 * 创建取消输入密码功能的对话框
	 * 
	 * @return 取消密码的对话框对象
	 */
	public Dialog buildCancelPasswordDialog() {
		// 将dlg_setpassword布局实例化，并找到其中的控件
		View localView = LayoutInflater.from(this).inflate(
				R.layout.dlg_inputpassword, null);
		EditText pwInputText = (EditText) localView
				.findViewById(R.id.input_pw_edittext);

		// 得到取消输入密码功能对话框对象
		AlertDialog localAlertDialog = creatDialog(localView,
				R.string.cancel_pw, new CanclePWPositiveListener(pwInputText),
				new CanclePWNegativeListener(pwInputText),
				new PWCancelListener(pwInputText));

		return localAlertDialog;
	}

	/* *************************************************************************
	 * 设置修改密码对话框，由AlertDialog实现 * 当已经设置了密码，点击菜单中功能按钮弹出该对话框。 *
	 * 需要正确输入旧密码并两次输入新密码一致才能正确更改密码。 *
	 * *************************************************************************
	 */

	/**
	 * 设置初始设置密码对话框确认按钮监听器
	 */
	class PWChangePositiveListener implements DialogInterface.OnClickListener {
		private EditText pwOldText = null;// 旧密码输入EditText
		private EditText pwNewText = null;// 新密码输入EditText
		private EditText pwConfirmText = null;// 新密码确认EditText

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
			// 输入密码都非空
			if (!TextUtils.isEmpty(pwOldStr) && !TextUtils.isEmpty(pwNewStr)
					&& !TextUtils.isEmpty(pwConfirmStr)) {
				// 检查输入的旧密码是否正确
				if (checkPW(pwOldStr)) {
					// 两次输入的新密码有效
					if (MoodPW.PWValidityCheck(MoodLogActivity.this, pwNewStr,
							pwConfirmStr)) {
						// 验证有效，对密码进行MD5加密
						pwString = MoodPW.encryptmd5(MoodPW.MD5(pwConfirmStr));
						// 删除原密码，再将新密码存入SharedPreferenced中
						Editor editor = mPreferences.edit();
						editor.remove(AppConstants.String_Const.PASSWORD);
						editor.commit();
						editor.putString(AppConstants.String_Const.PASSWORD,
								pwString);
						editor.commit();
						// 更改密码成功，向用户提示信息
						Toast.makeText(MoodLogActivity.this,
								AppConstants.Notifications.PW_CHANGE_SUCCESS,
								Toast.LENGTH_SHORT).show();
						// EditText控件清零
						pwOldText.setText("");
						pwNewText.setText("");
						pwConfirmText.setText("");
						dialog.dismiss();
					}
				} else {// 输入旧密码错误
					if (pw_error_counts == 3) {// 连续三次输入密码 错误，退出程序
						pw_error_counts = 0;
						// EditText控件清零
						pwOldText.setText("");
						pwNewText.setText("");
						pwConfirmText.setText("");
						dialog.dismiss();
					} else {
						// 向用户提示密码输入错误
						Toast.makeText(
								MoodLogActivity.this,
								AppConstants.Notifications.PW_ERROR + " 您还有"
										+ (3 - pw_error_counts) + "次机会",
								Toast.LENGTH_SHORT).show();
						pw_error_counts++;// 输入错误次数的自增
						// 控件内容清零
						pwOldText.setText("");
						pwNewText.setText("");
						pwConfirmText.setText("");
					}

				}
			} else {// 输入内容有为空的情况
				// 向用户提示
				Toast.makeText(MoodLogActivity.this,
						AppConstants.Notifications.PW_EMPTY, Toast.LENGTH_SHORT)
						.show();
				// 控件内容清零
				pwOldText.setText("");
				pwNewText.setText("");
				pwConfirmText.setText("");
			}
		}
	}

	/**
	 * 设置修改密码时取消按钮的监听器
	 */
	class PWChangeNegativeListener implements DialogInterface.OnClickListener {
		private EditText pwOldText = null;// 旧密码输入EditText
		private EditText pwNewText = null;// 新密码输入EditText
		private EditText pwConfirmText = null;// 新密码确认EditText

		public PWChangeNegativeListener(EditText pwOldEditText,
				EditText pwNewEditText, EditText pwConfirmEditText) {
			pwOldText = pwOldEditText;
			pwNewText = pwNewEditText;
			pwConfirmText = pwConfirmEditText;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// EditText控件内容清零
			pwOldText.setText("");
			pwNewText.setText("");
			pwConfirmText.setText("");
			pw_error_counts = 0;// 错误次数标志位清零
			dialog.dismiss();
		}
	}

	/**
	 * 设置取消修改密码对话框的监听器
	 */
	class PWChangeCancelListener implements DialogInterface.OnCancelListener {
		private EditText pwOldText = null;// 旧密码输入EditText
		private EditText pwNewText = null;// 新密码输入EditText
		private EditText pwConfirmText = null;// 新密码确认EditText

		public PWChangeCancelListener(EditText pwOldEditText,
				EditText pwNewEditText, EditText pwConfirmEditText) {
			pwOldText = pwOldEditText;
			pwNewText = pwNewEditText;
			pwConfirmText = pwConfirmEditText;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// EditText控件内容清零
			pwOldText.setText("");
			pwNewText.setText("");
			pwConfirmText.setText("");
			pw_error_counts = 0;// 错误次数标志位清零
			dialog.dismiss();
		}
	}

	/**
	 * 创建修改密码对话框
	 * 
	 * @return 修改密码对话框对象
	 */
	public Dialog buildChangePasswordDialog() {
		// 将dlg_setpassword布局实例化，并找到其中的控件
		View localView = LayoutInflater.from(this).inflate(
				R.layout.dlg_changepassword, null);
		EditText oldPWText = (EditText) localView
				.findViewById(R.id.old_pw_edittext);
		EditText newPWText = (EditText) localView
				.findViewById(R.id.new_pw_edittext);
		EditText confirmNewPWText = (EditText) localView
				.findViewById(R.id.confirm_pw_edittext);

		// 得到修改密码对话框对象
		AlertDialog localAlertDialog = creatDialog(localView,
				R.string.change_password, new PWChangePositiveListener(
						oldPWText, newPWText, confirmNewPWText),
				new PWChangeNegativeListener(oldPWText, newPWText,
						confirmNewPWText), new PWChangeCancelListener(
						oldPWText, newPWText, confirmNewPWText));

		return localAlertDialog;
	}

	/**
	 * 由设定的按键监听器得到AlertDialog对象
	 * 
	 * @param localView
	 *            布局对象
	 * @param title
	 *            对话框标题
	 * @param posivtiveListener
	 *            确定按钮监听器对象
	 * @param negativeListener
	 *            取消按钮监听器对象
	 * @param cancelListener
	 *            取消操作按钮监听器对象
	 * @return AlertDialog对象
	 */
	public AlertDialog creatDialog(View localView, int dialogTitle,
			DialogInterface.OnClickListener posivtiveListener,
			DialogInterface.OnClickListener negativeListener,
			DialogInterface.OnCancelListener cancelListener) {
		// 开始设定AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(
				localView).setTitle(dialogTitle);
		// 设定PositiveButton按下时的操作
		builder.setPositiveButton(R.string.ok, posivtiveListener);
		// 设定NegativeButton按下时的操作
		builder.setNegativeButton(R.string.cancel, negativeListener);
		// 设定按下返回时的操作
		builder.setOnCancelListener(cancelListener);
		// 创建AlertDialog
		AlertDialog localAlertDialog = builder.create();
		// 通过反射修改AlertControler中handler的设置，避免一旦按键，AlertDialog就关闭
		localAlertDialog = reflectDialog(localAlertDialog);
		return localAlertDialog;
	}

	/* *******************************************************************
	 * MoodLogActivity 中用到的其他功能成员函数及内部类 *
	 * *******************************************************************
	 */

	/**
	 * 设置添加心情按钮监听器
	 */
	class addMoodOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// 启动NewMoodAddit Activity
			Intent intent = new Intent(Intent.ACTION_INSERT, getIntent()
					.getData());
			startActivity(intent);
		}
	}

	/**
	 * 刷新按钮监听器
	 */
	class RefreshButtonListener implements OnClickListener {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			// 在数据库中查询最新的心情记录，返回结果集用于刷新主界面中显示的心情
			Cursor freshenCursor = managedQuery(getIntent().getData(),
					AppConstants.PROJECTION, null, null,
					MoodColumns.DEFAULT_SORT_ORDER + MoodColumns.DEFAULT_LIMIT);
			MoodLogActivity.this.adapter.changeCursor(freshenCursor);
		}
	}

	/**
	 * 心情搜索按钮监听器
	 */
	class QueryButtonListener implements OnClickListener {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			// 启动心情搜索对话框
			showDialog(SEARCH_DIALOG_ID);
		}
	}

	/**
	 * 继承SimpleCursorAdapter，复写其中的bindView方法，用于动态的显示ListView
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
			// 得到心情内容
			String mood = cursor.getString(cursor
					.getColumnIndex(MoodColumns.MOODITEM));
			// 字数超过限制时，后面的内容显示为"..."
			// 修改文字显示，将表情文字以图片显示
			// HoLyBird
			if (mood.length() > NUM_LIMITS)
				mood = mood.substring(0, NUM_LIMITS - 1) + "...";
			moodItem.setText(Utils.convertNormalStringToSpannableString(
					MoodLogActivity.this, mood), BufferType.SPANNABLE);
			// setViewText(moodItem, mood);
			// 得到时间戳
			long timeStamp = cursor.getLong(cursor
					.getColumnIndex(MoodColumns.TIMESTAMP));
			// 得到选定表情图片的resID
			int icon_id = cursor.getInt(cursor
					.getColumnIndex(MoodColumns.ICON_ID));
			// 修改图片id，使之与相应表情图片对应
			// HoLyBird
			// 由时间戳得到日期，并在TextView中设置
			setViewText(moodDate, getMoodItemDate(timeStamp));
			// 由ID得到图片并在ListView中显示
			moodIcon.setImageResource(icon_id);
		}

		@Override
		public void setViewText(TextView v, String text) {
			super.setViewText(v, text);
		}
	}

	/**
	 * 设置主界面listView初始显示内容，显示数据库中存储的最新十条心情记录
	 */
	@SuppressWarnings("deprecation")
	public void setCursorView() {
		// 设置刚打开应用时显示10条最近的心情记录，并返回查询后的cursor
		Cursor cursor = managedQuery(
				MoodLogActivity.this.getIntent().getData(),
				AppConstants.PROJECTION, null, null,
				MoodColumns.DEFAULT_SORT_ORDER + MoodColumns.DEFAULT_LIMIT);
		// 提示信息
		if (cursor.getCount() == 0) {
			// 第一次使用，还没有添加心情记录
			Toast.makeText(MoodLogActivity.this, Notifications.NO_MOOD,
					Toast.LENGTH_LONG).show();
		} else if (DaysDifCheck(cursor)) { // 三天之内没有更新心情
			Toast.makeText(MoodLogActivity.this,
					Notifications.APART_A_LONG_TIME, Toast.LENGTH_LONG).show();
		}

		// 构造SimpleCursorAdapter用于搜索后结果集中的心情记录显示在ListView中
		adapter = new MoodCursorAdapter(MoodLogActivity.this,
				R.layout.list_item, cursor, new String[] { MoodColumns.ICON_ID,
						MoodColumns.MOODITEM, MoodColumns.TIMESTAMP },
				new int[] { R.id.moodIconView, R.id.mood_item, R.id.mood_time });
		// 设置ListAdapter
		noteListView.setAdapter(adapter);
	}

	/**
	 * 验证输入密码的正确性
	 * 
	 * @param inputPWStr
	 * @return true: 输入密码正确 false: 输入密码错误
	 */
	public boolean checkPW(String inputPWStr) {
		// 对输入字符串进行MD5运算
		String pwString = MoodPW.encryptmd5(MoodPW.MD5(inputPWStr));
		// 得到Preferences中存储的密码
		String pw = mPreferences.getString(AppConstants.String_Const.PASSWORD,
				AppConstants.String_Const.EMPTY);
		// 通过比较确定密码是否输入正确
		if (pwString.equals(pw))
			return true;// 密码输入正确
		else
			return false;// 密码输入错误
	}

	/**
	 * 通过反射修改AlertControler中handler的设置，防止一旦按键，AlertDialog就关闭
	 * 
	 * @param alertDialog
	 * @return AlertDialog对象
	 */
	public AlertDialog reflectDialog(AlertDialog alertDialog) {
		AlertDialog localAlertDialog = alertDialog;
		try {

			Field field = localAlertDialog.getClass()
					.getDeclaredField("mAlert");
			field.setAccessible(true);
			// 获得mAlert变量的值
			Object obj = field.get(localAlertDialog);
			field = obj.getClass().getDeclaredField("mHandler");
			field.setAccessible(true);
			// 修改mHandler变量的值，使用新的ButtonHandler类
			field.set(obj, new ButtonHandler(localAlertDialog));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localAlertDialog;
	}

	/**
	 * 用于修改AlertControler中的ButtonHandler对象， 更改原先AlertDialog中每次按键之后不管结果将对话框关闭的操作。
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
	 * 返回当天对应3天前零点的UnixTime
	 * 
	 * @return 返回UnixTime
	 */
	public long getQueryDateLimit() {
		int year = 0, month = 0, day = 0;
		long limitMills = 0;
		long mills = 0;
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		// 得到当前的年、月、日
		Calendar c = Calendar.getInstance();
		try {
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 得到当天的整点时间字符串
		String timeStr = new String(year + "-" + month + "-" + day
				+ " 00:00:00");
		// 由当天的整点时间字符串计算出相应的UnixTime
		try {
			// 由yyyy-MM-dd HH:mm:ss各式字符串得到相应的Date对象
			Date dateTemp = sDateFormat.parse(timeStr);
			// 由Date对象得到UnixTime
			mills = dateTemp.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 得到3天前UnixTime
		limitMills = mills - MILLDIFF;
		return limitMills;
	}

	/**
	 * 检查三天之内心情记事本是否有更新
	 * 
	 * @param cursor
	 * @return true:没有更新; false:有更新
	 */
	public Boolean DaysDifCheck(Cursor cursor) {
		long currentMills = 0;
		long queryTimeLimit = 0;

		// 得到从当天算起3天前零点对应的时间戳
		queryTimeLimit = getQueryDateLimit();

		cursor.moveToFirst();

		// 得到当前最新心情记录的时间戳
		currentMills = cursor.getLong(cursor
				.getColumnIndex(MoodColumns.TIMESTAMP));
		cursor.moveToPrevious();
		if (currentMills <= queryTimeLimit) {
			return true;// 已经有三天没有更新
		} else {
			return false;// 三天之内有更新
		}
	}

	/**
	 * 由标准时间戳得到本地时区下的时间
	 * 
	 * @param timeStamp
	 *            时间戳
	 * @return 标准时间格式字符串
	 */
	public String getMoodItemDate(long timeStamp) {
		// 由数据库中标准时间换算到本地设置时区下的时间
		long unixTime = timeStamp + TimeZone.getDefault().getRawOffset();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("MM-dd HH:mm");
		Date date = new Date(unixTime);
		String moodItemDate = sDateFormat.format(date);
		return moodItemDate;
	}

}