package korenski.controller.sifrarnici;



import java.util.Iterator;
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

import korenski.intercepting.CustomAnnotation;
import korenski.model.sifrarnici.Valuta;
import korenski.repository.sifrarnici.ValutaRepository;
import korenski.service.sifrarnici.ValutaService;
import korenski.singletons.ValidatorSingleton;


@Controller
public class ValutaControler {

	@Autowired
	ValutaService valutaService;
	@Autowired
	ValutaRepository valutaRepository;
	
	/**
	 * Create new currency and persist it in database.
	 * @param valuta
	 * @param request
	 * @return
	 */
	@CustomAnnotation(value = "INSERT_VALUTE")
	@RequestMapping(
			value="/createNewValuta",
			method=RequestMethod.POST,
			consumes= MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Valuta> createNewValuta(@RequestBody  Valuta valuta, @Context HttpServletRequest request){
		Valuta v=validityCheck(valuta);
		if(v!=null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Valuta newValuta;
		try{
			 newValuta=valutaService.createValuta(valuta);
		}catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Valuta>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Valuta>(newValuta,HttpStatus.OK);
	}
	/**
	 * Update currency and persist changes in database.
	 * @param valuta
	 * @param request
	 * @return
	 */
	@CustomAnnotation(value = "UPDATE_VALUTE")
	@RequestMapping(
			value="/updateValuta",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Valuta> updateValuta(@RequestBody Valuta valuta, @Context HttpServletRequest request){
		Valuta foundValuta=valutaService.findValuta(valuta.getId());
		if(foundValuta==null){
			return new ResponseEntity<Valuta>(HttpStatus.BAD_REQUEST);
		}
		Valuta editedValuta=valutaService.updateValuta(valuta);
		return new ResponseEntity<Valuta>(editedValuta,HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "DELETE_VALUTE")
	@RequestMapping(
			value="/deleteValuta/{id}",
			method=RequestMethod.DELETE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Valuta> deleteValuta(@PathVariable("id") Long id, @Context HttpServletRequest request){
		Valuta valuta=null;
		try{
				valuta=valutaService.findValuta(id);
		}catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Valuta>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(valuta!=null){
			try{
			valutaService.deleteValuta(id);
			}catch (Exception e) {
				// TODO: handle exception
				return new ResponseEntity<Valuta>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<Valuta>(valuta, HttpStatus.OK);
		}
		return new ResponseEntity<Valuta>(HttpStatus.NO_CONTENT);
		
	}

	
	@RequestMapping(
			value="/findValue/{id}",
			method=RequestMethod.GET,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Valuta> findValuta(@RequestBody Long id, @Context HttpServletRequest request){
	
		Valuta valuta=null;
		try{
			valuta=valutaService.findValuta(id);
		}catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Valuta>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(valuta!=null){
			return new ResponseEntity<Valuta>(valuta,HttpStatus.OK);
		}
		return new ResponseEntity<Valuta>(HttpStatus.NO_CONTENT);
	}
	@RequestMapping(
			value="/findValueByCode/{code}",
			method=RequestMethod.GET,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Valuta> findValutaByCode(@RequestBody String code, @Context HttpServletRequest request){
			Valuta valutaSet=valutaService.findValutaByCode(code);
			return new ResponseEntity<Valuta>(valutaSet,HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "FIND_ALL_VALUTE")
	@RequestMapping(
			value="/findAllValuta",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Valuta>> findAllValuta(){
		Set<Valuta> valute=null;
		try{
			valute=valutaService.findAllValuta();
		}catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Set<Valuta>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(valute!=null)
			return new ResponseEntity<Set<Valuta>>(valutaService.findAllValuta(),HttpStatus.OK);
		else
			return new ResponseEntity<Set<Valuta>>(HttpStatus.NO_CONTENT);
	}
	
	@CustomAnnotation(value = "FILTER_VALUTE")
	@RequestMapping(
			value="/valute/{code}/{name}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Valuta>> findValutaByCodeAndName(@PathVariable("code") String code,@PathVariable("name") String name ){
		
		Set<Valuta> result=null;
		try{
			if(!code.equals("undefined") && !name.equals("undefined")){
				result=valutaService.findValutaByCodeAndName(code, name);
			}else if(code.equals("undefined")){
				result=valutaRepository.findByNameContainingIgnoreCase(name);
			}else if(name.equals("undefined")){
				result=valutaRepository.findByCodeContainingIgnoreCase(code);
			}
		}catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Set<Valuta>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(result!=null)
			return new ResponseEntity<Set<Valuta>>(result,HttpStatus.OK);
		else
			return new ResponseEntity<Set<Valuta>>(HttpStatus.NO_CONTENT);
	}
	public Valuta validityCheck(Valuta valuta) {
		System.out.println("**************************");
		System.out.println("VALIDATOR");
		System.out.println("**************************");

		Set<ConstraintViolation<Valuta>> violations = ValidatorSingleton.getInstance().getValidator()
				.validate(valuta);

		if (!violations.isEmpty()) {
			Iterator iter = violations.iterator();

			ConstraintViolation<Valuta> first = (ConstraintViolation<Valuta>) iter.next();

			Valuta v=new Valuta(new Long(-1), "", first.getMessage());
			return v;
		} else {
			return null;
		}
	}
}
