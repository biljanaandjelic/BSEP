package korenski.controller.autorizacija;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import korenski.controller.autentifikacija.pomocneKlase.PasswordChanging;
import korenski.model.autorizacija.User;
import korenski.repository.autorizacija.UserRepository;
import korenski.service.autorizacija.UserService;

@Controller
public class UserController {


	@Autowired
	UserRepository repository;
	@Autowired
	UserService userService;
	
	@RequestMapping(
			value = "/newUser",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> newUser(@RequestBody User user , @Context HttpServletRequest request) throws Exception {
		

		if(!userService.checkInput(user)){
			return new ResponseEntity<User>(new User(new Long(-1), null, null, null, null, null, null, null), HttpStatus.OK);
		}
		
		String pass = userService.generatePassword();
		
		System.out.println("============================================Generated pass " + pass +" size : "+pass.length());
		
		
		
		User rle;
		try {
			
			//userService.sendPassToUser(pass, user);
			user = userService.handleThePassword(user, pass);
			Date current = new Date();
			
			user.setCreationTime(new java.sql.Date(current.getTime()));
			rle = repository.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			rle = new User(new Long(-1), null, null, null, null, null, null, null);
		}
	
		return new ResponseEntity<User>(rle, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/deleteUser/{id}",
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<User> deleteUser(@PathVariable("id") Long id , @Context HttpServletRequest request) throws Exception {

		User User = repository.findOne(id);
		
		try {
			repository.delete(User);
		} catch (Exception e) {
			return new ResponseEntity<User>(new User(new Long(-1), null, null, null, null, null, null, null), HttpStatus.OK);
		}
	
		return new ResponseEntity<User>(new User(), HttpStatus.OK);
	}

	
	
	@RequestMapping(
			value = "/updateUser",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> updateUser(@RequestBody User user , @Context HttpServletRequest request) throws Exception {
		
		if(!userService.checkInput(user)){
			return new ResponseEntity<User>(new User(new Long(-1), null, null, null, null, null, null, null), HttpStatus.OK);
		}
		
		User userToModify = null;
		
		try {
			userToModify = repository.findOne(user.getId());
		} catch (Exception e) {
			return new ResponseEntity<User>(new User(new Long(-1), null, null, null, null, null, null, null), HttpStatus.OK);
		}
		
		
		userToModify.setName(user.getName());
		userToModify.setSurname(user.getSurname());
		userToModify.setUsername(user.getUsername());
		userToModify.setPassword(user.getPassword());
		userToModify.setEmail(user.getEmail());
		userToModify.setRole(user.getRole());
		userToModify.setBank(user.getBank());
		
		try {
			userToModify = repository.save(userToModify);
		} catch (Exception e) {
			return new ResponseEntity<User>(new User(new Long(-1), null, null, null, null, null, null, null), HttpStatus.OK);
		}

		return new ResponseEntity<User>(userToModify, HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value = "/allUsers",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<User>> allUsers() throws Exception {

		
		return new ResponseEntity<Collection<User>>( repository.findAll(), HttpStatus.OK);
	}
	
	

	@RequestMapping(
			value = "/passwordChange",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> passwordChange(@RequestBody PasswordChanging data , @Context HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User userfromsession = (User) request.getSession().getAttribute("user");
		
		if(userfromsession == null){
			//redirektuj na login.html
		}
		
		if(!userfromsession.getUsername().equals(data.getUsername())){
			//posalji poruku da se ne podudaraju ulogovani korisnik i uneti podaci
			
		}

		User user = null;
		
		try {
			user = repository.findByUsername(data.getUsername());
			boolean valid = userService.authenticate(data.getPassword(), user.getPassword(), user.getSalt());
			
			if(!valid){
				user.setId(new Long(-1));
				return new ResponseEntity<User>(user, HttpStatus.OK);
			}else{
				
				
				user = userService.handleThePassword(user, data.getNewPassword());
				user.setChangedFirstPassword(true);
				user = repository.save(user);
				
				request.getSession().setAttribute("user", user);
				
				return new ResponseEntity<User>(user, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		
		//return new ResponseEntity<String>("Ulogovan!", HttpStatus.OK);
		
		
	}
	
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
	
	
	
}
