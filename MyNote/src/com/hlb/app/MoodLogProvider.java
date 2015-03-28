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
 * MoodLogProvider * ����: �̳�ContentProvider���������ݿⲢ�����ݿ���й��� *
 *********************************************************************************************/

public class MoodLogProvider extends ContentProvider {
	// ���ݿ���
	private static final String DATABASE_NAME = "MoodLogDB.db";
	// ����
	public static final String MOODLOG_TABLE_NAME = "MoodLogTable";
	// �汾��
	private static final int DATABASE_VERSION = 1;
	// ����Uriƥ��
	private static final int MOODLOG_TABLE = 1;
	private static final int MOODLOG_ID = 2;
	// Ĭ�ϵ�����÷�
	private static final float DEFAULT_RATING = (float) 3.0;
	// UriMatcher����
	private static UriMatcher sUriMatcher = null;
	// HashMap����
	private static HashMap<String, String> sMoodProjectionMap = null;

	static {
		// ��̬����Uriƥ����
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AppConstants.AUTHORITY, "MoodLogTable",
				MOODLOG_TABLE);
		sUriMatcher
				.addURI(AppConstants.AUTHORITY, "MoodLogTable/#", MOODLOG_ID);

		// sMoodProjectionMap���˽���ֶ����������ϲ�Ӧ��ʹ�õ��ֶκ͵ײ����ݿ��ֶ�֮�佨��ӳ���ϵ�ģ�
		sMoodProjectionMap = new HashMap<String, String>();
		sMoodProjectionMap.put(MoodColumns._ID, MoodColumns._ID);
		sMoodProjectionMap.put(MoodColumns.MOODITEM, MoodColumns.MOODITEM);
		sMoodProjectionMap.put(MoodColumns.RATING, MoodColumns.RATING);
		sMoodProjectionMap.put(MoodColumns.ICON_ID, MoodColumns.ICON_ID);
		sMoodProjectionMap.put(MoodColumns.TIMESTAMP, MoodColumns.TIMESTAMP);

	}

	/**
	 * ����SQLiteOpenHelper�����࣬���ڴ������ݿ�MoodLog.db
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// ִ��SQL��䣬�������ݿ�
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

	// SQliteDateBase���������
	private DatabaseHelper moodLogOpenHelper;

	@Override
	public boolean onCreate() {
		// ���������ʵ����
		moodLogOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	/**
	 * Uriƥ�亯��
	 */
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		// ָ��������
		case MOODLOG_TABLE:
			return MoodColumns.CONTENT_TYPE;
			// ָ�����ĳһ��
		case MOODLOG_ID:
			return MoodColumns.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	/**
	 * ʵ�������ݿ��в���һ�еĹ���
	 */
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// ��֤Uri����Ч��
		if (sUriMatcher.match(uri) != MOODLOG_TABLE) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			// ����ContentValues����
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		// �õ���ǰ��ʱ���
		Long timeStamp = Long.valueOf(getUnixTime());
		// ȷ��һ������������ContentValue�����ú�
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

		// �õ���д�����ݿ�ʵ��
		SQLiteDatabase db = moodLogOpenHelper.getWritableDatabase();

		// �����ݿ��в���һ��
		long rowId = db.insert(MOODLOG_TABLE_NAME, null, values);

		if (rowId > 0) {
			Uri moodUri = ContentUris.withAppendedId(MoodColumns.CONTENT_URI,
					rowId);
			// ֪ͨ�ϲ㣺�䴫��������URI�ڵײ����ݿ����Ѿ������˱仯��
			getContext().getContentResolver().notifyChange(moodUri, null);
			return moodUri;
		}
		// û�гɹ�����һ�У��׳��쳣
		throw new SQLException("Failed to insert row into " + uri);
	}

	/**
	 * ʵ��ɾ�����ݿ���ĳһ�м�¼�Ĺ���
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = moodLogOpenHelper.getWritableDatabase();
		// The number of rows affected
		int count;
		// ƥ�䴫���Uri
		switch (sUriMatcher.match(uri)) {
		case MOODLOG_TABLE: // ָ���������ݱ�
			count = db.delete(MOODLOG_TABLE_NAME, selection, selectionArgs);
			break;

		case MOODLOG_ID: // ָ�����ݱ���ĳһ��
			String moodId = uri.getPathSegments().get(1);
			// ɾ������
			count = db.delete(MOODLOG_TABLE_NAME, MoodColumns._ID
					+ "="
					+ moodId
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		// ֪ͨ�ϲ㣺�䴫��������URI�ڵײ����ݿ����Ѿ������˱仯��
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/**
	 * ʵ�����ݿ��ѯ���ܣ����ز�ѯ�����(Cursor����)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// ��ѯ������
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(MOODLOG_TABLE_NAME);
		// ƥ��Uri
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

		// ���ò�ѯ��������򷽷�
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = MoodColumns.DEFAULT_SORT_ORDER; // ����ΪĬ�����򷽷�
		} else {
			orderBy = sortOrder;
		}

		// ��ѯ����
		SQLiteDatabase db = moodLogOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);

		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	/**
	 * �������ݿ����ݣ��ڱ������в���Ҫ���������ݸ��£���û�и�д�÷�����
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// ��֤Uri����Ч��
//		if (sUriMatcher.match(uri) != MOODLOG_TABLE) {
//			throw new IllegalArgumentException("Unknown URI " + uri);
//		}

		// �õ���ǰ��ʱ���
		Long timeStamp = Long.valueOf(getUnixTime());
		// ȷ��һ������������ContentValue�����ú�
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

		// �õ���д�����ݿ�ʵ��
		SQLiteDatabase db = moodLogOpenHelper.getWritableDatabase();

		// �����ݿ��в���һ��
		long rowId = db.update(MOODLOG_TABLE_NAME, values, selection,
				selectionArgs);

		if (rowId > 0) {
			Uri moodUri = ContentUris.withAppendedId(MoodColumns.CONTENT_URI,
					rowId);
			// ֪ͨ�ϲ㣺�䴫��������URI�ڵײ����ݿ����Ѿ������˱仯��
			getContext().getContentResolver().notifyChange(moodUri, null);
			return 1;
		}
		// û�гɹ�����һ�У��׳��쳣
		throw new SQLException("Failed to insert row into " + uri);
	}

	/**
	 * ��ȡ��ǰʱ���
	 * 
	 * @return Unixʱ���
	 */
	public long getUnixTime() {
		// ��ȡ��ǰ��������
		Calendar calendar = Calendar.getInstance();
		// ��ȡ��ǰʱ��������ʱ���Ӧ��ʱ���
		long unixTime = calendar.getTimeInMillis();
		// ��ȡ��׼��������ʱ��������ʱ���Ӧ��ʱ���
		// long unixTimeGMT = unixTime - TimeZone.getDefault().getRawOffset();
		return unixTime;
	}
}