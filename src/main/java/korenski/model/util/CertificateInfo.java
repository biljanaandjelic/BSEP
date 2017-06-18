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
@Table(name = "certificateID", uniqueConstraints = { @UniqueConstraint(columnNames = { "serialNumber", "bank" }) })
public class CertificateInfo {
	public enum Type {
		NationalBank, Bank, Company
	}

	public enum CertStatus {
		GOOD, REVOKED, UNKNOWN
	};

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "serialNumber", unique = true)
	private BigInteger serialNumber;

	@Column(nullable = false)
	private CertStatus status;

	private Date dateOfRevocation;

	@JoinColumn(name = "ca")
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private CertificateInfo ca;
	@Column(nullable = false)
	private Type type;

	@JoinColumn(name = "bank")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Bank bank;

	@Column(nullable = false)
	private String certificateName;

	private String caKeyStoreName;

	private byte[] caKeyStorePassword;

	private String certAlias;

	private String keyStorName;

	private byte[] keyStorePassword;

	private String keyAlias;

	public CertificateInfo() {

	}

	public CertificateInfo(BigInteger serialNumber, CertStatus status, Date dateOfRevocation, CertificateInfo idOfCA,
			Type type, String certificateName) {
		super();

		this.serialNumber = serialNumber;
		this.status = status;
		this.dateOfRevocation = dateOfRevocation;
		this.ca = idOfCA;
		this.type = type;
		this.certificateName = certificateName;
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

	public void advancedSetCaKeyStorePassword(String password) {

		SecretKey secretKey = CustomSecureRandom.getInstance().getSecretKey();

		
		byte[] cipeherText = Encryption.encrypt(password, secretKey);
		
		setCaKeyStorePassword(cipeherText);

	}

	/**
	 * Kriptovanje passworda za keystore u kom se nalazi
	 * privatni kljuc
	 * 
	 * @param password
	 */
	public void advancedSetKeyStorePassword(String password) {

		SecretKey secretKey = CustomSecureRandom.getInstance().getSecretKey();

		
		byte[] cipeherText = Encryption.encrypt(password, secretKey);
		
		setKeyStorePassword(cipeherText);

	}
	/**
	* Preuzimanje passworda iz baze koji je byte[], da bi se mogao iskoristiti
	 * za otvaranje keystore-a neophodno je dekriptovati i dobiti string
	 * 
	 * @return
	 */
	public String advancedGetCaKeyStorePassword() {

		SecretKey secretKey = CustomSecureRandom.getInstance().getSecretKey();

		byte[] cipherPassword = getCaKeyStorePassword();
		

		byte[] plainPassword = Decryption.decrypt(cipherPassword, secretKey);
		
		String password = new String(plainPassword);
		
		return password;

	}

	/**
	 * Preuzimanje passworda iz baze koji je byte[], da bi se mogao iskoristiti
	 * za otvaranje keystore-a neophodno je dekriptovati i dobiti string
	 * 
	 * @return
	 */
	public String advancedGetKeyStorePassword() {

		SecretKey secretKey = CustomSecureRandom.getInstance().getSecretKey();

		byte[] cipherPassword = getKeyStorePassword();

		byte[] plainPassword = Decryption.decrypt(cipherPassword, secretKey);

		String password = new String(plainPassword);

		return password;

	}

	public String getCertAlias() {
		return certAlias;
	}

	public void setCertAlias(String certAlias) {
		this.certAlias = certAlias;
	}

	public String getKeyAlias() {
		return keyAlias;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

}