package korenski.model.sifrarnici;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
//@Table(name="valuta")
public class Valuta {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique=true, nullable=false)
	@Pattern(regexp = "[A-Z]{3}", message = "Oznaka valute se mora sastojati od tacno 3 velika slova!")
	private String code;
	
	@Column(unique=true, nullable=false)
	@Size(max=30)
	//@Pattern(regexp = "[A-Z]*[a-z]*[A-Z]*", message = "Oznaka valute se mora sastojati od tacno 3 velika slova!")
	private String name;
	
	public Valuta() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Valuta(Long id, String code, String name) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
