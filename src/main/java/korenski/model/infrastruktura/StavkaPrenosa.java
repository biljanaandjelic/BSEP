package korenski.model.infrastruktura;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StavkaPrenosa {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne(optional=false)
	private AnalitikaIzvoda analitikaIzvoda;
	@ManyToOne(optional=false)
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

	public MedjubankarskiPrenos getStavkaPrenosa() {
		return medjubankarskiPrenos;
	}

	public void setStavkaPrenosa(MedjubankarskiPrenos medjubankarskiPrenos) {
		this.medjubankarskiPrenos = medjubankarskiPrenos;
	}
	

}
