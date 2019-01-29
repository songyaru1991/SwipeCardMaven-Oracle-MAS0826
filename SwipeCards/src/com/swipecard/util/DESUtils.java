package com.swipecard.util;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESUtils {

	private static Key key;

	private static String KEY_STR = "myKeyRyanCai";// 密钥

	private static String CHARSETNAME = "UTF-8";// 编码

	private static String ALGORITHM = "DES";// 加密类型

	static {

		try {

			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);

			generator.init(new SecureRandom(KEY_STR.getBytes()));

			key = generator.generateKey();

			generator = null;

		} catch (Exception e) {

			throw new RuntimeException(e);

		}

	}

	/**
	 * 
	 * 对str进行DES加密
	 * 
	 * 
	 * 
	 * @param str
	 * 
	 * @return
	 * 
	 */

	public static String getEncryptString(String str) {

		BASE64Encoder base64encoder = new BASE64Encoder();

		try {

			byte[] bytes = str.getBytes(CHARSETNAME);

			Cipher cipher = Cipher.getInstance(ALGORITHM);

			cipher.init(Cipher.ENCRYPT_MODE, key);

			byte[] doFinal = cipher.doFinal(bytes);

			return base64encoder.encode(doFinal);

		} catch (Exception e) {

			throw new RuntimeException(e);

		}

	}

	/**
	 * 
	 * 对str进行DES解密
	 * 
	 * 
	 * 
	 * @param str
	 * 
	 * @return
	 * 
	 */

	public static String getDecryptString(String str) {

		BASE64Decoder base64decoder = new BASE64Decoder();

		try {

			byte[] bytes = base64decoder.decodeBuffer(str);

			Cipher cipher = Cipher.getInstance(ALGORITHM);

			cipher.init(Cipher.DECRYPT_MODE, key);

			byte[] doFinal = cipher.doFinal(bytes);

			return new String(doFinal, CHARSETNAME);

		} catch (Exception e) {

			throw new RuntimeException(e);

		}

	}

	public static void main(String[] args) {

//		String cod = getEncryptString("usc");
//		String cod2 = getEncryptString("mis_sc");
//		String cod1 = getDecryptString("SwSYTqKMoSs=");
//		System.out.println(cod+"----"+cod2);
			
		String cod = getEncryptString("swipe");
		String cod2 = getEncryptString("mis_realtime");
		System.out.println(cod+"----"+cod2);
	}

}
