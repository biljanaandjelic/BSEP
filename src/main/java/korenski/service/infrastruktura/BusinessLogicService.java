package korenski.service.infrastruktura;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import korenski.model.infrastruktura.AnalitikaIzvoda;
import korenski.model.infrastruktura.Bank;
import korenski.model.infrastruktura.DnevnoStanjeRacuna;
import korenski.model.infrastruktura.MedjubankarskiPrenos;
import korenski.model.infrastruktura.Racun;
import korenski.model.infrastruktura.StavkaPrenosa;
import korenski.model.nalog_za_prenos.NalogZaPrenos;
import korenski.model.sifrarnici.Message;
import korenski.repository.institutions.AnalitikaIzvodaRepository;
import korenski.repository.institutions.BankRepository;
import korenski.repository.institutions.DnevnoStanjeRepository;
import korenski.repository.institutions.MedjubankarskiPrenosRepository;
import korenski.repository.institutions.RacunRepository;
import korenski.repository.institutions.StavkaPrenosaRepository;
import korenski.repository.sifrarnici.MessageRepository;

@Service
public class BusinessLogicService {

	@Autowired
	RacunRepository racunRepository;
	@Autowired
	BankRepository bankRepository;
	@Autowired
	DnevnoStanjeRepository dnevnoStanjeRepository;
	@Autowired
	AnalitikaIzvodaRepository analitikaIzvodaRepository;
	@Autowired
	MedjubankarskiPrenosRepository mBRepository;
	@Autowired
	StavkaPrenosaRepository sPRepository;
	@Autowired
	MessageRepository messageRepository;

