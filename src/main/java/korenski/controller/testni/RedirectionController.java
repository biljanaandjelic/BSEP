package korenski.controller.testni;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import korenski.controller.autentifikacija.pomocneKlase.LoginObject;

@Controller
public class RedirectionController {

	@RequestMapping(
			value = "/redirektGetMetodom",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> redirektGetom(@Context HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.getRequestDispatcher("/adminResources/AdminPage.html").forward(request, response);
		
		
		return new ResponseEntity<String>( "Sve ok", HttpStatus.PERMANENT_REDIRECT);
	}
	
	@RequestMapping(
			value = "/redirektPostMetodom",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginObject> redirektPostom(@RequestBody LoginObject loginObject , @Context HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getRequestDispatcher("/adminResources/AdminPage.html").forward(request, response);
		
		
		return new ResponseEntity<LoginObject>( new LoginObject(), HttpStatus.PERMANENT_REDIRECT);
	}
	
}
