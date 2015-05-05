package com.mytest.utils.pwd;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.NamingException;

public class EncodePwd {

	private static final String DEFAULT_SECURE_KEY = "secure key";

	private static final String PROVIDER_KEY = "Blowfish";

	public static String decodepwd(String pwd) throws Exception {
		return decode(pwd);
	}

	/**
	 * 加密方法
	 *
	 * @param secret
	 * @return
	 * @throws NamingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static String encode(String secret) throws NamingException,
			NoSuchAlgorithmException, InvalidKeyException,
			NoSuchPaddingException, BadPaddingException,
			IllegalBlockSizeException {
		byte[] kbytes = DEFAULT_SECURE_KEY.getBytes();
		SecretKeySpec key = new SecretKeySpec(kbytes, PROVIDER_KEY);
		Cipher cipher;
		cipher = Cipher.getInstance(PROVIDER_KEY);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encoding = cipher.doFinal(secret.getBytes());
		BigInteger n = new BigInteger(encoding);
		return n.toString(16);
	}

	/**
	 * 解密方法
	 *
	 * @param secret
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	private static String decode(String secret) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException {
		byte[] kbytes = DEFAULT_SECURE_KEY.getBytes();
		SecretKeySpec key = new SecretKeySpec(kbytes, PROVIDER_KEY);
		BigInteger n = new BigInteger(secret, 16);
		byte[] encoding = n.toByteArray();
		Cipher cipher = Cipher.getInstance(PROVIDER_KEY);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decode = cipher.doFinal(encoding);
		return new String(decode);
	}

	/**
	 * 产生加密串
	 *
	 * @param args
	 * @throws NamingException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static void main(String[] args) throws NamingException,
			InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, BadPaddingException,
			IllegalBlockSizeException {
		//String secret = "boseeandbokee";
		//String sec1="mbiYP08";//
		if(args!=null && args.length>0){
			String secret = args[0];
			System.out.println("[" + encode(secret) + "]");
		}else{
			System.out.println("Please input a secret!");
		}
		//String s ="2ece347ded357f7a125c218bb9954cc9";//-b0d08402db6l086
		//String ss ="2ece347ded357f7a125c218bb9954cc9";
//		System.out.println("[" + decode("-d49c81772042b72") + "]");
	}
}

