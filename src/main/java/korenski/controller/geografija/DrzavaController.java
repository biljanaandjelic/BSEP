package korenski.controller.geografija;

import java.util.ArrayList;
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

import korenski.model.geografija.Drzava;
import korenski.repository.geografija.DrzavaRepository;

@Controller
public class DrzavaController {

	@Autowired
	DrzavaRepository repository;
	
	@RequestMapping(
			value = "/novaDrzava",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Drzava> novaDrzava(@RequestBody Drzava drzava , @Context HttpServletRequest request) throws Exception {

		
		Drzava drz;
		try {
			drz = repository.save(drzava);
		} catch (Exception e) {
			drz = new Drzava(new Long(-1), null, null, null);
		}
	
		return new ResponseEntity<Drzava>(drz, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/obrisiDrzavu/{id}",
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE) //String id_string
	public ResponseEntity<Drzava> obrisiDrzavu(@PathVariable("id") Long id , @Context HttpServletRequest request) throws Exception {

		Drzava drzava = repository.findOne(id);
		
		try {
			repository.delete(drzava);
		} catch (Exception e) {
			return new ResponseEntity<Drzava>(new Drzava(new Long(-1), null, null, null), HttpStatus.OK);
		}
	
		
		return new ResponseEntity<Drzava>(new Drzava(), HttpStatus.OK);
	}

	
	
	@RequestMapping(
			value = "/azurirajDrzavu",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Drzava> azurirajDrzavu(@RequestBody Drzava drzava , @Context HttpServletRequest request) throws Exception {
		
		
		Drzava drzavaToModify = null;
		
		try {
			drzavaToModify = repository.findOne(drzava.getId());
		} catch (Exception e) {
			return new ResponseEntity<Drzava>(new Drzava(new Long(-1), null, null, null), HttpStatus.OK);
		}
		
		
		drzavaToModify.setOznaka(drzava.getOznaka());
		drzavaToModify.setNaziv(drzava.getNaziv());
		
		try {
			drzavaToModify = repository.save(drzavaToModify);
		} catch (Exception e) {
			return new ResponseEntity<Drzava>(new Drzava(new Long(-1), null, null, null), HttpStatus.OK);
		}

		return new ResponseEntity<Drzava>(drzavaToModify, HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value = "/sveDrzave",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Drzava>> sveDrzave() throws Exception {

		
		return new ResponseEntity<Collection<Drzava>>( repository.findAll(), HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value = "/filtrirajDrzave/{oznaka}/{naziv}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Drzava>> filtrirajDrzave(@PathVariable("oznaka") String oznaka,
			@PathVariable("naziv") String naziv) throws Exception {

		
		return new ResponseEntity<Collection<Drzava>>( repository.findByOznakaContainingIgnoreCaseOrNazivContainingIgnoreCase(oznaka, naziv), HttpStatus.OK);
	}
	

	
}
