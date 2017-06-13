package korenski.controller.autorizacija;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import korenski.controller.autentifikacija.AuthenticationController;
import korenski.controller.autentifikacija.pomocneKlase.LoginObject;
import korenski.controller.autentifikacija.pomocneKlase.PasswordChanging;
import korenski.intercepting.CustomAnnotation;
import korenski.model.autorizacija.Role;
import korenski.model.autorizacija.User;
import korenski.model.infrastruktura.Bank;
import korenski.model.klijenti.Employee;
import korenski.model.klijenti.PravnoLice;
import korenski.repository.autorizacija.RoleRepository;
import korenski.repository.autorizacija.UserRepository;
import korenski.repository.institutions.BankRepository;
import korenski.repository.klijenti.EmployeeRepository;
import korenski.repository.klijenti.PravnoLiceRepository;
import korenski.service.autorizacija.UserService;
import korenski.singletons.ValidatorSingleton;

@Controller
public class UserController {


	@Autowired
	UserRepository repository;
	@Autowired
	UserService userService;
	@Autowired
	BankRepository bankRepository;
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	PravnoLiceRepository pravnoRepository;
	@Autowired
	RoleRepository roleRepository;
	
	@CustomAnnotation(value = "INSERT_USER")
	@RequestMapping(
			value = "/newUser",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> newUser(@RequestBody User user , @Context HttpServletRequest request) throws Exception {
		User userfromsession = (User) request.getSession().getAttribute("user");
		Logger logger=LoggerFactory.getLogger(UserController.class);
		java.lang.reflect.Method m =UserController.class.getMethod("newUser",User.class, 
				HttpServletRequest.class);
		String mime = m.getAnnotation(CustomAnnotation.class).value();

		if(!userService.checkInput(user)){
			logger.warn("{} greska: nevalidni paterni inputa",mime);
			return new ResponseEntity<User>(new User(new Long(-1), null, null, null, null, null, null, null), HttpStatus.OK);
		}
		
		String pass = userService.generatePassword();
		
		System.out.println("============================================Generated pass " + pass +" size : "+pass.length());
		
		User sessionUser = (User) request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(sessionUser.getBank().getId());
		
		User rle;
		try {
			
			
			
			
			user.setBank(bank);
			Employee employee = employeeRepository.findOne(user.getSubject().getId());
			user.setSubject(employee);
			Date current = new Date();
			
			user.setCreationTime(new java.sql.Date(current.getTime()));
			
			User validity = validityCheck(user);
			if(validity != null){
				logger.warn("{} greska: nevalidni paterni inputa",mime);
				return new ResponseEntity<User>(validity, HttpStatus.OK);
				
			}
			

			userService.sendPassToUser(pass, user);
			user = userService.handleThePassword(user, pass);
			
			
			rle = repository.save(user);
			logger.info("User {} {} employee profile {} ",sessionUser.getId(), mime , user.getId());
		} catch (Exception e) {
			e.printStackTrace();
			rle = new User(new Long(-1), "Greska pri upisu u bazu!", null, null, null, null, null, null);
		}
	
		return new ResponseEntity<User>(rle, HttpStatus.OK);
	}
	
	
	@CustomAnnotation(value = "INSERT_USER")
	@RequestMapping(
			value = "/newLegalUser",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> newLegalUser(@RequestBody User user , @Context HttpServletRequest request) throws Exception {
		User userfromsession = (User) request.getSession().getAttribute("user");
		Logger logger=LoggerFactory.getLogger(UserController.class);
		java.lang.reflect.Method m =UserController.class.getMethod("newLegalUser",User.class, 
				HttpServletRequest.class);
		String mime = m.getAnnotation(CustomAnnotation.class).value();

		if(!userService.checkInput(user)){
			logger.warn("{} greska: nevalidni paterni inputa",mime);
			return new ResponseEntity<User>(new User(new Long(-1), null, null, null, null, null, null, null), HttpStatus.OK);
		}
		
		String pass = userService.generatePassword();
		
		System.out.println("============================================Generated pass " + pass +" size : "+pass.length());
		
		User sessionUser = (User) request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(sessionUser.getBank().getId());
		
		User rle;
		try {
			
			
			
			
			user.setBank(bank);
			PravnoLice pravno = pravnoRepository.findOne(user.getSubject().getId());
			user.setSubject(pravno);
			Date current = new Date();
			
			user.setCreationTime(new java.sql.Date(current.getTime()));
			
			User validity = validityCheck(user);
			if(validity != null){
				logger.warn("{} greska: nevalidni paterni inputa",mime);
				return new ResponseEntity<User>(validity, HttpStatus.OK);
			}
			
			userService.sendPassToUser(pass, user);
			user = userService.handleThePassword(user, pass);
			
			
			
			logger.info("User {} {} legal profile {} ",sessionUser.getId(), mime , user.getId());
			rle = repository.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			rle = new User(new Long(-1), "Greska pri upisu u bazu!", null, null, null, null, null, null);
		}
	
		return new ResponseEntity<User>(rle, HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value = "/special/newUser",
			method = RequestMethod.GET,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> newUser2(@Context HttpServletRequest request) throws Exception {
		
		User userfromsession = (User) request.getSession().getAttribute("user");
		Logger logger=LoggerFactory.getLogger(UserController.class);
		java.lang.reflect.Method m =UserController.class.getMethod("newUser2",
				HttpServletRequest.class);
	//	String mime = m.getAnnotation(CustomAnnotation.class).value();
//		if(!userService.checkInput(user)){
//			return new ResponseEntity<User>(new User(new Long(-1), null, null, null, null, null, null, null), HttpStatus.OK);
//		}
//		
		String pass = userService.generatePassword();
		
		System.out.println("============================================Generated pass for adminPoslovne is " + pass +" size : "+pass.length());
		
		//User sessionUser = (User) request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(new Long(1));
		
		User user = new User();
		user.setUsername("adminPoslovne");
		user.setChangedFirstPassword(false);
		user.setRole(roleRepository.findOne(new Long(1)));
		user.setEmail("adminPoslovne@kkk.kkk");
		
		User rle;
		try {
			
//			User validity = validityCheck(user);
//			if(validity != null){
//				logger.warn("INSERT_INITAL_ADMIN greska: nevalidni paterni inputa");
//				return new ResponseEntity<User>(validity, HttpStatus.OK);
//			}
			
			user = userService.handleThePassword(user, pass);
			
			
			user.setBank(bank);
			Employee employee = employeeRepository.findOne(new Long(11));
			user.setSubject(employee);
			Date current = new Date();
			
			user.setCreationTime(new java.sql.Date(current.getTime()));
		
			
			rle = repository.save(user);
			logger.info("User  INSERT_INITAL_ADMIN admin profile {} ", user.getId());
		} catch (Exception e) {
			e.printStackTrace();
			rle = new User(new Long(-1), "Greska pri upisu u bazu!", null, null, null, null, null, null);
		}
	
		napraviKorisnike(bank, logger);
		
		return new ResponseEntity<User>(rle, HttpStatus.OK);
	}
	
	
//	@CustomAnnotation(value = "INSERT_USER")
//	@RequestMapping(
//			value = "/newUser",
//			method = RequestMethod.POST,
//			consumes = MediaType.APPLICATION_JSON_VALUE,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<User> newUser(@RequestBody User user , @Context HttpServletRequest request) throws Exception {
//		
//
//		
//		
//		
//		User sessionUser = (User) request.getSession().getAttribute("user");
//		Bank bank = bankRepository.findOne(sessionUser.getBank().getId());
//		
//		User rle;
//		try {
//			user.setBank(bank);
//			Employee employee = employeeRepository.findOne(user.getSubject().getId());
//			user.setSubject(employee);
//			Date current = new Date();
//			user.setPassword("password");
//			user.setCreationTime(new java.sql.Date(current.getTime()));
//			
//			User validity = validityCheck(user);
//			if(validity != null){
//				return new ResponseEntity<User>(validity, HttpStatus.OK);
//			}
//			
//			rle = repository.save(user);
//		} catch (Exception e) {
//			e.printStackTrace();
//			rle = new User("Greska pri upisu u bazu!", null, null, null, null, null, null);
//		}
//	
//		return new ResponseEntity<User>(rle, HttpStatus.OK);
//	}
	
	@CustomAnnotation(value = "DELETE_USER")
	@RequestMapping(
			value = "/deleteUser/{id}",
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<User> deleteUser(@PathVariable("id") Long id , @Context HttpServletRequest request) throws Exception {
		User userfromsession = (User) request.getSession().getAttribute("user");
		Logger logger=LoggerFactory.getLogger(UserController.class);
		java.lang.reflect.Method m =UserController.class.getMethod("deleteUserr",Long.class, 
				HttpServletRequest.class);
		String mime = m.getAnnotation(CustomAnnotation.class).value();
		User user = repository.findOne(id);
		
		try {
			repository.delete(user);
			logger.info("{} {} {}", userfromsession.getId(), mime, user.getId());
		} catch (Exception e) {
			return new ResponseEntity<User>(new User(null, null, null, null, null, null, null), HttpStatus.OK);
		}
	
		return new ResponseEntity<User>(new User(), HttpStatus.OK);
	}

	
	@CustomAnnotation(value = "UPDATE_USER")
	@RequestMapping(
			value = "/updateUser",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> updateUser(@RequestBody User user , @Context HttpServletRequest request) throws Exception {
		User userfromsession = (User) request.getSession().getAttribute("user");
		Logger logger=LoggerFactory.getLogger(UserController.class);
		java.lang.reflect.Method m =UserController.class.getMethod("updateUser",User.class, 
				HttpServletRequest.class);
		String mime = m.getAnnotation(CustomAnnotation.class).value();
		User sessionUser = (User) request.getSession().getAttribute("user");
		
		User validity = validityCheck(user);
		if(validity != null){
			logger.warn("{} greska: greska: nevalidni paterni inputa ",mime);
			return new ResponseEntity<User>(validity, HttpStatus.OK);
		}
		
		User userToModify = null;
		
		try {
			userToModify = repository.findOne(user.getId());
		} catch (Exception e) {
		
			return new ResponseEntity<User>(new User(null, null, null, null, null, null, null), HttpStatus.OK);
		}
		if(userToModify==null){
			return new ResponseEntity<User>(new User(null, null, null, null, null, null, null), HttpStatus.OK);
		}
		Bank bank = bankRepository.findOne(sessionUser.getBank().getId());
		
		userToModify.setUsername(user.getUsername());
		userToModify.setPassword(user.getPassword());
		userToModify.setEmail(user.getEmail());
		userToModify.setRole(user.getRole());
		userToModify.setBank(bank);
		
		try {
			userToModify = repository.save(userToModify);
			logger.info("{} {} ",userfromsession.getId(),mime);
		} catch (Exception e) {
			return new ResponseEntity<User>(new User("Greska pri upisu u bazu!", null, null, null, null, null, null), HttpStatus.OK);
		}

		return new ResponseEntity<User>(userToModify, HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "FIND_ALL_USER")
	@RequestMapping(
			value = "/allUsers",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<User>> allUsers(@Context HttpServletRequest request) throws Exception {

		User user = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(user.getBank().getId());
		Role role = roleRepository.findByName("LEGAL");
		
		return new ResponseEntity<Collection<User>>( repository.findAllButLegal(bank.getId(), role.getId()), HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "FIND_ALL_USER")
	@RequestMapping(
			value = "/allLegalUsers",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<User>> allLegalUsers(@Context HttpServletRequest request) throws Exception {

		
		User user = (User)request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(user.getBank().getId());
		Role role = roleRepository.findByName("LEGAL");
		
		return new ResponseEntity<Collection<User>>( repository.findByBankAndRole(bank, role), HttpStatus.OK);
	}
	
	
	@CustomAnnotation(value = "PASSWORD_CHANGE_USER")
	@RequestMapping(
			value = "/passwordChange",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> passwordChange(@RequestBody PasswordChanging data , @Context HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User userfromsession = (User) request.getSession().getAttribute("user");
		Logger logger=LoggerFactory.getLogger(UserController.class);
		java.lang.reflect.Method m =UserController.class.getMethod("passwordChange", PasswordChanging.class, 
				HttpServletRequest.class, HttpServletResponse.class);
		String mime = m.getAnnotation(CustomAnnotation.class).value();
		User user = null;
		
		if(userfromsession == null){
			String url = "";
			String scheme = request.getScheme();
			String host = request.getServerName();
			int port = request.getServerPort();
			url = url.concat(scheme).concat("://").concat(host).concat(":"+Integer.toString(port)).concat("/authentification/login.html");
			user = new User();
			user.setId(new Long(-5));
			user.setUsername(url);
			logger.warn("{} Neuspijesna promjena lozinke greska: niko nije ulogovan",mime);
			return  new ResponseEntity<User>(user, HttpStatus.OK);
		}
		
		if(!userfromsession.getUsername().equals(data.getUsername())){
			//posalji poruku da se ne podudaraju ulogovani korisnik i uneti podaci
			user = new User();
			user.setId(new Long(-2));
			logger.warn("User {} {} greska: neuspijesna promjena lozinke korisnicko ime se ne poklapa sa unesenim", userfromsession.getId(),mime);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}

		
		
		try {
			user = repository.findByUsername(data.getUsername());
			
			if(user == null){
				
				user = new User();
				user.setId(new Long(-3));
				logger.warn("User {} {} greska: ne postoji u bazi", userfromsession.getId(), mime);
				return new ResponseEntity<User>(user, HttpStatus.OK);
				
				
			}
			boolean valid = userService.authenticate(data.getPassword(), user.getPassword(), user.getSalt());
			
			if(!valid){
				user.setId(new Long(-1));
				logger.warn("User {}  {} greska: stara i unesena lozinka se ne poklapaju", user.getId(),mime);
				return new ResponseEntity<User>(user, HttpStatus.OK);
			}else{
				
				if(!proveriPassword(data.getNewPassword())){
					user.setId(new Long(-3));
					return new ResponseEntity<User>(user, HttpStatus.OK);
				}
				
				user = userService.handleThePassword(user, data.getNewPassword());
				
				if(!user.isChangedFirstPassword()){
					user.setChangedFirstPassword(true);
				}
				user = repository.save(user);
				
				
				
				String url = "";
				String scheme = request.getScheme();
				String host = request.getServerName();
				int port = request.getServerPort();
				url = url.concat(scheme).concat("://").concat(host).concat(":"+Integer.toString(port)).concat("/authentification/login.html");
				user = new User();
				user.setId(new Long(-5));
				user.setUsername(url);
				
				request.getSession().setAttribute("user", null);
				
				request.getSession().invalidate();
				logger.info("User {} {} ", user.getId(),mime);
				return new ResponseEntity<User>(user, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		
		//return new ResponseEntity<String>("Ulogovan!", HttpStatus.OK);
		
		
	}
/*	
	@RequestMapping(
			value = "/logoff",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> logoff(@Context HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User user = (User) request.getSession().getAttribute("user");
		
		request.getSession().setAttribute("user", null);
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
		
		
	}
	
	*/
	/*
	@CustomAnnotation(value = "PASSWORD_CHANGE_USER")
	@RequestMapping(
			value = "/passwordChange",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> passwordChange(@RequestBody PasswordChanging data , @Context HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User userfromsession = (User) request.getSession().getAttribute("user");
		User user = null;
		
		if(userfromsession == null){
			//redirektuj na login.html
			String url = "";
			String scheme = request.getScheme();
			String host = request.getServerName();
			int port = request.getServerPort();
			url = url.concat(scheme).concat("://").concat(host).concat(":"+Integer.toString(port)).concat("/authentification/login.html");
			user = new User();
			user.setId(new Long(-5));
			user.setUsername(url);
			return  new ResponseEntity<User>(user, HttpStatus.OK);
		}
		
		if(!userfromsession.getUsername().equals(data.getUsername())){
			//posalji poruku da se ne podudaraju ulogovani korisnik i uneti podaci
			user = new User();
			user.setId(new Long(-2));
			new ResponseEntity<User>(user, HttpStatus.OK);
		}

		
		
		try {
			user = repository.findByUsername(data.getUsername());
			
			if(!userfromsession.getPassword().equals(user.getPassword())){
				user.setId(new Long(-1));
				return new ResponseEntity<User>(user, HttpStatus.OK);
			}else{
				
				user.setChangedFirstPassword(true);
				user.setPassword(data.getNewPassword());
				user = repository.save(user);
			
				request.getSession().invalidate();
				
				String url = "";
				String scheme = request.getScheme();
				String host = request.getServerName();
				int port = request.getServerPort();
				url = url.concat(scheme).concat("://").concat(host).concat(":"+Integer.toString(port)).concat("/authentification/login.html");
				user = new User();
				user.setId(new Long(-5));
				user.setUsername(url);
				
				return new ResponseEntity<User>(user, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		
		//return new ResponseEntity<String>("Ulogovan!", HttpStatus.OK);
		
		
	}
	
	*/
	public User validityCheck(User user){
		Set<ConstraintViolation<User>> violations = ValidatorSingleton.getInstance().getValidator().validate(user);
		
		if(!violations.isEmpty()){
			Iterator iter = violations.iterator();

			ConstraintViolation<User> first = (ConstraintViolation<User>) iter.next();
			User u = new User(new Long(-1), first.getMessage());
			return u;
		}else{
			return null;
		}
	}
	
	
	public void napraviKorisnike(Bank bank, Logger logger){
		User user;
		for(int i=2; i < 6; i++){
			
			String ime = "";
			
			switch (i) {
			case 2:
				ime = "menadzer";
				break;
			case 3:
				ime = "salterusa";
				break;
			case 4:
				ime = "pravnoLice";
				break;
			case 5:
				ime = "adminCentralne";
				break;

			default:
				break;
			}
		
			String pass = userService.generatePassword();
			
			System.out.println("============================================Generated pass for "+ ime +" is " + pass +" size : "+pass.length());
			
			
			
			user = new User();
			user.setUsername(ime);
			user.setChangedFirstPassword(true);
			user.setRole(roleRepository.findOne(new Long(i)));
			user.setEmail(ime.concat("@kkk.kkk"));
			
			User rle;
			try {
				
				
				user = userService.handleThePassword(user, pass);
				
				
				user.setBank(bank);
				Employee employee;
				PravnoLice pl;
				if(i!=4){
					employee = employeeRepository.findOne(new Long(3+i));
					user.setSubject(employee);
				}else{
					pl = pravnoRepository.findOne(new Long(17));
					user.setSubject(pl);
				}
				
				Date current = new Date();
				
				user.setCreationTime(new java.sql.Date(current.getTime()));
				
				rle = repository.save(user);
				logger.info("User  INSERT_INITAL_ADMIN admin profile {} ", user.getId());
			} catch (Exception e) {
				e.printStackTrace();
				rle = new User(new Long(-1), "Greska pri upisu u bazu!", null, null, null, null, null, null);
			}
			
			
		}
	}
	
	public boolean proveriPassword(String newValue){
		
		String brojMin = new String("^([A-Za-z0-9]{0,7})$");
		String brojMax = new String("^([A-Za-z0-9]{26,})$");
		String samoVelika = new String("^([A-Z]{8,25})$");
		String samoMala = new String("^([a-z]{8,25})$");
		String samoCifre = new String("^([0-9]{8,25})$");
		
		String samoMalaIVelika = new String("^([a-zA-Z]{8,25})$");
		String samoMalaICifre = new String("^([a-z0-9]{8,25})$");
		String samoVelikaICifre = new String("^([0-9A-Z]{8,25})$");
		
		String uobicajeni = new String("^[A-Z]([a-z]{6,23})[0-9]$");
		
		if (newValue.matches(brojMin)) {
			
			return false;
		}else if(newValue.matches(brojMax)){
			return false;
		}else if(newValue.matches(samoVelika)){
			return false;
		}else if(newValue.matches(samoMala)){
			return false;
		}else if(newValue.matches(samoCifre)){
			return false;
		}else if(newValue.matches(samoMalaIVelika)){
			return false;
		}else if(newValue.matches(samoMalaICifre)){
			return false;
		}else if(newValue.matches(samoVelikaICifre)){
			return false;
		}
		
		
		return true;
	}
}
