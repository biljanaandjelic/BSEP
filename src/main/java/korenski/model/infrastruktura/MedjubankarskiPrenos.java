package korenski.model.infrastruktura;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import korenski.model.sifrarnici.Message;

@Entity
@XmlRootElement()
public class MedjubankarskiPrenos {
	@Id
	@GeneratedValue
	@XmlTransient
	private Long id;
	@ManyToOne(optional=false)
	private Bank bankaPrva;
	@ManyToOne(optional=false)
	private Bank bankaDruga;
	@ManyToOne(optional=false)
	private Message poruka;
	
	@Column(nullable=false)
	//@Temporal(TemporalType.TIMESTAMP)
	private Timestamp datum;
	@Column(nullable=false)
	private double iznos;
	
	@OneToMany(fetch=FetchType.LAZY)	
	private List<StavkaPrenosa> stavkePrenosa;
	
	public MedjubankarskiPrenos() {
		super();
		// TODO Auto-generated constructor stub
		this.stavkePrenosa=new ArrayList<StavkaPrenosa>();
	}

	public MedjubankarskiPrenos(Long id, Bank bankaPrva, Bank bankaDruga, Message poruka, Timestamp datum,
			double iznos, List<StavkaPrenosa> stavkePrenosa) {
		super();
		this.id = id;
		this.bankaPrva = bankaPrva;
		this.bankaDruga = bankaDruga;
		this.poruka = poruka;
		this.datum = datum;
		this.iznos = iznos;
		this.stavkePrenosa=stavkePrenosa;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Bank getBankaPrva() {
		return bankaPrva;
	}

	public void setBankaPrva(Bank bankaPrva) {
		this.bankaPrva = bankaPrva;
	}

	public Bank getBankaDruga() {
		return bankaDruga;
	}

	public void setBankaDruga(Bank bankaDruga) {
		this.bankaDruga = bankaDruga;
	}

	public Message getPoruka() {
		return poruka;
	}

	public void setPoruka(Message poruka) {
		this.poruka = poruka;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Timestamp datum) {
		this.datum = datum;
	}

	public double getIznos() {
		return iznos;
	}

	public void setIznos(double iznos) {
		this.iznos = iznos;
	}
	@JsonIgnoreProperties("medjubankarskiPrenos")
	public List<StavkaPrenosa> getStavkePrenosa() {
		return stavkePrenosa;
	}

	public void setStavkePrenosa(List<StavkaPrenosa> stavkePrenosa) {
		this.stavkePrenosa = stavkePrenosa;
	}
	
	public void addStavkaPrenosa(StavkaPrenosa stavkaPrenosa){
		this.stavkePrenosa.add(stavkaPrenosa);
	}
	

}
