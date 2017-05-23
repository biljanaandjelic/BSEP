package korenski.controller.testni;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestTokenController {

	@RequestMapping(
			value = "/napraviToken",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> napraviToken(@Context HttpServletRequest request, HttpServletResponse response) throws Exception {

		Cookie myCookie =new Cookie("XSRF-TOKEN", "val");
		response.addCookie(myCookie);
		
		System.out.println("XSRF-TOKEN vrednost je "+ "val");
		
		return new ResponseEntity<String>( "Sve ok", HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:8090")
	@RequestMapping(
			value = "/testToken",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> testToken(@Context HttpServletRequest request) throws Exception {

		String value = request.getHeader("X-XSRF-TOKEN");
		
		System.out.println("X-XSRF-TOKEN vrednost je "+value);
		
		return new ResponseEntity<String>( "Sve ok", HttpStatus.OK);
	}
	
}
