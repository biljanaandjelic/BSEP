package korenski.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Decryption {
	
	public static byte[] decrypt(byte[] cipherText, SecretKey key) {
		//TODO: Desifrovati sifru uz pomoc tajnog kljuca koristeci konfiguraciju AES algoritma koju diktira najbolja praksa
		Cipher decriptCipher;
		
		try {
			System.out.println("::::::KEY "+Base64Utility.encode(key.getEncoded()));
			String initVector="initVectoraaaaa";
			
			
			decriptCipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] iv = new byte[decriptCipher.getBlockSize()];
			IvParameterSpec ivParams= new IvParameterSpec(iv);
			decriptCipher.init(Cipher.DECRYPT_MODE, key,ivParams);
			byte[] plainText=decriptCipher.doFinal(cipherText);
			return plainText;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
