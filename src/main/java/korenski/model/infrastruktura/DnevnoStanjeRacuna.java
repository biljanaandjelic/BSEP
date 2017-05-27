package korenski.model.infrastruktura;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class DnevnoStanjeRacuna {
	
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable=false)
	private Date datum;	
	@Column(nullable=false)
	private BigDecimal prethodnoStanje;
	@Column(nullable=false)
	private BigDecimal prometNaTeret;
	@Column(nullable=false)
	private BigDecimal prometUKorist;
	@Column(nullable=false)
	private BigDecimal novoStanje;
	@ManyToOne(optional=false)
	private Racun racun;
	
	public DnevnoStanjeRacuna() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public DnevnoStanjeRacuna(Long id, Date datum, BigDecimal prethodnoStanje, BigDecimal prometNaTeret,
			BigDecimal prometUKorist, BigDecimal novoStanje, Racun racun) {
		super();
		this.id = id;
		this.datum = datum;
		this.prethodnoStanje = prethodnoStanje;
		this.prometNaTeret = prometNaTeret;
		this.prometUKorist = prometUKorist;
		this.novoStanje = novoStanje;
		this.racun = racun;
	}


	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public BigDecimal getPrethodnoStanje() {
		return prethodnoStanje;
	}

	public void setPrethodnoStanje(BigDecimal prethodnoStanje) {
		this.prethodnoStanje = prethodnoStanje;
	}

	public BigDecimal getPrometNaTeret() {
		return prometNaTeret;
	}

	public void setPrometNaTeret(BigDecimal prometNaTeret) {
		this.prometNaTeret = prometNaTeret;
	}

	public BigDecimal getPrometUKorist() {
		return prometUKorist;
	}

	public void setPrometUKorist(BigDecimal prometUKorist) {
		this.prometUKorist = prometUKorist;
	}

	public BigDecimal getNovoStanje() {
		return novoStanje;
	}

	public void setNovoStanje(BigDecimal novoStanje) {
		this.novoStanje = novoStanje;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Racun getRacun() {
		return racun;
	}


	public void setRacun(Racun racun) {
		this.racun = racun;
	}
	
	

}
