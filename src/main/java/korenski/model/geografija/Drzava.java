package korenski.model.geografija;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="drzava")
public class Drzava {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = true)
	private Long id;

	
	@Column(unique = true, nullable = false)
	private String oznaka;
	
	@Column(unique = true, nullable = false)
	private String naziv;
	
	@Column(name="naseljenaMesta")
	@OneToMany()
	private Collection<NaseljenoMesto> naseljenaMesta;

	public Drzava() {
		super();
		// TODO Auto-generated constructor stub
		naseljenaMesta = new ArrayList<>();
	}

	
	
	public Drzava(Long id, String oznaka, String naziv, Collection<NaseljenoMesto> naseljenaMesta) {
		super();
		this.id = id;
		this.oznaka = oznaka;
		this.naziv = naziv;
		this.naseljenaMesta = naseljenaMesta;
	}



	public Drzava(String oznaka, String naziv) {
		super();
		this.oznaka = oznaka;
		this.naziv = naziv;
		this.naseljenaMesta = new ArrayList<>();
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

	@JsonIgnoreProperties(value={"drzava"}, allowSetters=true)
	public Collection<NaseljenoMesto> getNaseljenaMesta() {
		return naseljenaMesta;
	}

	public void setNaseljenaMesta(Collection<NaseljenoMesto> naseljenaMesta) {
		this.naseljenaMesta = naseljenaMesta;
	}
	
	
	
}
