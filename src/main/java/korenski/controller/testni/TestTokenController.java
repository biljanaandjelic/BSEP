package korenski.controller.testni;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import io.spring.guides.gs_producing_web_service2.NalogZaPrenos;
import korenski.isprobavanjeSoap.NalogKlijent;
import korenski.model.autorizacija.Role;
import korenski.model.autorizacija.User;
import korenski.model.infrastruktura.Bank;
import korenski.model.klijenti.Employee;
import korenski.repository.autorizacija.RoleRepository;
import korenski.repository.autorizacija.UserRepository;
import korenski.repository.institutions.BankRepository;
import korenski.repository.klijenti.EmployeeRepository;

@Controller
public class TestTokenController {
	
	@Autowired
	BankRepository bankRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	UserRepository userRepository;

	@Autowired
	NalogKlijent klijent;
	



	@RequestMapping(
			value = "/special/napraviToken",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> napraviToken(@Context HttpServletRequest request, HttpServletResponse response) throws Exception {

		User user = (User) request.getSession().getAttribute("user");
		
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

		
		User user = (User)request.getSession().getAttribute("user");
		
		if(user == null){
			String url = "";
			String scheme = request.getScheme();
			String host = request.getServerName();
			int port = request.getServerPort();
			url = url.concat(scheme).concat("://").concat(host).concat(":"+Integer.toString(port)).concat("/authentification/login.html");
			
			Cookie myCookie =new Cookie("OHNO", url);
			
			return  new ResponseEntity<Cookie>(myCookie, HttpStatus.OK);
		}
		
		user = userRepository.findOne(user.getId());
		
		if(user == null){
			String url = "";
			String scheme = request.getScheme();
			String host = request.getServerName();
			int port = request.getServerPort();
			url = url.concat(scheme).concat("://").concat(host).concat(":"+Integer.toString(port)).concat("/authentification/login.html");
			
			Cookie myCookie =new Cookie("OHNO", url);
			
			return  new ResponseEntity<Cookie>(myCookie, HttpStatus.OK);
		}
		
		
		
		String s1 = user.getId().toString();
		Date current = new Date();
		long d = current.getTime();
		String value = s1 + d;
		
		Cookie myCookie =new Cookie("XSRF-TOKEN", value);
		response.addCookie(myCookie);
		request.getSession().setAttribute("tokenValue", value);
		System.out.println("XSRF-TOKEN vrednost je "+ value);
		
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
	
	@RequestMapping(
			value = "/special/isprobajRest",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> isprobajRest(@Context HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("Rest 8080!");
		
		nesto();
		
		return new ResponseEntity<String>( "Sve ok", HttpStatus.OK);
	}
	
	private void nesto(){
		final String uri = "http://localhost:8080/special/newUser";
	     
		User user = new User();
		
		Bank bank = bankRepository.findOne(new Long(1));
		Role role = roleRepository.findOne(new Long(1));
		
		user.setBank(bank);
		user.setRole(role);
		user.setChangedFirstPassword(false);
		user.setEmail("mojmail@mail.com");
		user.setUsername("mojusername");
		Date current = new Date();
		
		user.setCreationTime(new java.sql.Date(current.getTime()));
		Employee e = employeeRepository.findOne(new Long(15));
		user.setSubject(e);
		
	    RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<User> result = restTemplate.postForObject( uri, user, ResponseEntity.class);
	    System.out.println("USESEN KORISNIK");
	}
	
	private KeyStore ks;
	
	//pomocna metoda za generisanje keystore-ova, potencijalno izbrisati pred rok
		@RequestMapping(value = "/special/generateKeystores", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
		public ResponseEntity<String> generateKeystores(@Context HttpServletRequest request) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, IOException {
			
			if (ks == null) {
				ks = KeyStore.getInstance("BKS", "BC");
			}
			
			ks.load(null, "prvi".toCharArray());
			ks.store(new FileOutputStream("./files/prvi.jks"), "prvi".toCharArray());
			
			ks.load(null, "drugi".toCharArray());
			ks.store(new FileOutputStream("./files/drugi.jks"), "drugi".toCharArray());
			
			ks.load(null, "treci".toCharArray());
			ks.store(new FileOutputStream("./files/treci.jks"), "treci".toCharArray());
			
			return new ResponseEntity<String>("ok", HttpStatus.OK);
		}
		
		
		@RequestMapping(
				value = "/special/posaljiZahtev",
				method = RequestMethod.GET,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<String> PosaljiZahtev(@Context HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			System.out.println("SALJEM!");
			
			NalogZaPrenos nzp = klijent.nadji();
			
			System.out.println("Stigao nalog!");
			
			JAXBContext context = JAXBContext.newInstance("io.spring.guides.gs_producing_web_service2");
			Marshaller marshaller = context.createMarshaller();
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		    marshaller.marshal(nzp, System.out);
			
			return new ResponseEntity<String>( "Sve ok", HttpStatus.OK);
		}
}
