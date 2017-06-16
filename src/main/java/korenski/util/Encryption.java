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

public class Encryption {

	public static  byte[] encrypt(String plainText, SecretKey key) {
		//TODO: Sifrovati otvoren tekst uz pomoc tajnog kljuca koristeci konfiguraciju AES algoritma koju diktira najbolja praksa
		Cipher encriptCipher;
		try {
			System.out.println("::::::KEY "+Base64Utility.encode(key.getEncoded()));
			String initVector="initVectoraaaaaa";
			
			encriptCipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] iv = new byte[encriptCipher.getBlockSize()];
			IvParameterSpec ivParams= new IvParameterSpec(iv);
			
			encriptCipher.init(Cipher.ENCRYPT_MODE, key,ivParams);
			byte[] cipherText=encriptCipher.doFinal(plainText.getBytes());
			return cipherText;
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
		}  catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
}
