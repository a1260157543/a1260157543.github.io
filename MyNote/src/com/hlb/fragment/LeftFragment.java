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

	// ������������Dialog�ؼ�����ʱ��ID
	private static final int EXIT_DIALOG_ID = 1;
	private static final int PW_INITIAL_ID = 2;
	private static final int PW_MODIFIED_ID = 3;
	private static final int PW_CANCEL_ID = 5;

	// ��־����
	private int pw_error_counts = 0;// ���ڼ�¼���������������Ĵ���

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(getActivity());

		// �õ����ڴ洢�����SharedPreferences����
		mPreferences = LeftFragment.this.getActivity().getSharedPreferences(
				AppConstants.String_Const.LOG_IN, Context.MODE_PRIVATE);

		initFeedBackView();
		initAboutView();
		initExitView();
	}

	// ����
	private void initFeedBackView() {
		mFeedBackView = mInflater.inflate(R.layout.feed_back_view, null);

		mFeedBackET = (EditText) mFeedBackView.findViewById(R.id.fee_back_edit);
		mFeedBackBtn = (Button) mFeedBackView.findViewById(R.id.feed_back_btn);
		mFeedBackBtn.setOnClickListener(this);
	}

	// ����
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

	// �˳�
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
			mSlidingLayer.removeAllViews();// ���Ƴ����е�view,��Ȼ�ᱨ��
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
				intent.putExtra(Intent.EXTRA_SUBJECT, "���±�-��Ϣ����");
				intent.putExtra(Intent.EXTRA_TEXT, content);
				intent.setData(Uri.parse("mailto:329145723@qq.com"));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getActivity().startActivity(intent);
				if (mSlidingLayer.isOpened()) {
					mSlidingLayer.closeLayer(true);
				}
			} else {
				Toast.makeText(LeftFragment.this.getActivity(), "�����뷴����Ϣ",
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
				// �û���û����������
				if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
						AppConstants.String_Const.EMPTY).equals(
						AppConstants.String_Const.EMPTY))
					myShowDialog(PW_INITIAL_ID);// ������������Ի���
				// �����Ѿ���������
				else {
					myShowDialog(PW_MODIFIED_ID); // �����޸�����Ի���
				}
			} else {
				// �û���û����������
				if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
						AppConstants.String_Const.EMPTY).equals(
						AppConstants.String_Const.EMPTY)) {
					// ��ʾ�û�����û�������뱣������
					Toast.makeText(LeftFragment.this.getActivity(),
							AppConstants.Notifications.PW_WITHOUT,
							Toast.LENGTH_SHORT).show();
				} else {// �û��Ѿ���������
					myShowDialog(PW_CANCEL_ID);// ����ȡ������Ի���
				}
			}
			break;
		default:
			break;
		}
	}

	public void myShowDialog(int id) {
		Dialog dialog = null;
		if (id == EXIT_DIALOG_ID) { // �����˳�ȷ�϶Ի���
			dialog = buildConfirmExitDialog();
		} else if (id == PW_INITIAL_ID) { // ����������������Ի���
			dialog = buildInitialPasswordDialog();
		} else if (id == PW_MODIFIED_ID) { // ���������޸�����Ի���
			dialog = buildChangePasswordDialog();
		} else if (id == PW_CANCEL_ID) { // ����ȡ�����뱣���Ի���
			dialog = buildCancelPasswordDialog();
		} else {
			return;
		}
		dialog.show();
	}

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
	 * �����˳�����ʱ�����Ի���
	 * 
	 * @return ���ش����ĶԻ������
	 */
	public Dialog buildConfirmExitDialog() {
		// ��ʼ�趨AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity()).setTitle(R.string.exit_dialog);
		// �趨PositiveButton����ʱ�Ĳ���
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// �˳����򣬹ر�MoodLogActivity
						LeftFragment.this.getActivity().finish();
					}
				});
		// �趨NegativeButton����ʱ�Ĳ���
		builder.setNegativeButton(R.string.cancel, null);
		// ����AlertDialog
		AlertDialog exitAlertDialog = builder.create();
		return exitAlertDialog;
	}

	/**
	 * ������ʼ��������ʱ�ĶԻ���
	 * 
	 * @return ��ʼ��������Ի������
	 */
	public Dialog buildInitialPasswordDialog() {
		// ��dlg_setpassword����ʵ���������ҵ����еĿؼ�
		View localView = LayoutInflater.from(getActivity()).inflate(
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
			if (MoodPW.PWValidityCheck(LeftFragment.this.getActivity(),
					pwInitialStr, pwConfirmStr)) {
				// ��֤��Ч�����������MD5����
				pwString = MoodPW.encryptmd5(MoodPW.MD5(pwConfirmStr));
				// ���������SharedPreferenced��
				Editor editor = mPreferences.edit();
				editor.putString(AppConstants.String_Const.PASSWORD, pwString);
				editor.commit();
				// ��ʾ�������óɹ�
				Toast.makeText(LeftFragment.this.getActivity(),
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
			Log.i("haolaibo", "cancel clicked...");
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY)) {
				userPasswordSwitchButton.setChecked(false);
			} else {
				userPasswordSwitchButton.setChecked(true);
			}
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
			Log.i("haolaibo", "cancel clicked...");
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY)) {
				userPasswordSwitchButton.setChecked(false);
			} else {
				userPasswordSwitchButton.setChecked(true);
			}
			// �رնԻ���
			dialog.dismiss();
		}
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
					if (MoodPW.PWValidityCheck(LeftFragment.this.getActivity(),
							pwNewStr, pwConfirmStr)) {
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
						Toast.makeText(LeftFragment.this.getActivity(),
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
								LeftFragment.this.getActivity(),
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
				Toast.makeText(LeftFragment.this.getActivity(),
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
	 * �����޸�����Ի���
	 * 
	 * @return �޸�����Ի������
	 */
	public Dialog buildChangePasswordDialog() {
		// ��dlg_setpassword����ʵ���������ҵ����еĿؼ�
		View localView = LayoutInflater.from(getActivity()).inflate(
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
					Toast.makeText(LeftFragment.this.getActivity(),
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
								LeftFragment.this.getActivity(),
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
							LeftFragment.this.getActivity(),
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
			Log.i("haolaibo", "cancel clicked...");
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY)) {
				userPasswordSwitchButton.setChecked(false);
			} else {
				userPasswordSwitchButton.setChecked(true);
			}
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
			if (mPreferences.getString(AppConstants.String_Const.PASSWORD,
					AppConstants.String_Const.EMPTY).equals(
					AppConstants.String_Const.EMPTY)) {
				userPasswordSwitchButton.setChecked(false);
			} else {
				userPasswordSwitchButton.setChecked(true);
			}
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
		View localView = LayoutInflater.from(LeftFragment.this.getActivity())
				.inflate(R.layout.dlg_inputpassword, null);
		EditText pwInputText = (EditText) localView
				.findViewById(R.id.input_pw_edittext);

		// �õ�ȡ���������빦�ܶԻ������
		AlertDialog localAlertDialog = creatDialog(localView,
				R.string.cancel_pw, new CanclePWPositiveListener(pwInputText),
				new CanclePWNegativeListener(pwInputText),
				new PWCancelListener(pwInputText));

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
		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity()).setView(localView).setTitle(dialogTitle);
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
