package com.hlb.utils;

import java.security.MessageDigest;

import com.hlb.app.AppConstants;

import android.content.Context;
import android.widget.Toast;

/**********************************************************************************************
 * MoodPW 																					  * 
 * ����: ��MoodLogActivity�д������޸ġ�ȡ�����뱣��ʱ�Ĺ����ࡣ								  *
 **********************************************************************************************/

public class MoodPW {

	// MD5���ܣ�32λ
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	// ����ļ����㷨
	public static String encryptmd5(String str) {
		char[] a = str.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 'l');
		}
		String s = new String(a);
		return s;
	}

	/**
	 * ��֤����������Ƿ���ȷ
	 * 
	 * @param paramContext
	 * @param paramIn
	 * @return true:��ȷ false:����ȷ
	 */
	public static boolean verifyPW(Context paramContext, String paramIn) {
		String MD5_Str = MD5(paramIn);
		String encryptMD5_Str = encryptmd5(MD5_Str);
		// ��������ַ�������MD5�任��ͬSharedPreferences�д洢��������бȽ�,��֤�����������Ч��
		return paramContext
				.getSharedPreferences(AppConstants.String_Const.LOG_IN,
						Context.MODE_PRIVATE)
				.getString(AppConstants.String_Const.PASSWORD, "0")
				.equals(encryptMD5_Str);
	}

	/**
	 * �������������ȷ���������Ч��
	 * 
	 * @param context
	 *            ������
	 * @param paramIn
	 *            ���������
	 * @param paramConfirm
	 *            �����ȷ������
	 * @return true:��Ч    false:��Ч
	 */
	public static boolean PWValidityCheck(Context context, String paramIn,
			String paramConfirm) {
		//���������Ƿ񶼲�Ϊ��
		if ((paramIn.length() != 0 && paramConfirm.length() != 0)
				&& paramIn.equals(paramConfirm))
			return true;
		else if (paramIn.length() == 0 || paramConfirm.length() == 0) {
			//��ʾ���벻��Ϊ��
			Toast.makeText(context, AppConstants.Notifications.PW_EMPTY,
					Toast.LENGTH_SHORT).show();
			return false;
		} else {
			//��ʾ������������벻ͬ
			Toast.makeText(context, AppConstants.Notifications.PW_DIFFERENT,
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}
}