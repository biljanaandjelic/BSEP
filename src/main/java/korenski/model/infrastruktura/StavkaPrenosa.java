package korenski.model.infrastruktura;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@XmlRootElement()
public class StavkaPrenosa {
	@Id
	@GeneratedValue
	@XmlTransient
	private Long id;
	@ManyToOne(optional=false)
	private AnalitikaIzvoda analitikaIzvoda;
	@ManyToOne(optional=false)
	@XmlTransient
	private MedjubankarskiPrenos medjubankarskiPrenos;
	
	public StavkaPrenosa() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StavkaPrenosa(Long id, AnalitikaIzvoda analitikaIzvoda, MedjubankarskiPrenos medjubankarskiPrenos) {
		super();
		this.id = id;
		this.analitikaIzvoda = analitikaIzvoda;
		this.medjubankarskiPrenos = medjubankarskiPrenos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AnalitikaIzvoda getAnalitikaIzvoda() {
		return analitikaIzvoda;
	}

	public void setAnalitikaIzvoda(AnalitikaIzvoda analitikaIzvoda) {
		this.analitikaIzvoda = analitikaIzvoda;
	}

	@JsonIgnoreProperties("stavkePrenosa")
	public MedjubankarskiPrenos getMedjubankarskiPrenos() {
		return medjubankarskiPrenos;
	}

	public void setStavkaPrenosa(MedjubankarskiPrenos medjubankarskiPrenos) {
		this.medjubankarskiPrenos = medjubankarskiPrenos;
	}
	

}
