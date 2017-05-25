package korenski.controller.klijenti;

import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
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

import korenski.intercepting.CustomAnnotation;
import korenski.model.autorizacija.User;
import korenski.model.geografija.NaseljenoMesto;
import korenski.model.infrastruktura.Bank;
import korenski.model.klijenti.Klijent;
import korenski.model.klijenti.PravnoLice;
import korenski.model.sifrarnici.Activity;
import korenski.repository.geografija.NaseljenoMestoRepository;
import korenski.repository.institutions.BankRepository;
import korenski.repository.klijenti.PravnoLiceRepository;
import korenski.repository.sifrarnici.ActivityRepository;

@Controller
public class PravnoLiceController {
	
	@Autowired
	PravnoLiceRepository repository;
	@Autowired
	NaseljenoMestoRepository repNM;
	@Autowired
	ActivityRepository repA;
	@Autowired
	BankRepository bankRepository;
	
	@CustomAnnotation(value = "INSERT_LEGAL")
	@RequestMapping(
			value = "/novoPravnoLice",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PravnoLice> novoPravnoLice(@RequestBody PravnoLice pravnoLice, @Context HttpServletRequest request) throws Exception {
		pravnoLice.setFizickoLice(false);
		
		User u = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());
		pravnoLice.setBank(bank);
		
		PravnoLice pl;
		try {
			pl = repository.save(pravnoLice);
		} catch (Exception e){
			return new ResponseEntity<PravnoLice>(new PravnoLice(new Long(-1), null, null, null, null, null, null, null), HttpStatus.OK);
		}
		return new ResponseEntity<PravnoLice>(pl, HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "DELETE_LEGAL")
	@RequestMapping(
			value = "/obrisiPravnoLice/{id}",
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE) //String id_string
	public ResponseEntity<PravnoLice> obrisiPravnoLice(@PathVariable("id") Long id , @Context HttpServletRequest request) throws Exception {
		
		try {
			PravnoLice pravnoLice = repository.findOne(id);
			repository.delete(pravnoLice);
		} catch (Exception e){
			return new ResponseEntity<PravnoLice>(new PravnoLice(new Long(-1), null, null, null, null, null, null, null), HttpStatus.OK);
		}
		return new ResponseEntity<PravnoLice>(new PravnoLice(), HttpStatus.OK);
	}

	
	@CustomAnnotation(value = "UPDATE_LEGAL")
	@RequestMapping(
			value = "/azurirajPravnoLice",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PravnoLice> azurirajPravnoLice(@RequestBody PravnoLice pravnoLice, @Context HttpServletRequest request) throws Exception {
		//zar se ne koristi metoda update?
		
		PravnoLice pravnoLiceToModify = null;
		NaseljenoMesto naseljenoMesto = null;
		Activity activity = null;
		
		try {
			pravnoLiceToModify = repository.findOne(pravnoLice.getId());
	
			naseljenoMesto = repNM.findOne(pravnoLice.getNaseljenoMesto().getId());
			activity = repA.findOne(pravnoLice.getActivity().getId());
		} catch (Exception e){
			return new ResponseEntity<PravnoLice>(new PravnoLice(new Long(-1), null, null, null, null, null, null, null), HttpStatus.OK);
		}
		
		pravnoLiceToModify.setJmbg(pravnoLice.getJmbg());
		pravnoLiceToModify.setIme(pravnoLice.getIme());
		pravnoLiceToModify.setPrezime(pravnoLice.getPrezime());
		pravnoLiceToModify.setAdresa(pravnoLice.getAdresa());
		pravnoLiceToModify.setTelefon(pravnoLice.getTelefon());
		pravnoLiceToModify.setEmail(pravnoLice.getEmail());
		pravnoLiceToModify.setPib(pravnoLice.getPib());
		pravnoLiceToModify.setFax(pravnoLice.getFax());
		pravnoLiceToModify.setOdobrio(pravnoLice.getOdobrio());
		pravnoLiceToModify.setNaseljenoMesto(naseljenoMesto);
		pravnoLiceToModify.setActivity(activity);
		
		try {
			repository.save(pravnoLiceToModify);
		} catch (Exception e) {
			return new ResponseEntity<PravnoLice>(new PravnoLice(new Long(-1), null, null, null, null, null, null, null), HttpStatus.OK);
		}
		return new ResponseEntity<PravnoLice>(pravnoLiceToModify, HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "FIND_ALL_LEGAL")
	@RequestMapping(
			value = "/svaPravnaLica",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<PravnoLice>> svaPravnaLica(@Context HttpServletRequest request) throws Exception {
		User u = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());
		
		return new ResponseEntity<Collection<PravnoLice>>( repository.findByBank(bank), HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "FIND_ALL_LEGAL_BY_CITY")
	@RequestMapping(
			value = "/nadjiPravnaLica/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<PravnoLice>> nadjiPravnaLica(@PathVariable("id") Long id, @Context HttpServletRequest request) throws Exception {

		NaseljenoMesto nm = repNM.findOne(id);
		
		User u = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());
		
		return new ResponseEntity<Collection<PravnoLice>>( repository.findByNaseljenoMestoAndBank(nm, bank), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/filtrirajPravnaLica/{jmbg}/{ime}/{prezime}/{adresa}/{telefon}/{email}/{pib}/{fax}/{odobrio}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<PravnoLice>> filtrirajPravnaLica(@PathVariable("jmbg") String jmbg,
			@PathVariable("ime") String ime, @PathVariable("prezime") String prezime, @PathVariable("adresa") String adresa, @PathVariable("telefon") String telefon, @PathVariable("email") String email, @PathVariable("pib") String pib, @PathVariable("fax") String fax, @PathVariable("odobrio") String odobrio) throws Exception {

		return new ResponseEntity<Collection<PravnoLice>>( repository.findByJmbgContainingIgnoreCaseOrImeContainingIgnoreCaseOrPrezimeContainingIgnoreCaseOrAdresaContainingIgnoreCaseOrTelefonContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPibContainingIgnoreCaseOrFaxContainingIgnoreCaseOrOdobrioContainingIgnoreCase(jmbg, ime, prezime, adresa, telefon, email, pib, fax, odobrio), HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "FILTER_LEGAL")
	@RequestMapping(
			value = "/filtrirajPravnaLicaZaNaseljenoMesto/{jmbg}/{ime}/{prezime}/{adresa}/{telefon}/{email}/{pib}/{fax}/{odobrio}/{id}/{idA}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<PravnoLice>> filtrirajPravnaLicaZaNaseljenoMesto(@PathVariable("jmbg") String jmbg,
			@PathVariable("ime") String ime, @PathVariable("prezime") String prezime, @PathVariable("adresa") String adresa, @PathVariable("telefon") String telefon, @PathVariable("email") String email, @PathVariable("pib") String pib, @PathVariable("fax") String fax, @PathVariable("odobrio") String odobrio, @PathVariable("id") Long id, @PathVariable("idA") Long idA, @Context HttpServletRequest request) throws Exception {
		
		NaseljenoMesto naseljenoMesto = repNM.findOne(id);
		Activity activity = repA.findOne(idA);
		
		User u = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());
		
		return new ResponseEntity<Collection<PravnoLice>>( repository.findByJmbgContainingIgnoreCaseOrImeContainingIgnoreCaseOrPrezimeContainingIgnoreCaseOrAdresaContainingIgnoreCaseOrTelefonContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPibContainingIgnoreCaseOrFaxContainingIgnoreCaseOrOdobrioContainingIgnoreCaseOrNaseljenoMestoOrActivityAndBank(jmbg, ime, prezime, adresa, telefon, email, pib, fax, odobrio, naseljenoMesto, activity, bank), HttpStatus.OK);
	}
}
