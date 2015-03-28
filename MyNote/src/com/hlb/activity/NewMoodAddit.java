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
 * NewMoodAddit * 功能：添加新的心情记录。用户输入心情内容，选择心情图片并为心情打分。 *
 *************************************************************************************/

public class NewMoodAddit extends Activity {
	// 心情等级常量
	private static final float perfect = (float) 5.0;
	private static final float good = (float) 4.0;
	private static final float fine = (float) 3.0;
	private static final float bad = (float) 2.0;
	private static final float terrible = (float) 1.0;
	// 控件对象的定义
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
			TransitionEffect.Accordion, };// 表情翻页效果
	private List<String> keys = null;
	// 当输入没有完成时Activity的实例被停止，保存输入状态
	private int icon_id = 0;
	private Uri contentUri = null;

	// 转向来源标志
	// 来源标志：0，记事列表；1，查看记事
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
		// 得到数据库表格中的Uri
		contentUri = intent.getData();

		if (contentUri.getPath().equals(
				"/" + MoodLogProvider.MOODLOG_TABLE_NAME)) {
			// 从记事列表转向过来的
			fromFlag = FROM_NOTE_LIST;
		} else {
			// 从查看记事转向过来的
			fromFlag = FROM_NOTE_VIEW;
			setViewContent();
		}
	}

	/**
	 * 如果从查看记事转向过来，则直接显示相应的记事内容
	 */
	@SuppressWarnings("deprecation")
	private void setViewContent() {
		// 通过Uri在数据库中查询，得到查询结果集cursor对象
		cursor = managedQuery(contentUri, AppConstants.PROJECTION, null, null,
				null);
		// 找出选中条目的内容，并在当前Activity中显示
		if (cursor != null) {
			cursor.moveToNext();
			_id = cursor.getInt(cursor
					.getColumnIndex(AppConstants.MoodColumns._ID));
			// 得到心情内容
			moodItem = cursor.getString(cursor
					.getColumnIndex(AppConstants.MoodColumns.MOODITEM));
			// 得到心情记录的时间戳
			timeStamp = cursor.getLong(cursor
					.getColumnIndex(AppConstants.MoodColumns.TIMESTAMP));
			// 得到该条心情的评分
			icon_id = cursor.getInt(cursor
					.getColumnIndex(AppConstants.MoodColumns.ICON_ID));
		} else {// 查询结果为空,结束当前Activity
			finish();
		}
		// 由时间戳得到心情记录时间的字符串
		// String moodItemDate = Utils.getMoodItemDate(timeStamp);
		// 在控件中显示心情内容
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
	 * 得到用户在MoodIconGrid中选择的心情图片的对应ID
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle localBundle = data.getExtras();
			icon_id = localBundle.getInt(String_Const.MOOD_ICON);
			// 缺省状态下的图片ID
			if (icon_id == 0)
				icon_id = AppConstants.DEFAULT_ICON_ID;
			moodIcon.setImageResource(icon_id);
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * 控件初始化
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
	 * 保存按钮监听器
	 */
	class SaveButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// 保存心情
			saveMood();
		}
	}

	/**
	 * 放弃按钮监听器
	 */
	class DiscardButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// 取消添加新心情
			cancelMood();
		}
	}

	// 防止乱pageview乱滚动
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
	 * 获取表情图片的GridView
	 * 
	 * @param i
	 * @return
	 */
	private GridView getGridView(int i) {
		// TODO Auto-generated method stub
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
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
				if (arg2 == MyApplication.NUM) {// 删除键的位置
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
					// 注释的部分，在EditText中显示字符串
					// String ori = msgEt.getText().toString();
					// int index = msgEt.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(count));
					// msgEt.setText(stringBuilder.toString());
					// msgEt.setSelection(index + keys.get(count).length());

					// 下面这部分，在EditText中显示表情
					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), (Integer) MyApplication
									.getInstance().getFaceMap().values()
									.toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 40;
						int newWidth = 40;
						// 计算缩放因子
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// 新建立矩阵
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// 设置图片的旋转角度
						// matrix.postRotate(-30);
						// 设置图片的倾斜
						// matrix.postSkew(0.1f, 0.1f);
						// 将图片大小压缩
						// 压缩后图片的宽和高以及kB大小均会变化
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
	 * 初始化表情滑动页
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
	 * 选择图片按钮监听器
	 */
	class IconButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// 启动MoodIconGrid Activity
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
	 * RatingBar监听器
	 */
	class RatingBarListener implements RatingBar.OnRatingBarChangeListener {

		@Override
		public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
			if (fromUser) {
				// 设置心情标签的内容
				setMoodRatingWord(rating);
			}
		}
	}

	/**
	 * 设置心情得分字体
	 * 
	 * @param rating
	 *            改变的RatingBar得分
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
	 * 将NewMoodAddit Actvity中用户输入的内容保存至数据库中，并成功返回
	 */
	private final void saveMood() {
		// 得到EditText中输入的文本内容
		String moodItem = editText.getText().toString();
		int length = moodItem.length();
		// 检查输入心情长度的有效性
		if (false == checkLength(length)) {
			return;
		}
		Long timeStamp = 0L;
		if (fromFlag == FROM_NOTE_LIST) {
			// 得到标准的格林尼治时间戳
			timeStamp = Long.valueOf(getUnixTimeGMT());
		} else if (fromFlag == FROM_NOTE_VIEW) {
			timeStamp = this.timeStamp;
		}
		// 得到当前RatingBar中的分数
		Float rating = Float.valueOf(ratingBar.getRating());
		// 心情分数最低为1分
		if (rating < (float) 1.0)
			rating = (float) 1.0;
		// 创建装载数据的ContentValues
		ContentValues values = new ContentValues();
		// 向values中添加数据,用于存入数据库中
		values.put(MoodColumns.TIMESTAMP, timeStamp);
		values.put(MoodColumns.RATING, rating);
		values.put(MoodColumns.MOODITEM, moodItem);
		// 确保用户不选择图片的情况下使用缺省的表情图片
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
		// 将values写入数据库中
		if (fromFlag == FROM_NOTE_LIST) {
			getContentResolver().insert(contentUri, values);
		} else if (fromFlag == FROM_NOTE_VIEW) {
			getContentResolver().update(contentUri, values,
					MoodColumns._ID + "=?", new String[] { _id + "" });
		}
		// 结束当前Activity
		finish();
	}

	/**
	 * 取消添加新的心情，直接返回上一级的Activity
	 */
	private final void cancelMood() {
		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * 得到标准格林尼治时间戳
	 * 
	 * @return Unix时间戳
	 */
	public long getUnixTimeGMT() {
		// 获取当前日历对象
		Calendar calendar = Calendar.getInstance();
		// 获取当前时区下日期时间对应的时间戳
		long unixTime = calendar.getTimeInMillis();
		// 获取标准格林尼治时间下日期时间对应的时间戳
		long unixTimeGMT = unixTime - TimeZone.getDefault().getRawOffset();
		return unixTimeGMT;
	}

	/**
	 * 检查输入内容长度的有效性，不能超过70个汉字
	 * 
	 * @param length
	 *            输入心情内容的长度
	 * @return true:长度有效 false:长度无效
	 */
	public Boolean checkLength(int length) {
		if (length == 0) { // 没有输入内容
			Toast.makeText(this, R.string.nothing_to_save, Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (length > AppConstants.MOOD_MAX_LENGTH) {// 内容长度超出有效范围
			Toast.makeText(this, R.string.mood_length_invalid,
					Toast.LENGTH_SHORT).show();
			return false;
		} else
			return true;
	}
}
