package korenski.model.geografija;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="naseljeno_mesto")
public class NaseljenoMesto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = true)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String oznaka;
	
	@Column(unique = true, nullable = false)
	private String naziv;
	
	@Column(unique = true, nullable = false)
	private String postanskiBroj;
	
	
	@ManyToOne
	private Drzava drzava;

	public NaseljenoMesto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NaseljenoMesto(String oznaka, String naziv, String postanskiBroj, Drzava drzava) {
		super();
		this.oznaka = oznaka;
		this.naziv = naziv;
		this.postanskiBroj = postanskiBroj;
		this.drzava = drzava;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getPostanskiBroj() {
		return postanskiBroj;
	}

	public void setPostanskiBroj(String postanskiBroj) {
		this.postanskiBroj = postanskiBroj;
	}

	public Drzava getDrzava() {
		return drzava;
	}

	public void setDrzava(Drzava drzava) {
		this.drzava = drzava;
	}

	
	
}
