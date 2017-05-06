package korenski.controller.sifrarnici;



import java.util.HashSet;
import java.util.Set;

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

import korenski.model.sifrarnici.Valuta;
import korenski.service.sifrarnici.ValutaService;


@Controller
public class ValutaControler {

	@Autowired
	ValutaService valutaService;
	
	/**
	 * Create new currency and persist it in database.
	 * @param valuta
	 * @param request
	 * @return
	 */
	@RequestMapping(
			value="/createNewValuta",
			method=RequestMethod.POST,
			consumes= MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Valuta> createNewValuta(@RequestBody  Valuta valuta, @Context HttpServletRequest request){
		Valuta newValuta=valutaService.createValuta(valuta);
		return new ResponseEntity<Valuta>(newValuta,HttpStatus.OK);
	}
	/**
	 * Update currency and persist changes in database.
	 * @param valuta
	 * @param request
	 * @return
	 */
	@RequestMapping(
			value="/updateValuta",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Valuta> updateValuta(@RequestBody Valuta valuta, @Context HttpServletRequest request){
		Valuta editedValuta=valutaService.updateValuta(valuta);
		return new ResponseEntity<Valuta>(editedValuta,HttpStatus.OK);
	}
	
	@RequestMapping(
			value="/deleteValuta/{id}",
			method=RequestMethod.DELETE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Valuta> deleteValuta(@PathVariable("id") Long id, @Context HttpServletRequest request){
		Valuta valuta=valutaService.findValuta(id);
		if(valuta!=null){
			valutaService.deleteValuta(id);
		}
		return new ResponseEntity<Valuta>(valuta, HttpStatus.OK);
	}

	@RequestMapping(
			value="/findValue/{id}",
			method=RequestMethod.GET,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Valuta> findValuta(@RequestBody Long id, @Context HttpServletRequest request){
		Valuta valuta=valutaService.findValuta(id);
		if(valuta!=null){
			return new ResponseEntity<Valuta>(valuta,HttpStatus.OK);
		}
		return new ResponseEntity<Valuta>(HttpStatus.OK);
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
	
	@RequestMapping(
			value="/findAllValuta",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Valuta>> findAllValuta(){
		return new ResponseEntity<Set<Valuta>>(valutaService.findAllValuta(),HttpStatus.OK);
	}
	
	@RequestMapping(
			value="/valute/{code}/{name}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Valuta>> findValutaByCodeAndName(@PathVariable("code") String code,@PathVariable("name") String name ){
		/*Set<Valuta> result=new HashSet<Valuta>();
		result.add(valutaService.findValutaByCode(code));
		result.add(valutaService.findValutaByName(name));*/
		Set<Valuta> result=valutaService.findValutaByCodeAndName(code, name);
		return new ResponseEntity<Set<Valuta>>(result,HttpStatus.OK);
	}
}
