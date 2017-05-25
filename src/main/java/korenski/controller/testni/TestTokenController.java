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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestTokenController {

	@RequestMapping(
			value = "/special/napraviToken",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> napraviToken(@Context HttpServletRequest request, HttpServletResponse response) throws Exception {

		Cookie myCookie =new Cookie("XSRF-TOKEN", "val");
		response.addCookie(myCookie);
		
		System.out.println("XSRF-TOKEN vrednost je "+ "val");
		
		return new ResponseEntity<String>( "val", HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value = "/special/testToken",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> testToken(@Context HttpServletRequest request) throws Exception {

		String value = request.getHeader("X-XSRF-TOKEN");
		
		System.out.println("X-XSRF-TOKEN vrednost je "+value);
		
		return new ResponseEntity<String>( "Sve ok", HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value = "/special/getSafeToken",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cookie> getSafeToken(@Context HttpServletRequest request, HttpServletResponse response) throws Exception {

		Cookie myCookie =new Cookie("XSRF-TOKEN", "MYPRECIOUSTOKEN");
		response.addCookie(myCookie);
		request.getSession().setAttribute("tokenValue", "MYPRECIOUSTOKEN");
		System.out.println("XSRF-TOKEN vrednost je "+ "MYPRECIOUSTOKEN");
		
		return new ResponseEntity<Cookie>( myCookie, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/special/checkToken",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> checkSafeToken(@Context HttpServletRequest request, HttpServletResponse response) throws Exception {
		String value = request.getHeader("X-XSRF-TOKEN");
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++X-XSRF-TOKEN vrednost je "+value);
		
		return new ResponseEntity<String>( "Sve ok", HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/special/checkPostToken",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> checkPostSafeToken(@RequestBody String str, @Context HttpServletRequest request, HttpServletResponse response) throws Exception {
		String value = request.getHeader("X-XSRF-TOKEN");
		
		System.out.println("++++++++++++++++&&&&&&&&&&&&&&&&&&&&&&&&&&&&&++++++++X-XSRF-TOKEN vrednost je "+value);
		
		return new ResponseEntity<String>( "Sve ok", HttpStatus.OK);
	}
}
