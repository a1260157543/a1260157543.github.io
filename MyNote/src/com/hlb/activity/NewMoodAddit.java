package com.hlb.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.BufferType;

import com.hlb.adapter.FaceAdapter;
import com.hlb.adapter.FacePageAdeapter;
import com.hlb.app.AppConstants;
import com.hlb.app.MoodLogProvider;
import com.hlb.app.MyApplication;
import com.hlb.app.AppConstants.MoodColumns;
import com.hlb.app.AppConstants.String_Const;
import com.hlb.face.CirclePageIndicator;
import com.hlb.face.JazzyViewPager;
import com.hlb.face.JazzyViewPager.TransitionEffect;
import com.hlb.utils.Utils;
import com.hlb.R;

/**************************************************************************************
 * NewMoodAddit * ���ܣ�����µ������¼���û������������ݣ�ѡ������ͼƬ��Ϊ�����֡� *
 *************************************************************************************/

public class NewMoodAddit extends Activity {
	// ����ȼ�����
	private static final float perfect = (float) 5.0;
	private static final float good = (float) 4.0;
	private static final float fine = (float) 3.0;
	private static final float bad = (float) 2.0;
	private static final float terrible = (float) 1.0;
	// �ؼ�����Ķ���
	private ImageButton saveButton = null;
	private ImageButton discardButton = null;
	private EditText editText = null;
	private RatingBar ratingBar = null;
	private ImageButton moodIcon = null;
	private ImageView moodRatingWord = null;

	private LinearLayout faceLinearLayout = null;
	private JazzyViewPager faceViewPager = null;

	private boolean isFaceShow = false;
	private int currentPage = 0;
	private TransitionEffect mEffects[] = { TransitionEffect.Standard,
			TransitionEffect.Tablet, TransitionEffect.CubeIn,
			TransitionEffect.CubeOut, TransitionEffect.FlipVertical,
			TransitionEffect.FlipHorizontal, TransitionEffect.Stack,
			TransitionEffect.ZoomIn, TransitionEffect.ZoomOut,
			TransitionEffect.RotateUp, TransitionEffect.RotateDown,
			TransitionEffect.Accordion, };// ���鷭ҳЧ��
	private List<String> keys = null;
	// ������û�����ʱActivity��ʵ����ֹͣ����������״̬
	private int icon_id = 0;
	private Uri contentUri = null;

	// ת����Դ��־
	// ��Դ��־��0�������б�1���鿴����
	private static final int FROM_NOTE_LIST = 0;
	private static final int FROM_NOTE_VIEW = 1;
	private int fromFlag = FROM_NOTE_LIST;

