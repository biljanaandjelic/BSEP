package korenski.util;

import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Set;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;

import korenski.model.util.KeyUtility;
import korenski.repository.dtos.KeyUtilityRepository;

public class CustomSecureRandom {

	@Autowired
	KeyUtilityRepository keyUtilityRepository;
	private KeyStore ks;
	private static CustomSecureRandom customSecureRadnom;
//	private static SecureRandom secureRandomNumber;
	private static SecretKey secretKey;
	//private static String password;
	
	public CustomSecureRandom(){
		
	}
	
	public static CustomSecureRandom getInstance(){
		if(customSecureRadnom==null){
			System.out.println("*********************CUSTOM SECURE RANDOM**********8");
			SecureRandom secureRandomNumber = new SecureRandom();
			Date date = new Date();
			Long seed = date.getTime();
			//SecretKey secretKey = null;
			System.out.println("*************************");
			System.out.println("Seed " + seed);
			System.out.println("*************************");
			KeyGenerator keygen;
			customSecureRadnom=new CustomSecureRandom();
			try {
				keygen = KeyGenerator.getInstance("AES");
				keygen.init(128, secureRandomNumber);
				secretKey = keygen.generateKey();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return customSecureRadnom;
	}
	
	
	
	public SecretKey getSecretKey(){
		return secretKey;
	}

	public KeyUtilityRepository getKeyUtilityRepository() {
		return keyUtilityRepository;
	}

	public void setKeyUtilityRepository(KeyUtilityRepository keyUtilityRepository) {
		this.keyUtilityRepository = keyUtilityRepository;
	}

	public KeyStore getKs() {
		return ks;
	}

	public void setKs(KeyStore ks) {
		this.ks = ks;
	}

	public static CustomSecureRandom getCustomSecureRadnom() {
		return customSecureRadnom;
	}

	public static void setCustomSecureRadnom(CustomSecureRandom customSecureRadnom) {
		CustomSecureRandom.customSecureRadnom = customSecureRadnom;
	}

	

	public static void setSecretKey(SecretKey secretKey) {
		CustomSecureRandom.secretKey = secretKey;
	}
	
	
}
