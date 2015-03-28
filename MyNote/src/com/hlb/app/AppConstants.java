package com.hlb.app;

import com.hlb.R;

import android.net.Uri;
import android.provider.BaseColumns;

/**********************************************************************************************
 * AppConstants * ����: �����࣬�洢�������õ����ַ��������ͳ��� *
 **********************************************************************************************/

public final class AppConstants {
	public static final class String_Const {
		public static final String CONTEXT_MENU_HEADER = "����";
		public static final String MOOD_ICON = "mood_icon";
		public static final String PASSWORD = "password";
		public static final String LOG_IN = "loginvalue";
		public static final String EMPTY = "empty";
	}

	// ֪ͨ���ݵ��ַ���
	public static final class Notifications {
		public static final String APART_A_LONG_TIME = "�ף��þ�û������д�½���������~";
		public static final String NO_MOOD = " ��~~���¼�½���ĺ������O(��_��)O~";
		public static final String DATEPICKERERROR = "�����ˡ�������ʼ���ڲ��ܴ��ڽ�������Ŷ������";
		public static final String NO_RECORDS = "�ף����»�û�м�¼����Ŷ~~~";
		public static final String DATEPICKERWRONG = "�ף�����û��δ���ļ�¼Ŷ...";
		public static final String DATEEARLY = "Sorry��ʱ��̫���ˣ���һ�����黹û������...";
		public static final String PW_ERROR = "��������������󣡣�";
		public static final String PW_DIFFERENT = "����������벻ͬ������������";
		public static final String PW_EMPTY = "���ã���������벻��Ϊ��!";
		public static final String PW_SETTING_SUCCESS = "�������óɹ�!";
		public static final String PW_SUCCESS = "����������ȷ!";
		public static final String PW_CANCEL = " �ɹ�ȡ�����뱣�� ";
		public static final String PW_WITHOUT = "�Բ�������û�п������뱣������";
		public static final String PW_CHANGE_SUCCESS = "�����޸ĳɹ�";
	}

	// �����������ݵĳ�������
	public static final int MOOD_MAX_LENGTH = 1024;

	// provider��AUTHORITY
	public static final String AUTHORITY = "com.provider.moodlog";

	// Ĭ�ϵ��������ͼƬa122��ID
	public static final int DEFAULT_ICON_ID = R.drawable.f021;

	// �����ݿ��в�ѯʱ��Ҫ����
	public static final String[] PROJECTION = new String[] { MoodColumns._ID, // 0
			MoodColumns.MOODITEM, // 1
			MoodColumns.RATING, // 2
			MoodColumns.ICON_ID, // 3
			MoodColumns.TIMESTAMP, // 4
	};

	// ͳ�Ʒ��������в�ѯ���ݿ�ʱ��Ҫ����
	public static final String[] CHART_PROJECTION = new String[] {
			MoodColumns._ID, // 0
			MoodColumns.RATING, // 1
	};

	/**
	 * �̳�BaseColumns���������������ݿ�ʱ��Ҫ�ĳ���
	 */
	public static final class MoodColumns implements BaseColumns {
		// This class cannot be instantiated
		private MoodColumns() {
		}

		/**
		 * ���ݿ�MoodLogTable���Uri
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
		 * ȱʡ�µ�����˳��---����ID�Ľ�������
		 */
		public static final String DEFAULT_SORT_ORDER = _ID + " DESC";

		/**
		 * ȱʡ״̬����MoodLogListView����ʾ��������Ŀ
		 */
		public static final String DEFAULT_LIMIT = " limit 10 ";

		/**
		 * MOODITEM����
		 * <P>
		 * ����: TEXT
		 * </P>
		 */
		public static final String MOODITEM = "moodItem";

		/**
		 * �����������
		 * <P>
		 * ����: FLOAT
		 * </P>
		 */
		public static final String RATING = "rating";

		/**
		 * ����ͼƬ����
		 * <P>
		 * ����: int
		 * </P>
		 */
		public static final String ICON_ID = "icon_id";

		/**
		 * ��¼����ʱ�������(Unixʱ���)
		 * <p>
		 * ����: Long
		 * <p>
		 */
		public static final String TIMESTAMP = "datetime";

	}
}
