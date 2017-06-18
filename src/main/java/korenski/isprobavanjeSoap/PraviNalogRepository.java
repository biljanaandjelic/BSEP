package korenski.isprobavanjeSoap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

import io.spring.guides.gs_producing_web_service2.NalogZaPrenos;
import io.spring.guides.gs_producing_web_service2.NalogZaPrenos.PodaciOPlacanju;
import io.spring.guides.gs_producing_web_service2.TFinansijskiPodaci;


@Component
public class PraviNalogRepository {
	private List<NalogZaPrenos> nalozi = new ArrayList<>();

	@PostConstruct
	public void initData() throws JAXBException {

		//printNalozi(nalozi);
		NalogZaPrenos n = new NalogZaPrenos();
		n.setHitno("Da");
		n.setPodaciODuzniku("Duznik1");
		n.setPodaciOPoveriocu("Poverilac");
		n.setSvrhaPlacanja("Svrha placanja 1");
		
		PodaciOPlacanju pop = new PodaciOPlacanju();
		pop.setIznos(new BigDecimal(100));
		pop.setSifraPlacanja("888");
		pop.setValuta("DIN");
		
		
		TFinansijskiPodaci fin = new TFinansijskiPodaci();
		fin.setBrojRacuna("123-7894546-97");
		fin.setModel("456789789");
		fin.setPozivNaBroj("4569879");
		
		
		pop.setFinansijskiPodaciDuznik(fin);
		
		n.setPodaciOPlacanju(pop);
		
		
		nalozi.add(n);
		
//		Marshaller marshaller = context.createMarshaller();
//		
//		// Podešavanje marshaller-a
//		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//		
//		// Umesto System.out-a, može se koristiti FileOutputStream
//		marshaller.marshal(nalozi, System.out);
//		
	}

	public NalogZaPrenos findNalog(String prvi) {
		//Assert.notNull(name, "The country's name must not be null");
		
		return nalozi.get(0);
	}
}