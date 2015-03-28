package com.hlb.app;

import java.util.Calendar;
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.hlb.app.AppConstants.MoodColumns;

/*********************************************************************************************
 * MoodLogProvider * 功能: 继承ContentProvider，创建数据库并对数据库进行管理。 *
 *********************************************************************************************/

public class MoodLogProvider extends ContentProvider {
	// 数据库名
	private static final String DATABASE_NAME = "MoodLogDB.db";
	// 表名
	public static final String MOODLOG_TABLE_NAME = "MoodLogTable";
	// 版本号
	private static final int DATABASE_VERSION = 1;
	// 用于Uri匹配
	private static final int MOODLOG_TABLE = 1;
	private static final int MOODLOG_ID = 2;
	// 默认的心情得分
	private static final float DEFAULT_RATING = (float) 3.0;
	// UriMatcher对象
	private static UriMatcher sUriMatcher = null;
	// HashMap对象
	private static HashMap<String, String> sMoodProjectionMap = null;

	static {
		// 静态配置Uri匹配器
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AppConstants.AUTHORITY, "MoodLogTable",
				MOODLOG_TABLE);
		sUriMatcher
				.addURI(AppConstants.AUTHORITY, "MoodLogTable/#", MOODLOG_ID);

		// sMoodProjectionMap这个私有字段是用来在上层应用使用的字段和底层数据库字段之间建立映射关系的，
		sMoodProjectionMap = new HashMap<String, String>();
		sMoodProjectionMap.put(MoodColumns._ID, MoodColumns._ID);
		sMoodProjectionMap.put(MoodColumns.MOODITEM, MoodColumns.MOODITEM);
		sMoodProjectionMap.put(MoodColumns.RATING, MoodColumns.RATING);
		sMoodProjectionMap.put(MoodColumns.ICON_ID, MoodColumns.ICON_ID);
		sMoodProjectionMap.put(MoodColumns.TIMESTAMP, MoodColumns.TIMESTAMP);

	}

	/**
	 * 构造SQLiteOpenHelper助手类，用于创建数据库MoodLog.db
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// 执行SQL语句，创建数据库
			db.execSQL("CREATE TABLE " + MOODLOG_TABLE_NAME + " ("
					+ MoodColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ MoodColumns.MOODITEM + " TEXT," + MoodColumns.RATING
					+ " FLOAT," + MoodColumns.ICON_ID + " INT,"
					+ MoodColumns.TIMESTAMP + " LONG" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS MoodLogTable");
			onCreate(db);
		}
	}

	// SQliteDateBase助手类对象
	private DatabaseHelper moodLogOpenHelper;

	@Override
	public boolean onCreate() {
		// 助手类对象实例化
		moodLogOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	/**
	 * Uri匹配函数
	 */
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		// 指向整个表
		case MOODLOG_TABLE:
			return MoodColumns.CONTENT_TYPE;
			// 指向表中某一行
		case MOODLOG_ID:
			return MoodColumns.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	/**
	 * 实现向数据库中插入一行的功能
	 */
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// 验证Uri的有效性
		if (sUriMatcher.match(uri) != MOODLOG_TABLE) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			// 复制ContentValues对象
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		// 得到当前的时间戳
		Long timeStamp = Long.valueOf(getUnixTime());
		// 确保一行中所有域都在ContentValue中设置好
		if (values.containsKey(MoodColumns.TIMESTAMP) == false) {
			values.put(MoodColumns.TIMESTAMP, timeStamp);
		}

		if (values.containsKey(MoodColumns.MOODITEM) == false) {
			values.put(MoodColumns.MOODITEM, "");
		}

		if (values.containsKey(MoodColumns.ICON_ID) == false) {
			values.put(MoodColumns.MOODITEM, AppConstants.DEFAULT_ICON_ID);
		}

		if (values.containsKey(MoodColumns.RATING) == false) {
			values.put(MoodColumns.RATING, DEFAULT_RATING);
		}

		// 得到可写的数据库实例
		SQLiteDatabase db = moodLogOpenHelper.getWritableDatabase();

		// 向数据库中插入一行
		long rowId = db.insert(MOODLOG_TABLE_NAME, null, values);

		if (rowId > 0) {
			Uri moodUri = ContentUris.withAppendedId(MoodColumns.CONTENT_URI,
					rowId);
			// 通知上层：其传递下来的URI在底层数据库中已经发生了变化。
			getContext().getContentResolver().notifyChange(moodUri, null);
			return moodUri;
		}
		// 没有成功插入一行，抛出异常
		throw new SQLException("Failed to insert row into " + uri);
	}

	/**
	 * 实现删除数据库中某一行记录的功能
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = moodLogOpenHelper.getWritableDatabase();
		// The number of rows affected
		int count;
		// 匹配传入的Uri
		switch (sUriMatcher.match(uri)) {
		case MOODLOG_TABLE: // 指向整个数据表
			count = db.delete(MOODLOG_TABLE_NAME, selection, selectionArgs);
			break;

		case MOODLOG_ID: // 指向数据表中某一行
			String moodId = uri.getPathSegments().get(1);
			// 删除操作
			count = db.delete(MOODLOG_TABLE_NAME, MoodColumns._ID
					+ "="
					+ moodId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		// 通知上层：其传递下来的URI在底层数据库中已经发生了变化。
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/**
	 * 实现数据库查询功能，返回查询结果集(Cursor对象)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// 查询助手类
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(MOODLOG_TABLE_NAME);
		// 匹配Uri
		switch (sUriMatcher.match(uri)) {
		case MOODLOG_TABLE:
			qb.setProjectionMap(sMoodProjectionMap);
			break;

		case MOODLOG_ID:
			qb.setProjectionMap(sMoodProjectionMap);
			qb.appendWhere(MoodColumns._ID + "=" + uri.getPathSegments().get(1));
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// 设置查询结果的排序方法
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = MoodColumns.DEFAULT_SORT_ORDER; // 设置为默认排序方法
		} else {
			orderBy = sortOrder;
		}

		// 查询操作
		SQLiteDatabase db = moodLogOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);

		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	/**
	 * 更改数据库内容，在本程序中不需要对数据内容更新，并没有复写该方法。
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// 验证Uri的有效性
//		if (sUriMatcher.match(uri) != MOODLOG_TABLE) {
//			throw new IllegalArgumentException("Unknown URI " + uri);
//		}

		// 得到当前的时间戳
		Long timeStamp = Long.valueOf(getUnixTime());
		// 确保一行中所有域都在ContentValue中设置好
		if (values.containsKey(MoodColumns.TIMESTAMP) == false) {
			values.put(MoodColumns.TIMESTAMP, timeStamp);
		}

		if (values.containsKey(MoodColumns.MOODITEM) == false) {
			values.put(MoodColumns.MOODITEM, "");
		}

		if (values.containsKey(MoodColumns.ICON_ID) == false) {
			values.put(MoodColumns.MOODITEM, AppConstants.DEFAULT_ICON_ID);
		}

		if (values.containsKey(MoodColumns.RATING) == false) {
			values.put(MoodColumns.RATING, DEFAULT_RATING);
		}

		// 得到可写的数据库实例
		SQLiteDatabase db = moodLogOpenHelper.getWritableDatabase();

		// 向数据库中插入一行
		long rowId = db.update(MOODLOG_TABLE_NAME, values, selection,
				selectionArgs);

		if (rowId > 0) {
			Uri moodUri = ContentUris.withAppendedId(MoodColumns.CONTENT_URI,
					rowId);
			// 通知上层：其传递下来的URI在底层数据库中已经发生了变化。
			getContext().getContentResolver().notifyChange(moodUri, null);
			return 1;
		}
		// 没有成功插入一行，抛出异常
		throw new SQLException("Failed to insert row into " + uri);
	}

	/**
	 * 获取当前时间戳
	 * 
	 * @return Unix时间戳
	 */
	public long getUnixTime() {
		// 获取当前日历对象
		Calendar calendar = Calendar.getInstance();
		// 获取当前时区下日期时间对应的时间戳
		long unixTime = calendar.getTimeInMillis();
		// 获取标准格林尼治时间下日期时间对应的时间戳
		// long unixTimeGMT = unixTime - TimeZone.getDefault().getRawOffset();
		return unixTime;
	}
}