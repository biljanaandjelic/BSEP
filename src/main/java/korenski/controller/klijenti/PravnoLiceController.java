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

import korenski.model.geografija.NaseljenoMesto;
import korenski.model.klijenti.PravnoLice;
import korenski.repository.geografija.NaseljenoMestoRepository;
import korenski.repository.klijenti.PravnoLiceRepository;

@Controller
public class PravnoLiceController {
	
	@Autowired
	PravnoLiceRepository repository;
	@Autowired
	NaseljenoMestoRepository repNM;
	
	@RequestMapping(
			value = "/novoPravnoLice",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PravnoLice> novoPravnoLice(@RequestBody PravnoLice pravnoLice, @Context HttpServletRequest request) throws Exception {

	
		return new ResponseEntity<PravnoLice>(repository.save(pravnoLice), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/obrisiPravnoLice/{id}",
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE) //String id_string
	public ResponseEntity<PravnoLice> obrisiPravnoLice(@PathVariable("id") Long id , @Context HttpServletRequest request) throws Exception {

		PravnoLice pravnoLice = repository.findOne(id);
		repository.delete(pravnoLice);
		return new ResponseEntity<PravnoLice>(pravnoLice, HttpStatus.OK);
	}

	
	
	@RequestMapping(
			value = "/azurirajPravnoLice",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PravnoLice> azurirajPravnoLice(@RequestBody PravnoLice pravnoLice, @Context HttpServletRequest request) throws Exception {
		//zar se ne koristi metoda update?
		
		PravnoLice pravnoLiceToModify = repository.findOne(pravnoLice.getId());
		//ovde ima greska kod tee
		NaseljenoMesto naseljenoMesto = repNM.findOne(pravnoLice.getNaseljenoMesto().getId());
		
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
		
		return new ResponseEntity<PravnoLice>(repository.save(pravnoLiceToModify), HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value = "/svaPravnaLica",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<PravnoLice>> svaPravnaLica() throws Exception {

		
		return new ResponseEntity<Collection<PravnoLice>>( repository.findAll(), HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value = "/nadjiPravnaLica/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<PravnoLice>> nadjiPravnaLica(@PathVariable("id") Long id) throws Exception {

		NaseljenoMesto nm = repNM.findOne(id);
		
		return new ResponseEntity<Collection<PravnoLice>>( repository.findByNaseljenoMesto(nm), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/filtrirajPravnaLica/{jmbg}/{ime}/{prezime}/{adresa}/{telefon}/{email}/{pib}/{fax}/{odobrio}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<PravnoLice>> filtrirajPravnaLica(@PathVariable("jmbg") String jmbg,
			@PathVariable("ime") String ime, @PathVariable("prezime") String prezime, @PathVariable("adresa") String adresa, @PathVariable("telefon") String telefon, @PathVariable("email") String email, @PathVariable("pib") String pib, @PathVariable("fax") String fax, @PathVariable("odobrio") String odobrio) throws Exception {

		return new ResponseEntity<Collection<PravnoLice>>( repository.findByJmbgContainingIgnoreCaseOrImeContainingIgnoreCaseOrPrezimeContainingIgnoreCaseOrAdresaContainingIgnoreCaseOrTelefonContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPibContainingIgnoreCaseOrFaxContainingIgnoreCaseOrOdobrioContainingIgnoreCase(jmbg, ime, prezime, adresa, telefon, email, pib, fax, odobrio), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/filtrirajPravnaLicaZaNaseljenoMesto/{jmbg}/{ime}/{prezime}/{adresa}/{telefon}/{email}/{pib}/{fax}/{odobrio}/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<PravnoLice>> filtrirajPravnaLicaZaNaseljenoMesto(@PathVariable("jmbg") String jmbg,
			@PathVariable("ime") String ime, @PathVariable("prezime") String prezime, @PathVariable("adresa") String adresa, @PathVariable("telefon") String telefon, @PathVariable("email") String email, @PathVariable("pib") String pib, @PathVariable("fax") String fax, @PathVariable("odobrio") String odobrio, @PathVariable("id") Long id) throws Exception {
		
		NaseljenoMesto naseljenoMesto = repNM.findOne(id);
		
		return new ResponseEntity<Collection<PravnoLice>>( repository.findByJmbgContainingIgnoreCaseOrImeContainingIgnoreCaseOrPrezimeContainingIgnoreCaseOrAdresaContainingIgnoreCaseOrTelefonContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPibContainingIgnoreCaseOrFaxContainingIgnoreCaseOrOdobrioContainingIgnoreCaseOrNaseljenoMesto(jmbg, ime, prezime, adresa, telefon, email, pib, fax, odobrio, naseljenoMesto), HttpStatus.OK);
	}
}