	public boolean sameBankTransfer(NalogZaPrenos nalog, Racun racunDuznika, Racun racunPoverioca)
			throws ParseException {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		Date today = new Date();
		Date todayWithZeroTime = null;
		try {
			todayWithZeroTime = formatter.parse(formatter.format(today));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		DnevnoStanjeRacuna dnevnoStanjeDuznika = dnevnoStanjeRepository.findByDatumAndRacun(todayWithZeroTime,
				racunDuznika);
		DnevnoStanjeRacuna dnevnoStanjePoverioca = dnevnoStanjeRepository.findByDatumAndRacun(todayWithZeroTime,
				racunPoverioca);

		if (dnevnoStanjeDuznika == null) {
			dnevnoStanjeDuznika = new DnevnoStanjeRacuna(todayWithZeroTime, racunDuznika.getStanje(), 0, 0,
					racunDuznika.getStanje(), racunDuznika);
			racunDuznika.getDnevnaStanjaRacuna().add(dnevnoStanjeDuznika);
			try {
				dnevnoStanjeRepository.save(dnevnoStanjeDuznika);
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}

		if (dnevnoStanjePoverioca == null) {
			dnevnoStanjePoverioca = new DnevnoStanjeRacuna(new Date(), racunPoverioca.getStanje(), 0, 0,
					racunPoverioca.getStanje(), racunPoverioca);
			racunPoverioca.getDnevnaStanjaRacuna().add(dnevnoStanjePoverioca);
			try {
				dnevnoStanjeRepository.save(dnevnoStanjePoverioca);
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}

		boolean hitno;

		if (nalog.getHitno().equals("Da")) {
			hitno = true;
		} else {
			hitno = false;
		}

		AnalitikaIzvoda analitikaDuznika = new AnalitikaIzvoda(new Date(new Date().getTime()), "T",
				nalog.getPodaciODuzniku().getAdresa(), nalog.getSvrhaPlacanja(),
				nalog.getPodaciOPoveriocu().getAdresa(), nalog.getPodaciOPlacanju().getDatumValute(),
				nalog.getPodaciOPlacanju().getDatumValute(), racunDuznika.getBrojRacuna(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciDuznik().getModel(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciDuznik().getPozivNaBroj(),
				racunPoverioca.getBrojRacuna(), nalog.getPodaciOPlacanju().getFinansijskiPodaciPoverilac().getModel(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciPoverilac().getPozivNaBroj(),
				nalog.getPodaciOPlacanju().getIznos(), nalog.getPodaciOPlacanju().getValuta(), hitno,
				dnevnoStanjeDuznika);

		AnalitikaIzvoda analitikaPoverioca = new AnalitikaIzvoda(new Date(new Date().getTime()), "K",
				nalog.getPodaciODuzniku().getAdresa(), nalog.getSvrhaPlacanja(),
				nalog.getPodaciOPoveriocu().getAdresa(), nalog.getPodaciOPlacanju().getDatumValute(),
				nalog.getPodaciOPlacanju().getDatumValute(), racunDuznika.getBrojRacuna(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciDuznik().getModel(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciDuznik().getPozivNaBroj(),
				racunPoverioca.getBrojRacuna(), nalog.getPodaciOPlacanju().getFinansijskiPodaciPoverilac().getModel(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciPoverilac().getPozivNaBroj(),
				nalog.getPodaciOPlacanju().getIznos(), nalog.getPodaciOPlacanju().getValuta(), hitno,
				dnevnoStanjePoverioca);

		// if((dnevnoStanjeDuznika.getNovoStanje()-analitikaDuznika.getIznos())
		// < 0){
		// System.out.println("Nema dovoljno sredstava!");
		// return;
		// }

		try {
			analitikaDuznika = analitikaIzvodaRepository.save(analitikaDuznika);
			dnevnoStanjeDuznika.setNovoStanje(dnevnoStanjeDuznika.getNovoStanje() - analitikaDuznika.getIznos());
			dnevnoStanjeDuznika.setPrometNaTeret(dnevnoStanjeDuznika.getPrometNaTeret() + analitikaDuznika.getIznos());
			dnevnoStanjeDuznika.getAnalitike().add(analitikaDuznika);
			dnevnoStanjeRepository.save(dnevnoStanjeDuznika);

//<<<<<<< HEAD
			analitikaPoverioca = analitikaIzvodaRepository.save(analitikaPoverioca);
//=======
			
			racunDuznika.setStanje(racunDuznika.getStanje()-nalog.getPodaciOPlacanju().getIznos());
			racunPoverioca.setStanje(racunPoverioca.getStanje()+nalog.getPodaciOPlacanju().getIznos());
			
			racunRepository.save(racunDuznika);
			racunRepository.save(racunPoverioca);
			
//			analitikaPoverioca=analitikaIzvodaRepository.save(analitikaPoverioca);
//>>>>>>> 867519fb46c1caadce1b9f443060473310b22185
			dnevnoStanjePoverioca.setNovoStanje(dnevnoStanjePoverioca.getNovoStanje() + analitikaPoverioca.getIznos());
			dnevnoStanjePoverioca
					.setPrometUKorist(dnevnoStanjePoverioca.getPrometUKorist() + analitikaPoverioca.getIznos());
			dnevnoStanjePoverioca.getAnalitike().add(analitikaPoverioca);
			dnevnoStanjeRepository.save(dnevnoStanjePoverioca);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	 * Metoda koja obradjuje nalog u slucaju da se racun poverioca ne nalazi u
	 * istoj banci u kojoj je i duznik. Provjerava se status naloga da li je
	 * hitno ili je iznos veci od 250000. Ukoliko je slucaj jedan od ta dva
	 * ucitava se MT103 koja oznacava da se transfer obavlja u RTGS rezimu,a
	 * ukolio ovo nije slucaj ucitava se poruka MT102 i dodjeljuje prenosu sto
	 * je oznacava kao poruku za obradu kliring rezimu rada.
	 * 
	 * @param nalog
	 *            nalog koji se obradjuje
	 * @param racunDuznika
	 *            racun duznika iz nase banke za koji se generise analiti,
	 *            azurira dnevno stanje i stanje racuna
	 * @param racunPoverioca
	 *            broj racuna poverioca koji se nalazi u nekoj drugoj banci.
	 * @author Biljana
	 * @throws ParseException
	 */
	public boolean differentBanksTransfer(NalogZaPrenos nalog, Racun racunDuznika, String racunPoverioca)
			throws ParseException {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		Date today = new Date();
		Date todayWithZeroTime = null;
		boolean rtgs = false;
		try {
			todayWithZeroTime = formatter.parse(formatter.format(today));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		DnevnoStanjeRacuna dnevnoStanjeDuznika = dnevnoStanjeRepository.findByDatumAndRacun(todayWithZeroTime,
				racunDuznika);

		if (dnevnoStanjeDuznika == null) {
			dnevnoStanjeDuznika = new DnevnoStanjeRacuna(todayWithZeroTime, racunDuznika.getStanje(), 0, 0,
					racunDuznika.getStanje(), racunDuznika);
			racunDuznika.getDnevnaStanjaRacuna().add(dnevnoStanjeDuznika);
			try {
				dnevnoStanjeRepository.save(dnevnoStanjeDuznika);
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}

		String code = racunPoverioca.substring(0, 3);

		Bank bankaDruga = bankRepository.findByCode(code);
		Date maxDate = mBRepository.findMaxDate(racunDuznika.getBank(), bankaDruga);
		System.out.println("MaxDate " + maxDate);
		MedjubankarskiPrenos latestMBPrenos = mBRepository.findLatestMedjubankarskiPrenos(maxDate);
		Set<StavkaPrenosa> stavkePrenosa = sPRepository.findStavkaPrenosaByMedjubankarskiPrenos(latestMBPrenos);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date newDate = new Date();
		try {
			newDate = sdf.parse(sdf.format(today));
		} catch (ParseException e1) {

			e1.printStackTrace();
			return false;
		}
		AnalitikaIzvoda analitikaDuznika = new AnalitikaIzvoda(newDate, "T", nalog.getPodaciODuzniku().getAdresa(),
				nalog.getSvrhaPlacanja(), nalog.getPodaciOPoveriocu().getAdresa(),
				nalog.getPodaciOPlacanju().getDatumValute(), nalog.getPodaciOPlacanju().getDatumValute(),
				racunDuznika.getBrojRacuna(), nalog.getPodaciOPlacanju().getFinansijskiPodaciDuznik().getModel(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciDuznik().getPozivNaBroj(), racunPoverioca,
				nalog.getPodaciOPlacanju().getFinansijskiPodaciPoverilac().getModel(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciPoverilac().getPozivNaBroj(),
				nalog.getPodaciOPlacanju().getIznos(), nalog.getPodaciOPlacanju().getValuta(), false,
				dnevnoStanjeDuznika);
		if (nalog.getHitno().equals("Da")) {
			analitikaDuznika.setHitno(true);
		}
		dnevnoStanjeDuznika.setPrometNaTeret(nalog.getPodaciOPlacanju().getIznos());
		dnevnoStanjeDuznika.setNovoStanje(dnevnoStanjeDuznika.getNovoStanje() - nalog.getPodaciOPlacanju().getIznos());

		try {

			dnevnoStanjeDuznika = dnevnoStanjeRepository.save(dnevnoStanjeDuznika);
			analitikaDuznika.setDnevnoStanjeRacuna(dnevnoStanjeDuznika);
			analitikaDuznika = analitikaIzvodaRepository.save(analitikaDuznika);

			racunDuznika.setStanje(racunDuznika.getStanje() - nalog.getPodaciOPlacanju().getIznos());
			dnevnoStanjeDuznika.getAnalitike().add(analitikaDuznika);
			dnevnoStanjeRepository.save(dnevnoStanjeDuznika);
			racunRepository.save(racunDuznika);

		} catch (Exception e) {
			System.out.println("Problem sa cuvanjem analitika izvoda");
			return false;
		}

		if (maxDate == null || latestMBPrenos == null || stavkePrenosa.size() == 4 || nalog.getHitno().equals("Da")) {
			latestMBPrenos = new MedjubankarskiPrenos();
			long start = System.currentTimeMillis();
			long end = start + 1000; // 60 seconds * 1000 ms/sec
			while (System.currentTimeMillis() < end) {

			}
			latestMBPrenos.setDatum(new Timestamp((new Date().getTime())));
			latestMBPrenos.setBankaPrva(racunDuznika.getBank());

			latestMBPrenos.setBankaDruga(bankaDruga);
			latestMBPrenos.setIznos(nalog.getPodaciOPlacanju().getIznos());
			Message message = null;
			if (nalog.getPodaciOPlacanju().getIznos() > 250000 || nalog.getHitno().equals("Da")) {

				try {
					message = messageRepository.findByCode("MT103");
				} catch (Exception e) {
					// TODO: handle exception
					return false;
				}
				if (message != null) {
					latestMBPrenos.setPoruka(messageRepository.findOne(new Long(2)));
					rtgs = true;
				} else {
					return false;
				}
			} else {
				try {
					message = messageRepository.findByCode("MT102");
				} catch (Exception e) {

					return false;
				}
				if (message != null) {
					latestMBPrenos.setPoruka(messageRepository.findOne(new Long(1)));
				} else {
					return false;
				}
			}
			try {

				MedjubankarskiPrenos newMedjubankarskiPrenos = mBRepository.save(latestMBPrenos);
				System.out.println("Uspijesno cuvanje medjubankarskog prenosa");
			} catch (Exception e) {
				return false;
			}
		} else {
			latestMBPrenos.setIznos(latestMBPrenos.getIznos() + nalog.getPodaciOPlacanju().getIznos());
		}

		StavkaPrenosa stavkaPrenosa = new StavkaPrenosa();
		stavkaPrenosa.setAnalitikaIzvoda(analitikaDuznika);

		try {

			mBRepository.save(latestMBPrenos);
			stavkaPrenosa.setStavkaPrenosa(latestMBPrenos);
			stavkaPrenosa = sPRepository.save(stavkaPrenosa);
			latestMBPrenos.addStavkaPrenosa(stavkaPrenosa);
			latestMBPrenos = mBRepository.save(latestMBPrenos);
			System.out.println("**********************************************");
			System.out.println("Perzistovanje medjubankarskog prenosa");
			System.out.println("**********************************************");
			if (rtgs) {
				if (!export(latestMBPrenos)) {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean otherBankPayment(NalogZaPrenos nalog, Racun racunPoverioca) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		Date today = new Date();
		Date todayWithZeroTime = null;
		try {
			todayWithZeroTime = formatter.parse(formatter.format(today));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}

		DnevnoStanjeRacuna dnevnoStanjePoverioca = dnevnoStanjeRepository.findByDatumAndRacun(todayWithZeroTime,
				racunPoverioca);

		if (dnevnoStanjePoverioca == null) {
			dnevnoStanjePoverioca = new DnevnoStanjeRacuna(new Date(), racunPoverioca.getStanje(), 0, 0,
					racunPoverioca.getStanje(), racunPoverioca);
			racunPoverioca.getDnevnaStanjaRacuna().add(dnevnoStanjePoverioca);
			try {
				dnevnoStanjeRepository.save(dnevnoStanjePoverioca);
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}

		boolean hitno;

		if (nalog.getHitno().equals("Da")) {
			hitno = true;
		} else {
			hitno = false;
		}

		AnalitikaIzvoda analitikaPoverioca = new AnalitikaIzvoda(new Date(new Date().getTime()), "K",
				nalog.getPodaciODuzniku().getAdresa(), nalog.getSvrhaPlacanja(),
				nalog.getPodaciOPoveriocu().getAdresa(), nalog.getPodaciOPlacanju().getDatumValute(),
				nalog.getPodaciOPlacanju().getDatumValute(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciDuznik().getBrojRacuna(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciDuznik().getModel(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciDuznik().getPozivNaBroj(),
				racunPoverioca.getBrojRacuna(), nalog.getPodaciOPlacanju().getFinansijskiPodaciPoverilac().getModel(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciPoverilac().getPozivNaBroj(),
				nalog.getPodaciOPlacanju().getIznos(), nalog.getPodaciOPlacanju().getValuta(), hitno,
				dnevnoStanjePoverioca);

		try {
//<<<<<<< HEAD

			analitikaPoverioca = analitikaIzvodaRepository.save(analitikaPoverioca);
///=======
			
			racunPoverioca.setStanje(racunPoverioca.getStanje()+nalog.getPodaciOPlacanju().getIznos());
			
			racunRepository.save(racunPoverioca);
			
//			analitikaPoverioca=analitikaIzvodaRepository.save(analitikaPoverioca);
//>>>>>>> 867519fb46c1caadce1b9f443060473310b22185
			dnevnoStanjePoverioca.setNovoStanje(dnevnoStanjePoverioca.getNovoStanje() + analitikaPoverioca.getIznos());
			dnevnoStanjePoverioca
					.setPrometUKorist(dnevnoStanjePoverioca.getPrometUKorist() + analitikaPoverioca.getIznos());
			dnevnoStanjePoverioca.getAnalitike().add(analitikaPoverioca);
			dnevnoStanjeRepository.save(dnevnoStanjePoverioca);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	public boolean uplata(NalogZaPrenos nalog, Racun racunPoverioca) {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		Date today = new Date();
		Date todayWithZeroTime = null;
		try {
			todayWithZeroTime = formatter.parse(formatter.format(today));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			e1.printStackTrace();
			return false;
		}

		DnevnoStanjeRacuna dnevnoStanjePoverioca = dnevnoStanjeRepository.findByDatumAndRacun(todayWithZeroTime,
				racunPoverioca);

		if (dnevnoStanjePoverioca == null) {
			dnevnoStanjePoverioca = new DnevnoStanjeRacuna(new Date(), racunPoverioca.getStanje(), 0, 0,
					racunPoverioca.getStanje(), racunPoverioca);
			racunPoverioca.getDnevnaStanjaRacuna().add(dnevnoStanjePoverioca);
			try {

		
				AnalitikaIzvoda analitikaPoverioca=null;
				racunPoverioca.setStanje(racunPoverioca.getStanje()+nalog.getPodaciOPlacanju().getIznos());
				
				racunRepository.save(racunPoverioca);
				
				analitikaPoverioca=analitikaIzvodaRepository.save(analitikaPoverioca);
				dnevnoStanjePoverioca.setNovoStanje(dnevnoStanjePoverioca.getNovoStanje() + analitikaPoverioca.getIznos());
				dnevnoStanjePoverioca
						.setPrometUKorist(dnevnoStanjePoverioca.getPrometUKorist() + analitikaPoverioca.getIznos());
				dnevnoStanjePoverioca.getAnalitike().add(analitikaPoverioca);

				dnevnoStanjeRepository.save(dnevnoStanjePoverioca);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		boolean hitno;
		if (nalog.getHitno().equals("Da")) {
			hitno = true;
		} else {
			hitno = false;
		}

		AnalitikaIzvoda analitikaPoverioca = new AnalitikaIzvoda(new Date(new Date().getTime()), "K",
				nalog.getPodaciODuzniku().getAdresa(), nalog.getSvrhaPlacanja(),
				nalog.getPodaciOPoveriocu().getAdresa(), nalog.getPodaciOPlacanju().getDatumValute(),
				nalog.getPodaciOPlacanju().getDatumValute(), null, null, null, racunPoverioca.getBrojRacuna(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciPoverilac().getModel(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciPoverilac().getPozivNaBroj(),
				nalog.getPodaciOPlacanju().getIznos(), nalog.getPodaciOPlacanju().getValuta(), hitno,
				dnevnoStanjePoverioca);

		try {

			analitikaPoverioca = analitikaIzvodaRepository.save(analitikaPoverioca);
			dnevnoStanjePoverioca.setNovoStanje(dnevnoStanjePoverioca.getNovoStanje() + analitikaPoverioca.getIznos());
			dnevnoStanjePoverioca
					.setPrometUKorist(dnevnoStanjePoverioca.getPrometUKorist() + analitikaPoverioca.getIznos());
			dnevnoStanjePoverioca.getAnalitike().add(analitikaPoverioca);
			dnevnoStanjeRepository.save(dnevnoStanjePoverioca);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

		return true;
	}

	public boolean isplata(NalogZaPrenos nalog, Racun racunDuznika) {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		Date today = new Date();
		Date todayWithZeroTime = null;
		try {
			todayWithZeroTime = formatter.parse(formatter.format(today));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		DnevnoStanjeRacuna dnevnoStanjeDuznika = dnevnoStanjeRepository.findByDatumAndRacun(todayWithZeroTime,
				racunDuznika);
	

		if (dnevnoStanjeDuznika == null) {
			dnevnoStanjeDuznika = new DnevnoStanjeRacuna(todayWithZeroTime, racunDuznika.getStanje(), 0, 0,
					racunDuznika.getStanje(), racunDuznika);
			racunDuznika.getDnevnaStanjaRacuna().add(dnevnoStanjeDuznika);
			try {
				dnevnoStanjeRepository.save(dnevnoStanjeDuznika);
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}

	

		boolean hitno;

		if (nalog.getHitno().equals("Da")) {
			hitno = true;
		} else {
			hitno = false;
		}

		AnalitikaIzvoda analitikaDuznika = new AnalitikaIzvoda(new Date(new Date().getTime()), "T",
				nalog.getPodaciODuzniku().getAdresa(), nalog.getSvrhaPlacanja(),
				nalog.getPodaciOPoveriocu().getAdresa(), nalog.getPodaciOPlacanju().getDatumValute(),
				nalog.getPodaciOPlacanju().getDatumValute(), racunDuznika.getBrojRacuna(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciDuznik().getModel(),
				nalog.getPodaciOPlacanju().getFinansijskiPodaciDuznik().getPozivNaBroj(), null, null, null,
				nalog.getPodaciOPlacanju().getIznos(), nalog.getPodaciOPlacanju().getValuta(), hitno,
				dnevnoStanjeDuznika);



		try {

			analitikaDuznika = analitikaIzvodaRepository.save(analitikaDuznika);

			
			racunDuznika.setStanje(racunDuznika.getStanje()-nalog.getPodaciOPlacanju().getIznos());
			
			racunRepository.save(racunDuznika);
			
			analitikaDuznika=analitikaIzvodaRepository.save(analitikaDuznika);

			dnevnoStanjeDuznika.setNovoStanje(dnevnoStanjeDuznika.getNovoStanje() - analitikaDuznika.getIznos());
			dnevnoStanjeDuznika.setPrometNaTeret(dnevnoStanjeDuznika.getPrometNaTeret() + analitikaDuznika.getIznos());
			dnevnoStanjeDuznika.getAnalitike().add(analitikaDuznika);
			dnevnoStanjeRepository.save(dnevnoStanjeDuznika);

		
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	public boolean export(MedjubankarskiPrenos foundMedjubankarskiPrenos) {
		try {
		
			File file = new File(
					"./files/xmls/medjubankarskiPrenos" + foundMedjubankarskiPrenos.getId().toString() + ".xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(MedjubankarskiPrenos.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(foundMedjubankarskiPrenos, file);
			foundMedjubankarskiPrenos.setSend(true);
			mBRepository.save(foundMedjubankarskiPrenos);
			// return new ResponseEntity<String>("OK",HttpStatus.OK);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}
}
