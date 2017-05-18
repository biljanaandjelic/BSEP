package korenski.ocsp;

import java.math.BigDecimal;

public class CertID {
	enum HashAlgorithm {
		SHA256WithRSAEncryption, SHA1withRSAencryption
	}

	private static HashAlgorithm hashAlgorithm;
	private String issuerNameHash;
	private String issuerPublicKeyHash;
	private BigDecimal seriaNumber;
	
	public CertID(String issuerNameHash, String issuerPublicKeyHash, BigDecimal seriaNumber) {
		super();
		this.issuerNameHash = issuerNameHash;
		this.issuerPublicKeyHash = issuerPublicKeyHash;
		this.seriaNumber = seriaNumber;
	}
	public CertID() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getIssuerNameHash() {
		return issuerNameHash;
	}
	public void setIssuerNameHash(String issuerNameHash) {
		this.issuerNameHash = issuerNameHash;
	}
	public String getIssuerPublicKeyHash() {
		return issuerPublicKeyHash;
	}
	public void setIssuerPublicKeyHash(String issuerPublicKeyHash) {
		this.issuerPublicKeyHash = issuerPublicKeyHash;
	}
	public BigDecimal getSeriaNumber() {
		return seriaNumber;
	}
	public void setSeriaNumber(BigDecimal seriaNumber) {
		this.seriaNumber = seriaNumber;
	}
	public static HashAlgorithm getHashAlgorithm() {
		return hashAlgorithm;
	}
	public static void setHashAlgorithm(HashAlgorithm hashAlgorithm) {
		CertID.hashAlgorithm = hashAlgorithm;
	}
	
	
}
