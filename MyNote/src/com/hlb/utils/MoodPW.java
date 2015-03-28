package com.hlb.utils;

import java.security.MessageDigest;

import com.hlb.app.AppConstants;

import android.content.Context;
import android.widget.Toast;

/**********************************************************************************************
 * MoodPW 																					  * 
 * 功能: 在MoodLogActivity中创建、修改、取消密码保护时的工具类。								  *
 **********************************************************************************************/

public class MoodPW {

	// MD5加密，32位
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

	// 可逆的加密算法
	public static String encryptmd5(String str) {
		char[] a = str.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 'l');
		}
		String s = new String(a);
		return s;
	}

	/**
	 * 验证输入的密码是否正确
	 * 
	 * @param paramContext
	 * @param paramIn
	 * @return true:正确 false:不正确
	 */
	public static boolean verifyPW(Context paramContext, String paramIn) {
		String MD5_Str = MD5(paramIn);
		String encryptMD5_Str = encryptmd5(MD5_Str);
		// 将传入的字符串经过MD5变换后同SharedPreferences中存储的密码进行比较,验证输入密码的有效性
		return paramContext
				.getSharedPreferences(AppConstants.String_Const.LOG_IN,
						Context.MODE_PRIVATE)
				.getString(AppConstants.String_Const.PASSWORD, "0")
				.equals(encryptMD5_Str);
	}

	/**
	 * 检查输入的密码和确认密码的有效性
	 * 
	 * @param context
	 *            上下文
	 * @param paramIn
	 *            输入的密码
	 * @param paramConfirm
	 *            输入的确认密码
	 * @return true:有效    false:无效
	 */
	public static boolean PWValidityCheck(Context context, String paramIn,
			String paramConfirm) {
		//输入密码是否都不为空
		if ((paramIn.length() != 0 && paramConfirm.length() != 0)
				&& paramIn.equals(paramConfirm))
			return true;
		else if (paramIn.length() == 0 || paramConfirm.length() == 0) {
			//提示密码不能为空
			Toast.makeText(context, AppConstants.Notifications.PW_EMPTY,
					Toast.LENGTH_SHORT).show();
			return false;
		} else {
			//提示两次输入的密码不同
			Toast.makeText(context, AppConstants.Notifications.PW_DIFFERENT,
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}
}