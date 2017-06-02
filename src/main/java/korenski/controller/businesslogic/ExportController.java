package korenski.controller.businesslogic;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import korenski.model.infrastruktura.MedjubankarskiPrenos;
import korenski.repository.institutions.MedjubankarskiPrenosRepository;

@Controller
public class ExportController {
	@Autowired
	MedjubankarskiPrenosRepository mbPrenosRepository;
	
	/**
	 * Metoda koja vrsi eksport medjubankarskog prenosa u xml file.
	 * @param id id medjubankarskog prenosa za koji se zahtjeva kreiranje xml fajla.
	 * @param request
	 * @return status odgovra
	 * @author Biljana 
	 */
	@RequestMapping(
			value="/exportMedjubankarskiPrenos/{id}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> exportMedjubankarskiPrenos(@PathVariable("id") Long id, @Context HttpServletRequest request){
		try{
			MedjubankarskiPrenos foundMedjubankarskiPrenos=mbPrenosRepository.findOne(id);
			File file = new File("./files/xmls/medjubankarskiPrenos"+foundMedjubankarskiPrenos.getId().toString()+".xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(MedjubankarskiPrenos.class);
			Marshaller jaxbMarshaller =  jaxbContext.createMarshaller();
			
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			

			jaxbMarshaller.marshal(foundMedjubankarskiPrenos, file);
			return new ResponseEntity<String>("OK",HttpStatus.OK);
		}catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}

}
