package korenski.controller.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import korenski.model.util.KeyUtility;
import korenski.repository.dtos.KeyUtilityRepository;
import korenski.util.CustomSecureRandom;
import korenski.util.CustomSecurity;

//import sun.security.util.SecurityConstants;
//import java.lang.Object.org.jumpmind.security.SecurityConstants;
@Controller
public class StartupConfiguration {
	@Autowired
	KeyUtilityRepository keyUtilityRepository;

	@RequestMapping(value = "/initialConfiguration", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<KeyUtility> generateKeyStorePassword(@RequestBody String keyStorePassword,
			@Context HttpServletRequest request) {
		byte[] salt = CustomSecurity.generateSalt();
		byte[] hashedPassword = CustomSecurity.hash(keyStorePassword, salt);
		KeyUtility keyUtility = new KeyUtility();
		keyUtility.setHashedPassword(hashedPassword);
		keyUtility.setSalt(salt);
		keyUtility.setName("password");
		try {
			keyUtility = keyUtilityRepository.save(keyUtility);
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<KeyUtility>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<KeyUtility>(keyUtility, HttpStatus.OK);

	}

	public SecretKey generateSymmetricKey() {
		SecureRandom secureRandomNumber = new SecureRandom();
		Date date = new Date();
		Long seed = date.getTime();
		SecretKey secretKey = null;
		System.out.println("*************************");
		System.out.println("Seed " + seed);
		System.out.println("*************************");
		KeyGenerator keygen;
		secureRandomNumber.setSeed(seed);
		try {
			keygen = KeyGenerator.getInstance("AES");
			keygen.init(128, secureRandomNumber);
			secretKey = keygen.generateKey();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return secretKey;
	}

	@RequestMapping(value = "/tekeSymmetricKey",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.TEXT_PLAIN_VALUE)
	public String takeSymmetricKey(@RequestBody String password) {
		KeyUtility key = keyUtilityRepository.findByName("password");
		byte[] hashedEntry = CustomSecurity.hash(password, key.getSalt());
		String filePath = "./files/AppKeystore.jceks";
		KeyStore ks;
	
		SecretKey secretKey;
		if (Arrays.equals(key.getHashedPassword(), hashedEntry)) {
			SecretKey symmetricKey=null;
			try {
				ks = KeyStore.getInstance("JCEKS");
				//File f = new File(filePath);

			    java.io.FileInputStream fis = null;
			    try {
			        fis = new java.io.FileInputStream("keyStoreName");
			        ks.load(fis, password.toCharArray());
			        ks.load(new FileInputStream(filePath), password.toCharArray());
					KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray());
					try {
						KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) ks.getEntry("secretKeyAlias",
								protParam);
						secretKey = entry.getSecretKey();
						CustomSecureRandom.getInstance().setSecretKey(secretKey);
					} catch (UnrecoverableEntryException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return "WRONG";
					}
			      }catch (Exception e) {
			    	 
			    	 // final char[]      BLANK_PWD=new char[0];
			    	//    SSLContext        ctx=SSLContext.getInstance("TLS");
			    //	    KeyManagerFactory kmf=KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			   // 	    Key               ctfkey=mstkst.getKey(svrctfals,BLANK_PWD);
			    //	    Certificate[]     ctfchn=mstkst.getCertificateChain(svrctfals);
						// TODO: handle exception
			    	  	ks.load(null, null);
						ks.store(new FileOutputStream("./files/AppKeystore.jceks"), password.toCharArray());
						KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password.toCharArray());
						secretKey = generateSymmetricKey();
						if(secretKey==null){
							System.out.println("WRONG key");
						}
						KeyGenerator keyGen = KeyGenerator.getInstance("DES");
						 SecretKey tmpSecretKey = keyGen.generateKey();
					//	KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(secretKey);
						KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(tmpSecretKey); 
						System.out.println("********************121");
						//ks.set
					//	ks.set
						CustomSecureRandom.getInstance().setSecretKey(secretKey);
						ks.setEntry("secretKeyAlias", skEntry, new KeyStore.PasswordProtection(password.toCharArray()));
						System.out.println("********************123");
						
					}
			/*	if (f.exists() && !f.isDirectory()) {
					

				} else {
					

				} */
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

		}
		return "GOOD";
	}

}
