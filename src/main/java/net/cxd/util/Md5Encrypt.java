
package net.cxd.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** 
* 
* 
* 说明：密码加密模块 
* key:公共密钥
* 密码md5成16位字符串然后加32位字符串进行混合加密
* */
public class Md5Encrypt {
	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * 密钥，公共加密配合verfycode
	 */
	public static final String key="0e7201cd040fa7292c9102caga55dae3acc91c90465dbeccddc81gd7bg405f67";
	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param text
	 *            明文
	 * 
	 * @return 密文
	 */
	public static String md5(String text) {
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"System doesn't support MD5 algorithm.");
		}

		try {
			msgDigest.update(text.getBytes("utf-8"));    //注意改接口是按照指定编码形式签名
 
		} catch (UnsupportedEncodingException e) {

			throw new IllegalStateException(
					"System doesn't support your  EncodingException.");

		}

		byte[] bytes = msgDigest.digest();

		String md5Str = new String(encodeHex(bytes));

		return md5Str;
	}
	
	/**
	 * 由16位密码混合key进行md5
	 * @param pwd
	 * @return
	 */
	public static String appendKey(String pwd){
		return pwd+key;
	}
	
	
	/**
	 * 十六进制转换
	 * */
	private static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

}