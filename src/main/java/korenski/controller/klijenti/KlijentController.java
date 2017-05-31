package korenski.controller.klijenti;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import korenski.model.klijenti.Klijent;
import korenski.DTOs.KlijentFilter;
import korenski.intercepting.CustomAnnotation;
import korenski.model.autorizacija.User;
import korenski.model.geografija.Drzava;
import korenski.model.geografija.NaseljenoMesto;
import korenski.model.geografija.pomocni.NMFilter;
import korenski.model.infrastruktura.Bank;
import korenski.repository.klijenti.KlijentRepository;
import korenski.singletons.ValidatorSingleton;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import korenski.repository.geografija.NaseljenoMestoRepository;
import korenski.repository.institutions.BankRepository;

@Controller
public class KlijentController {

	@Autowired
	KlijentRepository repository;
	@Autowired
	NaseljenoMestoRepository repNM;
	@Autowired
	BankRepository bankRepository;
	
	@CustomAnnotation(value = "INSERT_PHYSICAL")
	@RequestMapping(
			value = "/noviKlijent",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Klijent> noviKlijent(@RequestBody Klijent klijent, @Context HttpServletRequest request) throws Exception {
		klijent.setFizickoLice(true);
		
		Klijent validity = validityCheck(klijent);
		if(validity != null){
			return new ResponseEntity<Klijent>(validity, HttpStatus.OK);
		}
		
		User u = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());
		klijent.setBank(bank);
		
		Klijent k;
		try {
			k = repository.save(klijent);
		} catch (Exception e){
			return new ResponseEntity<Klijent>(new Klijent(new Long(-1), null, "Greska pri cuvanju u bazi!", null, null, null, null, null), HttpStatus.OK);
		}
		return new ResponseEntity<Klijent>(k, HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "DELETE_PHYSICAL")
	@RequestMapping(
			value = "/obrisiKlijenta/{id}",
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE) //String id_string
	public ResponseEntity<Klijent> obrisiKlijenta(@PathVariable("id") Long id , @Context HttpServletRequest request) throws Exception {
		
		try {
			Klijent klijent = repository.findOne(id);
			repository.delete(klijent);
		} catch (Exception e) {
			return new ResponseEntity<Klijent>(new Klijent(new Long(-1), null, null, null, null, null, null, null), HttpStatus.OK);
		}
		
		return new ResponseEntity<Klijent>(new Klijent(), HttpStatus.OK);
	}

	
	@CustomAnnotation(value = "UPDATE_PHYSICAL")
	@RequestMapping(
			value = "/azurirajKlijenta",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Klijent> azurirajKlijenta(@RequestBody Klijent klijent, @Context HttpServletRequest request) throws Exception {
		
		Klijent validity = validityCheck(klijent);
		if(validity != null){
			return new ResponseEntity<Klijent>(validity, HttpStatus.OK);
		}
		
		Klijent klijentToModify = null;
		NaseljenoMesto naseljenoMesto = null;
		
		try {
			klijentToModify = repository.findOne(klijent.getId());
			
			naseljenoMesto = repNM.findOne(klijent.getNaseljenoMesto().getId());
		} catch (Exception e) {
			return new ResponseEntity<Klijent>(new Klijent(new Long(-1), null, "Ne postoji klijent u bazi.", null, null, null, null, null), HttpStatus.OK);
		}
		
		klijentToModify.setJmbg(klijent.getJmbg());
		klijentToModify.setIme(klijent.getIme());
		klijentToModify.setPrezime(klijent.getPrezime());
		klijentToModify.setAdresa(klijent.getAdresa());
		klijentToModify.setTelefon(klijent.getTelefon());
		klijentToModify.setEmail(klijent.getEmail());
		klijentToModify.setNaseljenoMesto(naseljenoMesto);
		
		try {
			repository.save(klijentToModify);
		} catch (Exception e) {
			return new ResponseEntity<Klijent>(new Klijent(new Long(-1), null, "Greska pri cuvanju u bazi!", null, null, null, null, null), HttpStatus.OK);
		}
		
		return new ResponseEntity<Klijent>(klijentToModify, HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "FIND_ALL_PHYSICAL")
	@RequestMapping(
			value = "/sviKlijenti",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Klijent>> sviKlijenti(@Context HttpServletRequest request) throws Exception {
		User u = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());
		
		return new ResponseEntity<Collection<Klijent>>( repository.findByFizickoLiceAndBank(true, bank), HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "FIND_ALL_PHYSICAL_BY_CITY")
	@RequestMapping(
			value = "/nadjiKlijente/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Klijent>> nadjiKlijente(@PathVariable("id") Long id, @Context HttpServletRequest request) throws Exception {
		
		NaseljenoMesto nm = repNM.findOne(id);
		
		User u = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());
		
		return new ResponseEntity<Collection<Klijent>>( repository.findByNaseljenoMestoAndFizickoLiceAndBank(nm, true, bank), HttpStatus.OK);
	}
	
	/*
	@RequestMapping(
			value = "/filtrirajKlijente/{jmbg}/{ime}/{prezime}/{adresa}/{telefon}/{email}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Klijent>> filtrirajKlijente(@PathVariable("jmbg") String jmbg,
			@PathVariable("ime") String ime, @PathVariable("prezime") String prezime, @PathVariable("adresa") String adresa, @PathVariable("telefon") String telefon, @PathVariable("email") String email) throws Exception {

		
		return new ResponseEntity<Collection<Klijent>>( repository.findByJmbgContainingIgnoreCaseOrImeContainingIgnoreCaseOrPrezimeContainingIgnoreCaseOrAdresaContainingIgnoreCaseOrTelefonContainingIgnoreCaseOrEmailContainingIgnoreCase(jmbg, ime, prezime, adresa, telefon, email), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/filtrirajKlijenteZaNaseljenoMesto/{jmbg}/{ime}/{prezime}/{adresa}/{telefon}/{email}/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Klijent>> filtrirajKlijenteZaNaseljenoMesto(@PathVariable("jmbg") String jmbg,
			@PathVariable("ime") String ime, @PathVariable("prezime") String prezime, @PathVariable("adresa") String adresa, @PathVariable("telefon") String telefon, @PathVariable("email") String email,  @PathVariable("id") Long id, @Context HttpServletRequest request) throws Exception {
		
		NaseljenoMesto naseljenoMesto = repNM.findOne(id);
		
		User u = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());
		
		return new ResponseEntity<Collection<Klijent>>( repository.findByJmbgContainingIgnoreCaseOrImeContainingIgnoreCaseOrPrezimeContainingIgnoreCaseOrAdresaContainingIgnoreCaseOrTelefonContainingIgnoreCaseOrEmailContainingIgnoreCaseOrNaseljenoMestoAndFizickoLiceAndBank(jmbg, ime, prezime, adresa, telefon, email, naseljenoMesto, true, bank), HttpStatus.OK);
	}
	*/
	@CustomAnnotation(value = "FILTER_PHYSICAL")
	@RequestMapping(
			value = "/filtrirajKlijenteZaNaseljenoMesto",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Klijent>> filtrirajKlijenteNaseljenaMesta(@RequestBody KlijentFilter klijentFilter) throws Exception {
		
		if(klijentFilter.getJmbg() == null){
			klijentFilter.setJmbg("");
		}
		
		if(klijentFilter.getIme() == null){
			klijentFilter.setIme("");
		}
		
		if(klijentFilter.getPrezime() == null){
			klijentFilter.setPrezime("");
		}
		
		if(klijentFilter.getAdresa() == null){
			klijentFilter.setAdresa("");
		}
		
		if(klijentFilter.getTelefon() == null){
			klijentFilter.setTelefon("");
		}
		
		if(klijentFilter.getEmail() == null){
			klijentFilter.setEmail("");
		}
		
		if(klijentFilter.getMesto() == null){
			klijentFilter.setMesto(new Long(0));
		}
		
		if(klijentFilter.getMesto().equals(new Long(0))){
			return new ResponseEntity<Collection<Klijent>>( repository.filter(klijentFilter.getJmbg(), klijentFilter.getIme(), klijentFilter.getPrezime(), klijentFilter.getAdresa(), klijentFilter.getTelefon(), klijentFilter.getEmail()), HttpStatus.OK);
		}
		//repository.findByOznakaContainingIgnoreCaseOrNazivContainingIgnoreCaseOrPostanskiBrojContainingIgnoreCaseOrDrzava(oznaka, naziv, postanskiBroj, drzava)
		return new ResponseEntity<Collection<Klijent>>( repository.filterNaseljenoMesto(klijentFilter.getJmbg(), klijentFilter.getIme(), klijentFilter.getPrezime(), klijentFilter.getAdresa(), klijentFilter.getTelefon(), klijentFilter.getEmail(), klijentFilter.getMesto()), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/izvestajIzvoda",
			method = RequestMethod.POST,
			produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> izvestajiRacuna(@Context HttpServletRequest request, @RequestBody Klijent klijent) throws Exception {
		//idiotizam
		try {
			
			Properties connectionProps = new Properties();
		    connectionProps.put("user", "test");
		    connectionProps.put("password", "test");
		    
		    Connection conn = DriverManager.getConnection(
	                   "jdbc:mysql://localhost:3306/finalni?useSSL=false",
	                   connectionProps);
			
		    Calendar cal = Calendar.getInstance();
			Date end = cal.getTime();
			cal.add(Calendar.DATE, -7);
			Date start = cal.getTime();
		    
		    HashMap<String, Object> parameters = new HashMap<String, Object>();
		    parameters.put("id_klijenta", klijent.getId());
		    parameters.put("od", start);
		    parameters.put("do", end);
		    
		    String jp = JasperFillManager.fillReportToFile("./files/izvodKlijenta.jasper", parameters, conn);
		    
			//JasperPrint jp = JasperFillManager.fillReport(
			//	new FileInputStream("./files/test.jasper"),
			//	new HashMap<String, Object>(), conn);
			//eksport
			//File pdf = File.createTempFile("output.", ".pdf");
			JasperExportManager.exportReportToPdfFile(jp, "./files/izvod_klijenta_" + klijent.getId() + ".pdf");
		}catch (Exception ex) {
				ex.printStackTrace();
		}

		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	public Klijent validityCheck(Klijent klijent){
		Set<ConstraintViolation<Klijent>> violations = ValidatorSingleton.getInstance().getValidator().validate(klijent);
		
		if(!violations.isEmpty()){
			Iterator iter = violations.iterator();

			ConstraintViolation<Klijent> first = (ConstraintViolation<Klijent>) iter.next();
			Klijent k = new Klijent();
			k.setId(new Long(-1));
			k.setIme(first.getMessage());
			return k;
		}else{
			return null;
		}
	}
}
