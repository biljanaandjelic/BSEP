package korenski.model.dto;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;



@Entity
@Table(name="certificateID", uniqueConstraints={@UniqueConstraint(columnNames={"serialNumber","alias"})})
public class CertificateID {
	
	public enum Status { OK, UNKNOWN , REVOKED;  };  
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="serialNumber", nullable=false)
	private BigInteger serialNumber;
	
	@Column(nullable=false)
	private Status status;
	
	private Date dateOfRevocation;
	
	@JoinColumn(name="ca")
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	private CertificateID ca;
	
	@Column(unique=true)
	private String alias;
	
	public CertificateID(){
		
	}

	public CertificateID( BigInteger serialNumber, Status status, Date dateOfRevocation, CertificateID idOfCA, String alias) {
		super();
		
		this.serialNumber = serialNumber;
		this.status = status;
		this.dateOfRevocation = dateOfRevocation;
		this.ca = idOfCA;
		this.alias=alias;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDateOfRevocation() {
		return dateOfRevocation;
	}

	public void setDateOfRevocation(Date dateOfRevocation2) {
		this.dateOfRevocation = dateOfRevocation2;
	}

	public CertificateID getIdOfCA() {
		return ca;
	}

	public void setIdOfCA(CertificateID ca) {
		this.ca = ca;
	}

	public CertificateID getCa() {
		return ca;
	}

	public void setCa(CertificateID ca) {
		this.ca = ca;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	

}
