package korenski.controller.institutions;

import java.util.Collection;
import java.util.Date;

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

import korenski.DTOs.RacunSearchDTO;
import korenski.model.geografija.NaseljenoMesto;
import korenski.model.infrastruktura.Racun;
import korenski.model.klijenti.Klijent;
import korenski.repository.institutions.RacunRepository;
import korenski.repository.klijenti.KlijentRepository;

@Controller
public class RacunController {
	
	@Autowired
	RacunRepository repository;
	
	@Autowired
	KlijentRepository klijentRepository;
	
	@RequestMapping(
			value = "/noviRacun",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Racun> noviRacun(@RequestBody Klijent klijent, @Context HttpServletRequest request) throws Exception {
		Racun racun = new Racun();
		
		racun.setBrojRacuna("000-0000000000001-00");
		racun.setKlijent(klijent);
		racun.setDatumOtvaranja(new Date());
		racun.setStatus(true);
	
		return new ResponseEntity<Racun>(repository.save(racun), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/filtrirajRacune",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Racun>> filtrirajRacune(@RequestBody RacunSearchDTO dto, @Context HttpServletRequest request) throws Exception {
	
		return new ResponseEntity<Collection<Racun>>(repository.findBySearch(dto.status, dto.datumOtvaranjaOd, dto.datumOtvaranjaDo, dto.ime, dto.prezime), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/sviRacuni",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Racun>> sviRacuni() throws Exception {

		
		return new ResponseEntity<Collection<Racun>>( repository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/nadjiRacune/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Racun>> nadjiRacune(@PathVariable("id") Long id) throws Exception {
		
		Klijent klijent = klijentRepository.findOne(id);
		
		return new ResponseEntity<Collection<Racun>>( repository.findByKlijent(klijent), HttpStatus.OK);
	}
}
