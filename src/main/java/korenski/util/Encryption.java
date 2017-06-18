package korenski.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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
		System.out.println("Generisan kljuc: " + Base64Utility.encode(key.getEncoded()));
		try {
			//Kada se definise sifra potrebno je navesti njenu konfiguraciju, sto u ovom slucaju ukljucuje:
			//	- Algoritam koji se koristi (AES)
			//	- Rezim rada tog algoritma (CBC - Cipher Block Chaining)
			/*
			Koristi se Cipher block chaining rezim rada kako se ne bi
			isti blok uvek sifrovao na isti nacin.
			 */
			//	- Strategija za popunjavanje poslednjeg bloka (PKCS5Padding)
			/*
			Opsta praksa je da inicijalni vektor bude random generisan,
			i posto ne moze biti zloupotrebljen, nije tajan i kao takav
			se konkatenira sa sifrovanim tekstom.
			 */
			
			byte[] iv = new SecureRandom().generateSeed(16);
			
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
	        System.out.println("Generisan inicijalizacioni vektor: " + Base64Utility.encode(ivspec.getIV()));
			Cipher desCipherEnc = Cipher.getInstance("AES/CBC/PKCS5Padding");
			//inicijalizacija za sifrovanje, 
			desCipherEnc.init(Cipher.ENCRYPT_MODE, key, ivspec);
			
			//sifrovanje
			byte[] ciphertext = desCipherEnc.doFinal(plainText.getBytes());
			
			byte[] retVal = new byte[iv.length + ciphertext.length];
	        System.arraycopy(iv, 0, retVal, 0, iv.length);
	        System.arraycopy(ciphertext, 0, retVal, iv.length, ciphertext.length);
			
			return retVal;
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
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return null;
	}
}
