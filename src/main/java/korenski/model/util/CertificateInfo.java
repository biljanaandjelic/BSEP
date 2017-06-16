package korenski.model.util;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import korenski.model.infrastruktura.Bank;
import korenski.util.Base64Utility;
import korenski.util.CustomSecureRandom;
import korenski.util.Decryption;
import korenski.util.Encryption;



@Entity
@Table(name="certificateID", uniqueConstraints={@UniqueConstraint(columnNames={"serialNumber","bank"})})
public class CertificateInfo {
	public enum Type{
		NationalBank, Bank, Company
	}
	public enum CertStatus { GOOD,REVOKED, UNKNOWN };  
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="serialNumber", unique=true)
	private BigInteger serialNumber;
	
	@Column(nullable=false)
	private CertStatus status;
	
	private Date dateOfRevocation;
	
	@JoinColumn(name="ca")
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	private CertificateInfo ca;
	@Column(nullable=false)
	private Type type;

	@JoinColumn(name="bank")
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	private Bank bank;
	
	@Column(nullable=false)
	private String certificateName;
	
	private String caKeyStoreName;
	
	private byte[] caKeyStorePassword;
	
	private String keyStorName;
	
	private byte[] keyStorePassword;
	
	public CertificateInfo(){
		
	}

	public CertificateInfo( BigInteger serialNumber, CertStatus status, Date dateOfRevocation, CertificateInfo idOfCA,  Type type, String certificateName) {
		super();
		
		this.serialNumber = serialNumber;
		this.status = status;
		this.dateOfRevocation = dateOfRevocation;
		this.ca = idOfCA;
		this.type=type;
		this.certificateName=certificateName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigInteger getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(BigInteger serialNumber) {
		this.serialNumber = serialNumber;
	}

	public CertStatus getStatus() {
		return status;
	}

	public void setStatus(CertStatus status) {
		this.status = status;
	}

	public Date getDateOfRevocation() {
		return dateOfRevocation;
	}

	public void setDateOfRevocation(Date dateOfRevocation2) {
		this.dateOfRevocation = dateOfRevocation2;
	}

	public CertificateInfo getIdOfCA() {
		return ca;
	}

	public void setIdOfCA(CertificateInfo ca) {
		this.ca = ca;
	}

	public CertificateInfo getCa() {
		return ca;
	}

	public void setCa(CertificateInfo ca) {
		this.ca = ca;
	}



	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public String getCaKeyStoreName() {
		return caKeyStoreName;
	}

	public void setCaKeyStoreName(String caKeyStoreName) {
		this.caKeyStoreName = caKeyStoreName;
	}

	public byte[] getCaKeyStorePassword() {
		return caKeyStorePassword;
	}

	public void setCaKeyStorePassword(byte[] caKeyStorePassword) {
		this.caKeyStorePassword = caKeyStorePassword;
	}

	public String getKeyStorName() {
		return keyStorName;
	}

	public void setKeyStorName(String keyStorName) {
		this.keyStorName = keyStorName;
	}

	public byte[] getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(byte[] keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}
	
	public void advancedSetCaKeyStorePassword(String password){
		// SecureRandom random=CustomSecureRandom.getInstance().getSecureRadnom();
		 SecretKey secretKey=CustomSecureRandom.getInstance().getSecretKey();
		 // KeyGenerator keygen;
	//	try {
		
			 System.out.println("_______________________________________________");
			 System.out.println("Plain password "+password);
			byte[] cipeherText=Encryption.encrypt(password, secretKey);
			System.out.println("Kriptovan password  "+Base64Utility.encode(cipeherText));
			 System.out.println("_______________________________________________");
			setCaKeyStorePassword(cipeherText);
	//	} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
	}
	/**
	 * Kriptovanje passworda za keystore u kom se nalazi 
	 * @param password
	 */
	public void advancedSetKeyStorePassword(String password){
		// SecureRandom random=CustomSecureRandom.getInstance().getSecureRadnom();
		 SecretKey secretKey=CustomSecureRandom.getInstance().getSecretKey();
//		try {
//			keygen = KeyGenerator.getInstance("AES");
//			 keygen.init(128, random);
//			 SecretKey secretKey = keygen.generateKey();
			 System.out.println("_______________________________________________");
			 System.out.println("Plain password "+password);
			byte[] cipeherText=Encryption.encrypt(password, secretKey);
			System.out.println("Kriptovan password  "+Base64Utility.encode(cipeherText));
			 System.out.println("_______________________________________________");
			setKeyStorePassword(cipeherText);
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	public String advancedGetCaKeyStorePassword(){
		// SecureRandom random=CustomSecureRandom.getInstance().getSecureRadnom();
		 SecretKey secretKey=CustomSecureRandom.getInstance().getSecretKey();
	//	try {
			// keygen = KeyGenerator.getInstance("AES");
			// keygen.init(128, random);
			// SecretKey secretKey = keygen.generateKey();
			 System.out.println("_______________________________________________");
			
			 byte[] cipherPassword=getCaKeyStorePassword();
			 System.out.println("Plain password "+Base64Utility.encode(cipherPassword));
			//byte[] cipeherText=Encryption.encrypt(password, secretKey);
			 byte[] plainPassword=Decryption.decrypt(cipherPassword, secretKey); 
			 String password=Base64Utility.encode(plainPassword);
			System.out.println("Kriptovan password  "+password);
			 System.out.println("_______________________________________________");
			 return password;
		//}catch(Exception e){
		//	e.printStackTrace();
		///	return "";
		//}
		
	}
	
	public String advancedGetKeyStorePassword(){
		// SecureRandom random=CustomSecureRandom.getInstance().getSecureRadnom();
		 SecretKey secretKey=CustomSecureRandom.getInstance().getSecretKey();
	//	try {
		//	keygen = KeyGenerator.getInstance("AES");
		//	 keygen.init(128, random);
			 //secretKey = keygen.generateKey();
			 System.out.println("_______________________________________________");
			
			 byte[] cipherPassword=getKeyStorePassword();
			 System.out.println("Cipher  password "+Base64Utility.encode(cipherPassword));
			//byte[] cipeherText=Encryption.encrypt(password, secretKey);
			 byte[] plainPassword=Decryption.decrypt(cipherPassword, secretKey); 
			 String password=Base64Utility.encode(plainPassword);
			System.out.println("Plain password  "+password);
			 System.out.println("_______________________________________________");
			 return password;
		//}catch(Exception e){
		//	e.printStackTrace();
		//	return "";
		//}
	}

}