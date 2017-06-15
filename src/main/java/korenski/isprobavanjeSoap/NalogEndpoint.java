package korenski.isprobavanjeSoap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import io.spring.guides.gs_producing_web_service2.GetNalogRequest;
import io.spring.guides.gs_producing_web_service2.GetNalogResponse;

@Endpoint
public class NalogEndpoint {

	
	private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service2";

	private PraviNalogRepository nalogRepository;

	@Autowired
	public NalogEndpoint(PraviNalogRepository nalogRepository) {
		this.nalogRepository = nalogRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getNalogRequest")
	@ResponsePayload
	public GetNalogResponse getNalog(@RequestPayload GetNalogRequest request) {
		
		System.out.println("Iz request-a "+ request.getPrvi());
		
		GetNalogResponse response = new GetNalogResponse();
		response.setNalog(nalogRepository.findNalog(request.getPrvi()));

		return response;
	}
}