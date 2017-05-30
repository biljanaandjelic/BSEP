package korenski.controller.institutions;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import korenski.model.infrastruktura.MedjubankarskiPrenos;
import korenski.model.infrastruktura.StavkaPrenosa;
import korenski.repository.institutions.StavkaPrenosaRepository;

@Controller
public class StavkaPrenosaController {
	@Autowired
	StavkaPrenosaRepository stavkaPrenosaRepository;
	@RequestMapping(
			value="/stavkePrenosa",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Set<StavkaPrenosa>> findStavkePrenosa(@RequestBody MedjubankarskiPrenos mBPrenos, @Context HttpServletRequest request){
		Set<StavkaPrenosa> stavkePrenosa;
		try{
			stavkePrenosa=stavkaPrenosaRepository.findStavkaPrenosaByMedjubankarskiPrenos(mBPrenos);
			
		}catch(Exception e){
			return new ResponseEntity<Set<StavkaPrenosa>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Set<StavkaPrenosa>>(stavkePrenosa,HttpStatus.OK);
	}

}
