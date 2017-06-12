package korenski.model.klijenti;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import korenski.model.autorizacija.Subject;
import korenski.model.geografija.NaseljenoMesto;
import korenski.model.infrastruktura.Bank;
import korenski.model.infrastruktura.Racun;

@Entity
@Table(name="klijent")
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "", propOrder = {
//    "ime",
//    "prezime",
//    "adresa",
//    "fizickoLice",
//    "bank",
//    "racuni"
//})
@XmlRootElement()
@XmlSeeAlso({Racun.class, Bank.class})
public class Klijent extends Subject{
	
//	@Id
//	@GeneratedValue
	//@Column(name="id")
//	private Long id;
	@Column(nullable = false, length = 13)
	@Pattern(regexp = "[0-9]{13}", message = "Oznaka JMBG-a mora imati 13 cifara.")
	//@XmlTransient
	private String jmbg;
	
	@Column(nullable = false)
	@Size(max = 60)
	@Pattern(regexp = "[A-Z][a-z]*", message = "Ime mora imati veliko pocetno slovo.")
	private String ime;
	
	@Column(nullable = false)
	@Size(max = 60)

	@Pattern(regexp = "[A-Z][a-z]*", message = "Prezime mora imati veliko pocetno slovo.")


	private String prezime;
	
	@Column(nullable = false)
	@Size(max = 60)
	@Pattern(regexp = "[0-9a-zA-Z]+")
	//@XmlElement(name="adresa")
	private String adresa;
	
	@Column(nullable = false)
	@Size(max = 20)
	//@Pattern(regexp = "\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\2([0-9]{4})")

	@Pattern(regexp = "[0-9]{9,15}", message = "Telefon se sastoji od 9 do 15 cifara")

	//@XmlTransient

	private String telefon;
	
	@Column(unique = true, nullable = false)
	@Size(max = 254)
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
//	@XmlElement(name="")
	//@XmlTransient
	private String email;
	
	//sta da radim sa delom tip klijenta?
	
	@Column(nullable = false)
	//@XmlElement(name="fizickoLice")
	private boolean fizickoLice;
	
	@ManyToOne
	//@XmlElement(name="naseljenoMjesto")
	//@XmlTransient
	private NaseljenoMesto naseljenoMesto;
	
	@ManyToOne
	//@XmlElement(name="bank")
	private Bank bank;
	
	///@Column(name="racuni")
	@OneToMany(mappedBy="klijent", fetch=FetchType.LAZY)
	//@XmlElement(name="racuni")
	private List<Racun> racuni;
	
	
	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	
	public boolean isFizickoLice() {
		return fizickoLice;
	}

	public void setFizickoLice(boolean fizickoLice) {
		this.fizickoLice = fizickoLice;
	}

	//@JsonIgnoreProperties({"klijent"})
	public List<Racun> getRacuni() {
		return racuni;
	}

	public void setRacuni(List<Racun> racuni) {
		this.racuni = racuni;
	}

	public Klijent(Long id, String jmbg, String ime, String prezime, String adresa, String telefon, String email,
			NaseljenoMesto naseljenoMesto) {
		super();
		//this.id = id;
		this.jmbg = jmbg;
		this.ime = ime;
		this.prezime = prezime;
		this.adresa = adresa;
		this.telefon = telefon;
		this.email = email;
		this.naseljenoMesto = naseljenoMesto;
	}

	public Klijent() {
		super();
		// TODO Auto-generated constructor stub
		this.racuni=new ArrayList<Racun>();
	}

	
	@XmlTransient
	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	@XmlTransient
	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	@XmlTransient
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	@XmlTransient
	public NaseljenoMesto getNaseljenoMesto() {
		return naseljenoMesto;
	}

	public void setNaseljenoMesto(NaseljenoMesto naseljenoMesto) {
		this.naseljenoMesto = naseljenoMesto;
	}
	
}
