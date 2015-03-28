package com.hlb.fragment;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.hlb.R;
import com.hlb.app.AppConstants;
import com.hlb.slidinglayer.SlidingLayer;
import com.hlb.slidinglayer.SlidingLayer.OnInteractListener;
import com.hlb.switchbtn.SwitchButton;
import com.hlb.utils.MoodPW;

public class LeftFragment extends Fragment implements OnClickListener,
		OnCheckedChangeListener, OnInteractListener {
	private SharedPreferences mPreferences = null;

	private LayoutInflater mInflater;
	private SlidingLayer mSlidingLayer;

	private View mFeedBackView = null;
	private View mAboutView = null;
	private View mExitConfirmView = null;

	private SwitchButton userPasswordSwitchButton;

	private EditText mFeedBackET = null;
	private Button mFeedBackBtn = null;
	private Button mExitAppBtn = null;

	private View mFeedBack = null;
	private View mAbout = null;

	// 主界面中所有Dialog控件启动时的ID
	private static final int EXIT_DIALOG_ID = 1;
	private static final int PW_INITIAL_ID = 2;
	private static final int PW_MODIFIED_ID = 3;
	private static final int PW_CANCEL_ID = 5;

	// 标志常量
	private int pw_error_counts = 0;// 用于记录密码连续输入错误的次数

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(getActivity());

		// 得到用于存储密码的SharedPreferences对象
		mPreferences = LeftFragment.this.getActivity().getSharedPreferences(
				AppConstants.String_Const.LOG_IN, Context.MODE_PRIVATE);

		initFeedBackView();
		initAboutView();
		initExitView();
	}

	// 反馈
	private void initFeedBackView() {
		mFeedBackView = mInflater.inflate(R.layout.feed_back_view, null);

		mFeedBackET = (EditText) mFeedBackView.findViewById(R.id.fee_back_edit);
		mFeedBackBtn = (Button) mFeedBackView.findViewById(R.id.feed_back_btn);
		mFeedBackBtn.setOnClickListener(this);
	}

	// 关于
	private void initAboutView() {
		mAboutView = mInflater.inflate(R.layout.about, null);
		TextView app_information = (TextView) mAboutView
				.findViewById(R.id.app_information);
		String myBlog = String.format("<a href=\"%s\">%s</a>",
				getString(R.string.my_blog_url),
				getString(R.string.my_blog_url));
		app_information.setText(Html.fromHtml(getString(
				R.string.app_information, myBlog)));
		app_information.setMovementMethod(LinkMovementMethod.getInstance());
	}

	// 退出
	private void initExitView() {
		mExitConfirmView = mInflater.inflate(R.layout.exit_app_confirm, null);
		mExitAppBtn = (Button) mExitConfirmView
				.findViewById(R.id.confirm_exit_btn);
		mExitAppBtn.setOnClickListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_left_fragment, container,
				false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// title
		view.findViewById(R.id.ivTitleBtnLeft).setVisibility(View.GONE);
		view.findViewById(R.id.ivTitleBtnRight).setVisibility(View.GONE);
		TextView title = (TextView) view.findViewById(R.id.ivTitleName);
		title.setText(R.string.app_name);

		mSlidingLayer = (SlidingLayer) view
				.findViewById(R.id.right_sliding_layer);

		userPasswordSwitchButton = (SwitchButton) view
				.findViewById(R.id.use_password_switch);
		if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
				AppConstants.String_Const.EMPTY).equals(
				AppConstants.String_Const.EMPTY)) {
			userPasswordSwitchButton.setChecked(false);
		} else {
			userPasswordSwitchButton.setChecked(true);
		}
		mExitAppBtn = (Button) view.findViewById(R.id.exit_app);
		mFeedBack = (View) view.findViewById(R.id.set_feedback);
		mAbout = (View) view.findViewById(R.id.set_about);
		setListener();
	}

	private void setListener() {
		mSlidingLayer.setOnInteractListener(this);

		userPasswordSwitchButton.setOnCheckedChangeListener(this);
		mFeedBackBtn.setOnClickListener(this);
		mExitAppBtn.setOnClickListener(this);

		mFeedBack.setOnClickListener(this);
		mAbout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exit_app:
			mSlidingLayer.removeAllViews();// 先移除所有的view,不然会报错
			if (!mSlidingLayer.isOpened()) {
				mSlidingLayer.addView(mExitConfirmView);
				mSlidingLayer.openLayer(true);
			}
			break;
		case R.id.confirm_exit_btn:
			if (mSlidingLayer.isOpened()) {
				mSlidingLayer.closeLayer(true);
			}
			getActivity().finish();
			break;
		case R.id.feed_back_btn:
			String content = mFeedBackET.getText().toString();
			if (!TextUtils.isEmpty(content)) {
				Intent intent = new Intent(Intent.ACTION_SENDTO);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "记事本-信息反馈");
				intent.putExtra(Intent.EXTRA_TEXT, content);
				intent.setData(Uri.parse("mailto:329145723@qq.com"));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getActivity().startActivity(intent);
				if (mSlidingLayer.isOpened()) {
					mSlidingLayer.closeLayer(true);
				}
			} else {
				Toast.makeText(LeftFragment.this.getActivity(), "请输入反馈信息",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.set_feedback:
			mSlidingLayer.removeAllViews();
			if (!mSlidingLayer.isOpened()) {
				mSlidingLayer.addView(mFeedBackView);
				mSlidingLayer.openLayer(true);
			}
			break;
		case R.id.set_about:
			mSlidingLayer.removeAllViews();
			if (!mSlidingLayer.isOpened()) {
				mSlidingLayer.addView(mAboutView);
				mSlidingLayer.openLayer(true);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.use_password_switch:
			Log.i("haolaibo", "switch button is clicked");
			if (isChecked) {
				// 用户还没有设置密码
				if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
						AppConstants.String_Const.EMPTY).equals(
						AppConstants.String_Const.EMPTY))
					myShowDialog(PW_INITIAL_ID);// 启动设置密码对话框
				// 用于已经设置密码
				else {
					myShowDialog(PW_MODIFIED_ID); // 启动修改密码对话框
				}
			} else {
				// 用户还没有设置密码
				if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
						AppConstants.String_Const.EMPTY).equals(
						AppConstants.String_Const.EMPTY)) {
					// 提示用户，还没设置密码保护功能
					Toast.makeText(LeftFragment.this.getActivity(),
							AppConstants.Notifications.PW_WITHOUT,
							Toast.LENGTH_SHORT).show();
				} else {// 用户已经设置密码
					myShowDialog(PW_CANCEL_ID);// 启动取消密码对话框
				}
			}
			break;
		default:
			break;
		}
	}

	public void myShowDialog(int id) {
		Dialog dialog = null;
		if (id == EXIT_DIALOG_ID) { // 弹出退出确认对话框
			dialog = buildConfirmExitDialog();
		} else if (id == PW_INITIAL_ID) { // 弹出创建设置密码对话框
			dialog = buildInitialPasswordDialog();
		} else if (id == PW_MODIFIED_ID) { // 弹出创建修改密码对话框
			dialog = buildChangePasswordDialog();
		} else if (id == PW_CANCEL_ID) { // 弹出取消密码保护对话框
			dialog = buildCancelPasswordDialog();
		} else {
			return;
		}
		dialog.show();
	}

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
	 * 创建退出程序时弹出对话框
	 * 
	 * @return 返回创建的对话框对象
	 */
	public Dialog buildConfirmExitDialog() {
		// 开始设定AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity()).setTitle(R.string.exit_dialog);
		// 设定PositiveButton按下时的操作
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 退出程序，关闭MoodLogActivity
						LeftFragment.this.getActivity().finish();
					}
				});
		// 设定NegativeButton按下时的操作
		builder.setNegativeButton(R.string.cancel, null);
		// 创建AlertDialog
		AlertDialog exitAlertDialog = builder.create();
		return exitAlertDialog;
	}

	/**
	 * 创建初始设置密码时的对话框
	 * 
	 * @return 初始设置密码对话框对象
	 */
	public Dialog buildInitialPasswordDialog() {
		// 将dlg_setpassword布局实例化，并找到其中的控件
		View localView = LayoutInflater.from(getActivity()).inflate(
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
			if (MoodPW.PWValidityCheck(LeftFragment.this.getActivity(),
					pwInitialStr, pwConfirmStr)) {
				// 验证有效，对密码进行MD5加密
				pwString = MoodPW.encryptmd5(MoodPW.MD5(pwConfirmStr));
				// 将密码存入SharedPreferenced中
				Editor editor = mPreferences.edit();
				editor.putString(AppConstants.String_Const.PASSWORD, pwString);
				editor.commit();
				// 提示密码设置成功
				Toast.makeText(LeftFragment.this.getActivity(),
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
			Log.i("haolaibo", "cancel clicked...");
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY)) {
				userPasswordSwitchButton.setChecked(false);
			} else {
				userPasswordSwitchButton.setChecked(true);
			}
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
			Log.i("haolaibo", "cancel clicked...");
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY)) {
				userPasswordSwitchButton.setChecked(false);
			} else {
				userPasswordSwitchButton.setChecked(true);
			}
			// 关闭对话框
			dialog.dismiss();
		}
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
					if (MoodPW.PWValidityCheck(LeftFragment.this.getActivity(),
							pwNewStr, pwConfirmStr)) {
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
						Toast.makeText(LeftFragment.this.getActivity(),
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
								LeftFragment.this.getActivity(),
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
				Toast.makeText(LeftFragment.this.getActivity(),
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
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY)) {
				userPasswordSwitchButton.setChecked(false);
			} else {
				userPasswordSwitchButton.setChecked(true);
			}
			dialog.dismiss();
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
			Log.i("haolaibo", "cancel clicked...");
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY)) {
				userPasswordSwitchButton.setChecked(false);
			} else {
				userPasswordSwitchButton.setChecked(true);
			}
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
		View localView = LayoutInflater.from(getActivity()).inflate(
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
					Toast.makeText(LeftFragment.this.getActivity(),
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
								LeftFragment.this.getActivity(),
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
							LeftFragment.this.getActivity(),
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
			Log.i("haolaibo", "cancel clicked...");
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY)) {
				userPasswordSwitchButton.setChecked(false);
			} else {
				userPasswordSwitchButton.setChecked(true);
			}
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
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY)) {
				userPasswordSwitchButton.setChecked(false);
			} else {
				userPasswordSwitchButton.setChecked(true);
			}
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
		View localView = LayoutInflater.from(LeftFragment.this.getActivity())
				.inflate(R.layout.dlg_inputpassword, null);
		EditText pwInputText = (EditText) localView
				.findViewById(R.id.input_pw_edittext);

		// 得到取消输入密码功能对话框对象
		AlertDialog localAlertDialog = creatDialog(localView,
				R.string.cancel_pw, new CanclePWPositiveListener(pwInputText),
				new CanclePWNegativeListener(pwInputText),
				new PWCancelListener(pwInputText));

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
		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity()).setView(localView).setTitle(dialogTitle);
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

	@Override
	public void onOpen() {

	}

	@Override
	public void onClose() {
		// mSlidingLayer.removeAllViews();
	}

	@Override
	public void onOpened() {

	}

	@Override
	public void onClosed() {
		mSlidingLayer.removeAllViews();
	}
}
