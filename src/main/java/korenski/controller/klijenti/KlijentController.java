package korenski.controller.klijenti;

import java.util.Collection;

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

import korenski.model.klijenti.Klijent;
import korenski.model.geografija.NaseljenoMesto;
import korenski.repository.klijenti.KlijentRepository;
import korenski.repository.geografija.NaseljenoMestoRepository;

@Controller
public class KlijentController {

	@Autowired
	KlijentRepository repository;
	@Autowired
	NaseljenoMestoRepository repNM;
	
	@RequestMapping(
			value = "/noviKlijent",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Klijent> noviKlijent(@RequestBody Klijent klijent, @Context HttpServletRequest request) throws Exception {

	
		return new ResponseEntity<Klijent>(repository.save(klijent), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/obrisiKlijenta/{id}",
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE) //String id_string
	public ResponseEntity<Klijent> obrisiKlijenta(@PathVariable("id") Long id , @Context HttpServletRequest request) throws Exception {

		Klijent klijent = repository.findOne(id);
		repository.delete(klijent);
		return new ResponseEntity<Klijent>(klijent, HttpStatus.OK);
	}

	
	
	@RequestMapping(
			value = "/azurirajKlijenta",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Klijent> azurirajKlijenta(@RequestBody Klijent klijent, @Context HttpServletRequest request) throws Exception {
		//zar se ne koristi metoda update?
		
		Klijent klijentToModify = repository.findOne(klijent.getId());
		//ovde ima greska kod tee
		NaseljenoMesto naseljenoMesto = repNM.findOne(klijent.getNaseljenoMesto().getId());
		
		klijentToModify.setJmbg(klijent.getJmbg());
		klijentToModify.setIme(klijent.getIme());
		klijentToModify.setPrezime(klijent.getPrezime());
		klijentToModify.setAdresa(klijent.getAdresa());
		klijentToModify.setTelefon(klijent.getTelefon());
		klijentToModify.setEmail(klijent.getEmail());
		klijentToModify.setNaseljenoMesto(naseljenoMesto);
		
		return new ResponseEntity<Klijent>(repository.save(klijentToModify), HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value = "/sviKlijenti",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Klijent>> sviKlijenti() throws Exception {

		
		return new ResponseEntity<Collection<Klijent>>( repository.findAll(), HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value = "/nadjiKlijente/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Klijent>> nadjiKlijente(@PathVariable("id") Long id) throws Exception {
		
		NaseljenoMesto nm = repNM.findOne(id);
		
		return new ResponseEntity<Collection<Klijent>>( repository.findByNaseljenoMesto(nm), HttpStatus.OK);
	}
	
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
			@PathVariable("ime") String ime, @PathVariable("prezime") String prezime, @PathVariable("adresa") String adresa, @PathVariable("telefon") String telefon, @PathVariable("email") String email,  @PathVariable("id") Long id) throws Exception {
		
		NaseljenoMesto naseljenoMesto = repNM.findOne(id);
		
		
		
		return new ResponseEntity<Collection<Klijent>>( repository.findByJmbgContainingIgnoreCaseOrImeContainingIgnoreCaseOrPrezimeContainingIgnoreCaseOrAdresaContainingIgnoreCaseOrTelefonContainingIgnoreCaseOrEmailContainingIgnoreCaseOrNaseljenoMesto(jmbg, ime, prezime, adresa, telefon, email, naseljenoMesto), HttpStatus.OK);
	}
	
	
}
