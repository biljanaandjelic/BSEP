package korenski.model.infrastruktura;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Bank {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private String code;
	
	@Column(nullable=false, unique=false)
	private String swiftCode;
	
	@Column(nullable=false)
	private String sstatmentAcount;

	public Bank(Long id, String name, String code, String swiftCode, String sstatmentAcount) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.swiftCode = swiftCode;
		this.sstatmentAcount = sstatmentAcount;
	}

	public Bank() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getSstatmentAcount() {
		return sstatmentAcount;
	}

	public void setSstatmentAcount(String sstatmentAcount) {
		this.sstatmentAcount = sstatmentAcount;
	}
	
	
}