	private Cursor cursor = null;
	private int _id = 0;
	private String moodItem = null;
	private Long timeStamp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mood_addit);
		setView();
		Intent intent = getIntent();
		// �õ����ݿ����е�Uri
		contentUri = intent.getData();

		if (contentUri.getPath().equals(
				"/" + MoodLogProvider.MOODLOG_TABLE_NAME)) {
			// �Ӽ����б�ת�������
			fromFlag = FROM_NOTE_LIST;
		} else {
			// �Ӳ鿴����ת�������
			fromFlag = FROM_NOTE_VIEW;
			setViewContent();
		}
	}

	/**
	 * ����Ӳ鿴����ת���������ֱ����ʾ��Ӧ�ļ�������
	 */
	@SuppressWarnings("deprecation")
	private void setViewContent() {
		// ͨ��Uri�����ݿ��в�ѯ���õ���ѯ�����cursor����
		cursor = managedQuery(contentUri, AppConstants.PROJECTION, null, null,
				null);
		// �ҳ�ѡ����Ŀ�����ݣ����ڵ�ǰActivity����ʾ
		if (cursor != null) {
			cursor.moveToNext();
			_id = cursor.getInt(cursor
					.getColumnIndex(AppConstants.MoodColumns._ID));
			// �õ���������
			moodItem = cursor.getString(cursor
					.getColumnIndex(AppConstants.MoodColumns.MOODITEM));
			// �õ������¼��ʱ���
			timeStamp = cursor.getLong(cursor
					.getColumnIndex(AppConstants.MoodColumns.TIMESTAMP));
			// �õ��������������
			icon_id = cursor.getInt(cursor
					.getColumnIndex(AppConstants.MoodColumns.ICON_ID));
		} else {// ��ѯ���Ϊ��,������ǰActivity
			finish();
		}
		// ��ʱ����õ������¼ʱ����ַ���
		// String moodItemDate = Utils.getMoodItemDate(timeStamp);
		// �ڿؼ�����ʾ��������
		editText.setText(Utils.convertNormalStringToSpannableString(
				NewMoodAddit.this, moodItem), BufferType.SPANNABLE);
		moodRatingWord.setImageResource(icon_id);
		switch (icon_id) {
		case R.drawable.perfect:
			ratingBar.setRating((float) 5.0);
			break;
		case R.drawable.good:
			ratingBar.setRating((float) 4.0);
			break;
		case R.drawable.fine:
			ratingBar.setRating((float) 3.0);
			break;
		case R.drawable.bad:
			ratingBar.setRating((float) 2.0);
			break;
		case R.drawable.terrible:
			ratingBar.setRating((float) 1.0);
			break;
		default:
			break;
		}
		// moodIconView.setImageResource(icon_id);
	}

	/**
	 * �õ��û���MoodIconGrid��ѡ�������ͼƬ�Ķ�ӦID
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle localBundle = data.getExtras();
			icon_id = localBundle.getInt(String_Const.MOOD_ICON);
			// ȱʡ״̬�µ�ͼƬID
			if (icon_id == 0)
				icon_id = AppConstants.DEFAULT_ICON_ID;
			moodIcon.setImageResource(icon_id);
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * �ؼ���ʼ��
	 */
	public void setView() {
		saveButton = (ImageButton) findViewById(R.id.additSaveButton);
		discardButton = (ImageButton) findViewById(R.id.additDiscardButton);
		editText = (EditText) findViewById(R.id.additText);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		moodIcon = (ImageButton) findViewById(R.id.moodIconButton);
		moodIcon.getBackground().setAlpha(0);
		saveButton.getBackground().setAlpha(0);
		discardButton.getBackground().setAlpha(0);
		moodRatingWord = (ImageView) findViewById(R.id.mood_word);
		saveButton.setOnClickListener(new SaveButtonListener());
		discardButton.setOnClickListener(new DiscardButtonListener());
		moodIcon.setOnClickListener(new IconButtonListener());
		ratingBar.setOnRatingBarChangeListener(new RatingBarListener());

		faceLinearLayout = (LinearLayout) findViewById(R.id.face_ll);
		faceViewPager = (JazzyViewPager) findViewById(R.id.face_pager);

		initData();
		initFacePage();
	}

	/**
	 * ���水ť������
	 */
	class SaveButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// ��������
			saveMood();
		}
	}

	/**
	 * ������ť������
	 */
	class DiscardButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// ȡ�����������
			cancelMood();
		}
	}

	// ��ֹ��pageview�ҹ���
	private OnTouchListener forbidenScroll() {
		return new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		};
	}

	/**
	 * ��ȡ����ͼƬ��GridView
	 * 
	 * @param i
	 * @return
	 */
	private GridView getGridView(int i) {
		// TODO Auto-generated method stub
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// ����GridViewĬ�ϵ��Ч��
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(1);
		gv.setVerticalScrollBarEnabled(false);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(this, i));
		gv.setOnTouchListener(forbidenScroll());
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == MyApplication.NUM) {// ɾ������λ��
					int selection = editText.getSelectionStart();
					String text = editText.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							editText.getText().delete(start, end);
							return;
						}
						editText.getText().delete(selection - 1, selection);
					}
				} else {
					int count = currentPage * MyApplication.NUM + arg2;
					// ע�͵Ĳ��֣���EditText����ʾ�ַ���
					// String ori = msgEt.getText().toString();
					// int index = msgEt.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(count));
					// msgEt.setText(stringBuilder.toString());
					// msgEt.setSelection(index + keys.get(count).length());

					// �����ⲿ�֣���EditText����ʾ����
					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), (Integer) MyApplication
									.getInstance().getFaceMap().values()
									.toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 40;
						int newWidth = 40;
						// ������������
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// �½�������
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// ����ͼƬ����ת�Ƕ�
						// matrix.postRotate(-30);
						// ����ͼƬ����б
						// matrix.postSkew(0.1f, 0.1f);
						// ��ͼƬ��Сѹ��
						// ѹ����ͼƬ�Ŀ�͸��Լ�kB��С����仯
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(NewMoodAddit.this,
								newBitmap);
						String emojiStr = keys.get(count);
						SpannableString spannableString = new SpannableString(
								emojiStr);
						spannableString.setSpan(imageSpan,
								emojiStr.indexOf('['),
								emojiStr.indexOf(']') + 1,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						editText.append(spannableString);
					} else {
						String ori = editText.getText().toString();
						int index = editText.getSelectionStart();
						StringBuilder stringBuilder = new StringBuilder(ori);
						stringBuilder.insert(index, keys.get(count));
						editText.setText(stringBuilder.toString());
						editText.setSelection(index + keys.get(count).length());
					}
				}
			}
		});
		return gv;
	}

	private void initData() {
		Set<String> keySet = MyApplication.getInstance().getFaceMap().keySet();
		keys = new ArrayList<String>();
		keys.addAll(keySet);
	}

	/**
	 * ��ʼ�����黬��ҳ
	 */
	private void initFacePage() {
		// TODO Auto-generated method stub
		List<View> lv = new ArrayList<View>();
		for (int i = 0; i < MyApplication.NUM_PAGE; ++i) {
			lv.add(getGridView(i));
		}
		FacePageAdeapter adapter = new FacePageAdeapter(lv, faceViewPager);
		faceViewPager.setAdapter(adapter);
		faceViewPager.setCurrentItem(currentPage);
		faceViewPager.setTransitionEffect(mEffects[(int) Math.random()
				% mEffects.length]);
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(faceViewPager);
		adapter.notifyDataSetChanged();
		faceLinearLayout.setVisibility(View.GONE);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 < 0) {
					currentPage = MyApplication.NUM_PAGE;
				} else if (arg0 > MyApplication.NUM_PAGE) {
					currentPage = 0;
				} else {
					currentPage = arg0;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// do nothing
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// do nothing
			}
		});

	}

	/**
	 * ѡ��ͼƬ��ť������
	 */
	class IconButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// ����MoodIconGrid Activity
			// Intent intent = new Intent();
			// intent.setClass(NewMoodAddit.this, MoodIconGrid.class);
			// NewMoodAddit.this.startActivityForResult(intent,100);
			if (!isFaceShow) {
				faceLinearLayout.setVisibility(View.VISIBLE);
				isFaceShow = true;
			} else {
				faceLinearLayout.setVisibility(View.GONE);
				isFaceShow = false;
			}
		}
	}

	/**
	 * RatingBar������
	 */
	class RatingBarListener implements RatingBar.OnRatingBarChangeListener {

		@Override
		public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
			if (fromUser) {
				// ���������ǩ������
				setMoodRatingWord(rating);
			}
		}
	}

	/**
	 * ��������÷�����
	 * 
	 * @param rating
	 *            �ı��RatingBar�÷�
	 */
	public void setMoodRatingWord(float rating) {
		if (rating == perfect)
			moodRatingWord.setImageResource(R.drawable.perfect);
		else if (rating == good)
			moodRatingWord.setImageResource(R.drawable.good);
		else if (rating == fine)
			moodRatingWord.setImageResource(R.drawable.fine);
		else if (rating == bad)
			moodRatingWord.setImageResource(R.drawable.bad);
		else if (rating == terrible)
			moodRatingWord.setImageResource(R.drawable.terrible);
		else
			System.out.println("Error in Rating Star");
	}

	/**
	 * ��NewMoodAddit Actvity���û���������ݱ��������ݿ��У����ɹ�����
	 */
	private final void saveMood() {
		// �õ�EditText��������ı�����
		String moodItem = editText.getText().toString();
		int length = moodItem.length();
		// ����������鳤�ȵ���Ч��
		if (false == checkLength(length)) {
			return;
		}
		Long timeStamp = 0L;
		if (fromFlag == FROM_NOTE_LIST) {
			// �õ���׼�ĸ�������ʱ���
			timeStamp = Long.valueOf(getUnixTimeGMT());
		} else if (fromFlag == FROM_NOTE_VIEW) {
			timeStamp = this.timeStamp;
		}
		// �õ���ǰRatingBar�еķ���
		Float rating = Float.valueOf(ratingBar.getRating());
		// ����������Ϊ1��
		if (rating < (float) 1.0)
			rating = (float) 1.0;
		// ����װ�����ݵ�ContentValues
		ContentValues values = new ContentValues();
		// ��values���������,���ڴ������ݿ���
		values.put(MoodColumns.TIMESTAMP, timeStamp);
		values.put(MoodColumns.RATING, rating);
		values.put(MoodColumns.MOODITEM, moodItem);
		// ȷ���û���ѡ��ͼƬ�������ʹ��ȱʡ�ı���ͼƬ
		if (rating == perfect)
			icon_id = R.drawable.perfect;
		else if (rating == good)
			icon_id = R.drawable.good;
		else if (rating == fine)
			icon_id = R.drawable.fine;
		else if (rating == bad)
			icon_id = R.drawable.bad;
		else if (rating == terrible)
			icon_id = R.drawable.terrible;
		else
			icon_id = R.drawable.fine;
		values.put(MoodColumns.ICON_ID, icon_id);
		// ��valuesд�����ݿ���
		if (fromFlag == FROM_NOTE_LIST) {
			getContentResolver().insert(contentUri, values);
		} else if (fromFlag == FROM_NOTE_VIEW) {
			getContentResolver().update(contentUri, values,
					MoodColumns._ID + "=?", new String[] { _id + "" });
		}
		// ������ǰActivity
		finish();
	}

	/**
	 * ȡ������µ����飬ֱ�ӷ�����һ����Activity
	 */
	private final void cancelMood() {
		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * �õ���׼��������ʱ���
	 * 
	 * @return Unixʱ���
	 */
	public long getUnixTimeGMT() {
		// ��ȡ��ǰ��������
		Calendar calendar = Calendar.getInstance();
		// ��ȡ��ǰʱ��������ʱ���Ӧ��ʱ���
		long unixTime = calendar.getTimeInMillis();
		// ��ȡ��׼��������ʱ��������ʱ���Ӧ��ʱ���
		long unixTimeGMT = unixTime - TimeZone.getDefault().getRawOffset();
		return unixTimeGMT;
	}

	/**
	 * ����������ݳ��ȵ���Ч�ԣ����ܳ���70������
	 * 
	 * @param length
	 *            �����������ݵĳ���
	 * @return true:������Ч false:������Ч
	 */
	public Boolean checkLength(int length) {
		if (length == 0) { // û����������
			Toast.makeText(this, R.string.nothing_to_save, Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (length > AppConstants.MOOD_MAX_LENGTH) {// ���ݳ��ȳ�����Ч��Χ
			Toast.makeText(this, R.string.mood_length_invalid,
					Toast.LENGTH_SHORT).show();
			return false;
		} else
			return true;
	}
}
