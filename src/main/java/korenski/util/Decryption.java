package korenski.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
		System.out.println("Generisan kljuc: " + Base64Utility.encode(key.getEncoded()));
	// System.out.println("Cipher  password "+Base64Utility.encode(key));
		try { 
			//algoritam MORA biti isti kao i kod sifrovanja, provider moze da se razlikuje
			Cipher desCipherDec = Cipher.getInstance("AES/CBC/PKCS5Padding");
			//inicijalizacija za dekriptovanje
			/*
			Preuzimaju se prvih 16 bajta sifrovanog teksta koji predstavljaju inicijalizacioni
			vektor koji predstavlja ulaz u aes.
			*/
			byte[] iv = (byte[])Arrays.copyOfRange(cipherText, 0, 16);
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
	        System.out.println("Preuzet inicijalizacioni vektor: " + Base64Utility.encode(ivspec.getIV()));
			
			desCipherDec.init(Cipher.DECRYPT_MODE, key, ivspec);
			
			byte[] realCipherText = (byte[])Arrays.copyOfRange(cipherText, 16, cipherText.length); 
			
			//desifrovanje
			byte[] plainText = desCipherDec.doFinal(realCipherText);
			return plainText;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e){
			e.printStackTrace();
		}
		return null;
	}
}
