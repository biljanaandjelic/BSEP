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

@Controller
public class AuthenticationController {
	
	@Autowired
	UserRepository repository;
	
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
			if(user != null){
				if(user.getPassword().equals(loginObject.getPassword())){
					request.getSession().setAttribute("user", user);
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
	
		return new ResponseEntity<LoginObject>(loginObject, HttpStatus.OK);
	}

}
