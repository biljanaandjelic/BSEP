package korenski.controller.institutions;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import korenski.model.autorizacija.Role;
import korenski.model.infrastruktura.Bank;
import korenski.repository.institutions.BankRepository;

@Controller
public class BankController {

	@Autowired
	BankRepository repository;
	
	@RequestMapping(
			value = "/allBanks",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Bank>> allBanks() throws Exception {

		
		return new ResponseEntity<Collection<Bank>>( repository.findAll(), HttpStatus.OK);
	}
	
}
