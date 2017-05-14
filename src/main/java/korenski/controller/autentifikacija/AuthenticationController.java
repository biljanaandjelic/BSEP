package korenski.controller.autentifikacija;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import korenski.model.autorizacija.User;
import korenski.repository.autorizacija.UserRepository;
import korenski.service.autorizacija.UserService;

@Controller
public class AuthenticationController {
	
	@Autowired
	UserRepository repository;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(
			value = "/login",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginObject> loginUser(@RequestBody LoginObject loginObject , @Context HttpServletRequest request) throws Exception {
		
		User userFromSession  = (User) request.getSession().getAttribute("korisnik");
		
		if(userFromSession != null){
			loginObject.setId(-2);
			return new ResponseEntity<LoginObject>(loginObject, HttpStatus.OK);
		}
		
		User user = null;
		try {
			user = repository.findByUsername(loginObject.getUsername());
			
			boolean expired = userService.checkPasswordExpiration(user);
			
			if(expired){
				loginObject.setId(-4);
				return new ResponseEntity<LoginObject>(loginObject, HttpStatus.OK);
			}
			
			if(user != null){
			
				boolean valid = userService.authenticate(loginObject.getPassword(), user.getPassword(), user.getSalt());
				
				if(!valid){
					loginObject.setId(-1);
					return new ResponseEntity<LoginObject>(loginObject, HttpStatus.OK);
				}else{
					request.getSession().setAttribute("user", user);
					
					if(!user.isChangedFirstPassword()){
						loginObject.setId(-5);
						loginObject.setUrl("http://localhost:8080/authentification/change.html");
					}
					
					return new ResponseEntity<LoginObject>(loginObject, HttpStatus.OK);
				}
			}else{
				loginObject.setId(-1);
				return new ResponseEntity<LoginObject>(loginObject, HttpStatus.OK);
			}
		} catch (Exception e) {
			loginObject.setId(-3);
			return new ResponseEntity<LoginObject>(loginObject, HttpStatus.OK);
		}
	
		//return new ResponseEntity<LoginObject>(loginObject, HttpStatus.OK);
	}

}