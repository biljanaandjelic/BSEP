package korenski.model.sifrarnici;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



@Entity
public class Activity {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique=true, nullable=false)
	@Pattern(regexp = "[A-Z]+[0-9]*{5}", message = "Oznaka djelatnosti se mora sastojati od tacno 5 karaktera  pri cemu mogu biti velika slova ili brojevi !")
	private String code;
	
	@Column(unique=true, nullable=false)
	@Size(max=60, message="Naziv djelatnosti ne smije sadrzati vise od 60 karaktera")
	private String name;
	
//	@Column(name="pravnaLica")
//	@OneToMany()
//	private Collection<PravnoLice> pravnaLica;
//	
//	public Collection<PravnoLice> getPravnaLica() {
//		return pravnaLica;
//	}


//	public void setPravnaLica(Collection<PravnoLice> pravnaLica) {
//		this.pravnaLica = pravnaLica;
//	}


	public Activity() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Activity(Long id, String code, String name) {
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
