package korenski.isprobavanjeSoap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import io.spring.guides.gs_producing_web_service2.GetNalogRequest;
import io.spring.guides.gs_producing_web_service2.GetNalogResponse;
import io.spring.guides.gs_producing_web_service2.NalogZaPrenos;
import io.spring.guides.gs_producing_web_service2.ObjectFactory;

@Component
public class NalogKlijent {

	 @Autowired
	  private WebServiceTemplate webServiceTemplate;

	  public NalogZaPrenos nadji() {

		  System.out.println("TRAZIM!");
		  
		  GetNalogRequest request = new GetNalogRequest();
		  request.setPrvi("Drugi");
		  
	    ObjectFactory factory = new ObjectFactory();
	    
	    
	    
	    GetNalogResponse nalog = (GetNalogResponse) webServiceTemplate.marshalSendAndReceive(request);

	   
	    return nalog.getNalog();
	  }
}
