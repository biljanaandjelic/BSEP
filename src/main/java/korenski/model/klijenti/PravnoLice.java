package korenski.model.klijenti;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import korenski.model.geografija.NaseljenoMesto;

@Entity
@Table(name="pravno_lice")
public class PravnoLice extends Klijent {
	
	@Column(nullable = false)
	private String pib;
	
	@Column(nullable = false)
	private String fax;
	
	@Column(nullable = false)
	private String odobrio;

	public PravnoLice(Long id, String jmbg, String ime, String prezime, String adresa, String telefon, String email,
			NaseljenoMesto naseljenoMesto, String pib, String fax, String odobrio) {
		super(id, jmbg, ime, prezime, adresa, telefon, email, naseljenoMesto);
		this.pib = pib;
		this.fax = fax;
		this.odobrio = odobrio;
	}

	public PravnoLice() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PravnoLice(Long id, String jmbg, String ime, String prezime, String adresa, String telefon, String email,
			NaseljenoMesto naseljenoMesto) {
		super(id, jmbg, ime, prezime, adresa, telefon, email, naseljenoMesto);
		// TODO Auto-generated constructor stub
	}

	public String getPib() {
		return pib;
	}

	public void setPib(String pib) {
		this.pib = pib;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getOdobrio() {
		return odobrio;
	}

	public void setOdobrio(String odobrio) {
		this.odobrio = odobrio;
	}
	
}
