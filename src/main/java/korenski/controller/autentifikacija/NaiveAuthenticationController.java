package korenski.controller.autentifikacija;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import korenski.controller.autentifikacija.pomocneKlase.LoginObject;
import korenski.model.autorizacija.Permission;
import korenski.model.autorizacija.User;
import korenski.repository.autorizacija.RoleRepository;
import korenski.repository.autorizacija.UserRepository;
import korenski.repository.institutions.BankRepository;
import korenski.service.autorizacija.UserService;

@Controller
public class NaiveAuthenticationController {

	@Autowired
	UserRepository repository;
	
	@Autowired
	UserService userService;
	
	
	@Autowired 
	BankRepository bankRepository;
	@Autowired
	RoleRepository roleRepository;
	
	@RequestMapping(
			value = "/loginSubject",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginObject> loginUserSubject(@RequestBody LoginObject loginObject , @Context HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User userFromSession  = (User) request.getSession().getAttribute("user");
		
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
			
				boolean valid = user.getPassword().equals(loginObject.getPassword());
				
				if(!valid){
					loginObject.setId(-1);
					return new ResponseEntity<LoginObject>(loginObject, HttpStatus.OK);
				}else{
					request.getSession().setAttribute("user", user);
					
//					if(!user.isChangedFirstPassword()){
//						loginObject.setId(-5);
//						//loginObject.setUrl("http://localhost:8080/authentification/change.html");
//						request.getRequestDispatcher("/authentification/change.html").forward(request, response);
//					}
					
					String redirect = checkPermission(user);
					
					
					//request.getServletContext().getRequestDispatcher(redirect).forward(request, response);
					
					
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


	private String checkPermission(User user){
		
		Collection<Permission> permissions = user.getRole().getPermissions();
		
		for(Permission permission : permissions){
			if(permission.getName().equals("ADMINPAGE")){
				return "/adminResources/AdminPage.html";
			}else if(permission.getName().equals("EMPLOYEEPAGE")){
				return "/inicijalizacija/InitPage.html";
			}else if(permission.getName().equals("CLIENTPAGE")){
				return "/index.html";
			}
		}
		
		return "";
	}
	
	
	@RequestMapping(
			value = "/logoff",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> logoff(@Context HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User user = (User) request.getSession().getAttribute("user");
		
		request.getSession().setAttribute("user", null);
		
		return new ResponseEntity<User>(new User(), HttpStatus.OK);
		
		
	}
}
