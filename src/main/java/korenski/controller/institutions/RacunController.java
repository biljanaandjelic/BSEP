package korenski.controller.institutions;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

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
import korenski.model.geografija.NaseljenoMesto;
import korenski.model.infrastruktura.Bank;
import korenski.model.infrastruktura.Racun;
import korenski.model.klijenti.Klijent;
import korenski.repository.institutions.BankRepository;
import korenski.repository.institutions.RacunRepository;
import korenski.repository.klijenti.KlijentRepository;

@Controller
public class RacunController {
	
	@Autowired
	RacunRepository repository;
	
	@Autowired
	KlijentRepository klijentRepository;
	
	@Autowired
	BankRepository bankRepository;
	
	@RequestMapping(
			value = "/noviRacun",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Racun> noviRacun(@RequestBody Klijent klijent, @Context HttpServletRequest request) throws Exception {
		Racun racun = new Racun();
		
		User u = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());
		
		String brojRacuna = generateBrojRacuna(u.getBank().getCode(), getRacunBase());
		
		racun.setBrojRacuna(brojRacuna);
		racun.setKlijent(klijent);
		racun.setDatumOtvaranja(new Date());
		racun.setStatus(true);
		racun.setBank(bank);
	
		return new ResponseEntity<Racun>(repository.save(racun), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/filtrirajRacune",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Racun>> filtrirajRacune(@RequestBody RacunSearchDTO dto, @Context HttpServletRequest request) throws Exception {
		
		User u = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());
		
		return new ResponseEntity<Collection<Racun>>(repository.findBySearch(dto.status, dto.datumOtvaranjaOd, dto.datumOtvaranjaDo, dto.ime, dto.prezime, bank.getId()), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/sviRacuni",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Racun>> sviRacuni(@Context HttpServletRequest request) throws Exception {
		User u = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());
		
		return new ResponseEntity<Collection<Racun>>( repository.findByBank(bank), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/nadjiRacune/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Racun>> nadjiRacune(@PathVariable("id") Long id, @Context HttpServletRequest request) throws Exception {
		
		Klijent klijent = klijentRepository.findOne(id);
		
		User u = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());
		
		return new ResponseEntity<Collection<Racun>>( repository.findByKlijentAndBank(klijent, bank), HttpStatus.OK);
	}
	
	private String getRacunBase() {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 13) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
	
	private String generateBrojRacuna(String bankCode, String racunBase){
		
		BigInteger bigInt = new BigInteger(bankCode + racunBase);
		
		BigInteger checksum = new BigInteger("98").subtract(bigInt.multiply(new BigInteger("100")).remainder(new BigInteger("97")));
		
		return bankCode + "-" + racunBase + "-" + checksum;
	}
	
}
