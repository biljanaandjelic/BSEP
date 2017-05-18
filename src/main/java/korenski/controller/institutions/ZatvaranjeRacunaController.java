package korenski.controller.institutions;

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

import korenski.DTOs.RacunSearchDTO;
import korenski.model.autorizacija.User;
import korenski.model.infrastruktura.Bank;
import korenski.model.infrastruktura.Racun;
import korenski.model.infrastruktura.ZatvaranjeRacuna;
import korenski.repository.institutions.BankRepository;
import korenski.repository.institutions.RacunRepository;
import korenski.repository.institutions.ZatvaranjeRacunaRepository;

@Controller
public class ZatvaranjeRacunaController {

	@Autowired
	ZatvaranjeRacunaRepository repository;
	
	@Autowired 
	BankRepository bankRepository;
	@Autowired
	RacunRepository racunRepository;
	
	
	@RequestMapping(
			value = "/svaZatvaranja",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<ZatvaranjeRacuna>> svaZatvaranja(@Context HttpServletRequest request) throws Exception {
		User u = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());
		
		Collection<ZatvaranjeRacuna> zatvaranja = repository.findBySearch(bank.getId());
		
		return new ResponseEntity<Collection<ZatvaranjeRacuna>>( zatvaranja, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/filtrirajZatvaranjaPoRacunu/{racun}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ZatvaranjeRacuna> filtrirajRacune(@PathVariable("racun") Long id, @Context HttpServletRequest request) throws Exception {
		
		Racun racun = racunRepository.findOne(id);
		ZatvaranjeRacuna zatvaranje = repository.findByRacun(racun);
		
		return new ResponseEntity<ZatvaranjeRacuna>(zatvaranje, HttpStatus.OK);
	}
	
}
