package com.hlb.app;

import com.hlb.R;

import android.net.Uri;
import android.provider.BaseColumns;

/**********************************************************************************************
 * AppConstants * 功能: 工具类，存储程序中用到的字符串等类型常量 *
 **********************************************************************************************/

public final class AppConstants {
	public static final class String_Const {
		public static final String CONTEXT_MENU_HEADER = "设置";
		public static final String MOOD_ICON = "mood_icon";
		public static final String PASSWORD = "password";
		public static final String LOG_IN = "loginvalue";
		public static final String EMPTY = "empty";
	}

	// 通知内容的字符串
	public static final class Notifications {
		public static final String APART_A_LONG_TIME = "亲，好久没见，快写下今天的心情吧~";
		public static final String NO_MOOD = " 亲~~快记录下今天的好心情吧O(∩_∩)O~";
		public static final String DATEPICKERERROR = "出错了。。。起始日期不能大于结束日期哦。。。";
		public static final String NO_RECORDS = "亲，本月还没有记录心情哦~~~";
		public static final String DATEPICKERWRONG = "亲，这里没有未来的记录哦...";
		public static final String DATEEARLY = "Sorry，时间太早了，第一条心情还没出生呢...";
		public static final String PW_ERROR = "您输入的密码有误！！";
		public static final String PW_DIFFERENT = "您输入的密码不同，请重新输入";
		public static final String PW_EMPTY = "您好，输入的密码不能为空!";
		public static final String PW_SETTING_SUCCESS = "密码设置成功!";
		public static final String PW_SUCCESS = "密码输入正确!";
		public static final String PW_CANCEL = " 成功取消密码保护 ";
		public static final String PW_WITHOUT = "对不起，您还没有开启密码保护功能";
		public static final String PW_CHANGE_SUCCESS = "密码修改成功";
	}

	// 输入心情内容的长度限制
	public static final int MOOD_MAX_LENGTH = 1024;

	// provider的AUTHORITY
	public static final String AUTHORITY = "com.provider.moodlog";

	// 默认的心情表情图片a122的ID
	public static final int DEFAULT_ICON_ID = R.drawable.f021;

	// 在数据库中查询时需要的列
	public static final String[] PROJECTION = new String[] { MoodColumns._ID, // 0
			MoodColumns.MOODITEM, // 1
			MoodColumns.RATING, // 2
			MoodColumns.ICON_ID, // 3
			MoodColumns.TIMESTAMP, // 4
	};

	// 统计分析功能中查询数据库时需要的列
	public static final String[] CHART_PROJECTION = new String[] {
			MoodColumns._ID, // 0
			MoodColumns.RATING, // 1
	};

	/**
	 * 继承BaseColumns，创建及操作数据库时需要的常量
	 */
	public static final class MoodColumns implements BaseColumns {
		// This class cannot be instantiated
		private MoodColumns() {
		}

		/**
		 * 数据库MoodLogTable表的Uri
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/MoodLogTable");

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of
		 * MoodLogTable
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.moodlog";

		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * mood in MoodLogTable.
		 */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.moodlog";

		/**
		 * 缺省下的排序顺序---按照ID的降序排列
		 */
		public static final String DEFAULT_SORT_ORDER = _ID + " DESC";

		/**
		 * 缺省状态下在MoodLogListView中显示的心情数目
		 */
		public static final String DEFAULT_LIMIT = " limit 10 ";

		/**
		 * MOODITEM列名
		 * <P>
		 * 类型: TEXT
		 * </P>
		 */
		public static final String MOODITEM = "moodItem";

		/**
		 * 心情分数列名
		 * <P>
		 * 类型: FLOAT
		 * </P>
		 */
		public static final String RATING = "rating";

		/**
		 * 心情图片列名
		 * <P>
		 * 类型: int
		 * </P>
		 */
		public static final String ICON_ID = "icon_id";

		/**
		 * 记录心情时间的列名(Unix时间戳)
		 * <p>
		 * 类型: Long
		 * <p>
		 */
		public static final String TIMESTAMP = "datetime";

	}
}
