package com.hlb.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hlb.app.MyApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

public class Utils {

	public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

	public static CharSequence convertNormalStringToSpannableString(Context mContext,
			String message) {
		// TODO Auto-generated method stub
		String hackTxt;
		if (message.startsWith("[") && message.endsWith("]")) {
			hackTxt = message + " ";
		} else {
			hackTxt = message;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);

		Matcher localMatcher = EMOTION_URL.matcher(value);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			// k = str2.lastIndexOf("[");
			// Log.i("way", "str2.length = "+str2.length()+", k = " + k);
			// str2 = str2.substring(k, m);
			if (m - k < 8) {
				if (MyApplication.getInstance().getFaceMap().containsKey(str2)) {
					int face = MyApplication.getInstance().getFaceMap()
							.get(str2);
					Bitmap bitmap = BitmapFactory.decodeResource(
							mContext.getResources(), face);
					if (bitmap != null) {
						ImageSpan localImageSpan = new ImageSpan(mContext,
								bitmap, ImageSpan.ALIGN_BASELINE);
						value.setSpan(localImageSpan, k, m,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		return value;
	}

	/**
	 * 由时间戳得到标准格式的时间
	 * 
	 * @param timeStamp
	 *            时间戳
	 * @return 标准格式时间的字符串
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getMoodItemDate(long timeStamp) {
		// 由数据库中标准时间换算到本地设置时区下的时间
		long unixTime = timeStamp + TimeZone.getDefault().getRawOffset();
		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date(unixTime);
		String moodItemDate = sDateFormat.format(date);
		return moodItemDate;
	}
}
