package korenski.controller.autorizacija;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
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

import korenski.controller.autorizacija.dtos.LoggedAndRole;
import korenski.intercepting.CustomAnnotation;
import korenski.model.autorizacija.Permission;
import korenski.model.autorizacija.User;
import korenski.repository.autorizacija.PermissionRepository;
import korenski.repository.autorizacija.UserRepository;


@Controller
public class PermissionController {

	@Autowired
	PermissionRepository repository;
	@Autowired
	UserRepository userRepository;
	
	@CustomAnnotation(value = "INSERT_PERMISSION")
	@RequestMapping(
			value = "/newPermission",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Permission> newPermission(@RequestBody Permission permission , @Context HttpServletRequest request) throws Exception {

		
		Permission perm;
		try {
			perm = repository.save(permission);
		} catch (Exception e) {
			perm = new Permission(new Long(-1), null);
		}
	
		return new ResponseEntity<Permission>(perm, HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "DELETE_PERMISSION")
	@RequestMapping(
			value = "/deletePermission/{id}",
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE) //String id_string
	public ResponseEntity<Permission> deletePermission(@PathVariable("id") Long id , @Context HttpServletRequest request) throws Exception {

		Permission Permission = repository.findOne(id);
		
		try {
			repository.delete(Permission);
		} catch (Exception e) {
			return new ResponseEntity<Permission>(new Permission(new Long(-1), null), HttpStatus.OK);
		}
	
		return new ResponseEntity<Permission>(new Permission(), HttpStatus.OK);
	}

	
	@CustomAnnotation(value = "UPDATE_PERMISSION")
	@RequestMapping(
			value = "/updatePermission",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission , @Context HttpServletRequest request) throws Exception {
		
		
		Permission permissionToModify = null;
		
		try {
			permissionToModify = repository.findOne(permission.getId());
		} catch (Exception e) {
			return new ResponseEntity<Permission>(new Permission(new Long(-1), null), HttpStatus.OK);
		}
		
		permissionToModify.setName(permission.getName());
		
		try {
			permissionToModify = repository.save(permissionToModify);
		} catch (Exception e) {
			return new ResponseEntity<Permission>(new Permission(new Long(-1), null), HttpStatus.OK);
		}

		return new ResponseEntity<Permission>(permissionToModify, HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "FIND_ALL_PERMISSION")
	@RequestMapping(
			value = "/allPermissions",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Permission>> allPermissions() throws Exception {

		
		return new ResponseEntity<Collection<Permission>>( repository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/special/getLoggedAndRole",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoggedAndRole> loggedAndRole( @Context HttpServletRequest request) throws Exception {

		User user = (User) request.getSession().getAttribute("user");
		
		if(user != null){
			
			if(user.getId()==null){
				return new ResponseEntity<LoggedAndRole>(new LoggedAndRole(false, null), HttpStatus.OK);
			}
			
			user = userRepository.findByUsername(user.getUsername());
			
			if(user== null){
				return new ResponseEntity<LoggedAndRole>(new LoggedAndRole(false, null), HttpStatus.OK);
			}
			
			
			return new ResponseEntity<LoggedAndRole>( new LoggedAndRole(true, user.getRole().getName().substring(0, 3)), HttpStatus.OK);
		}
		
		return new ResponseEntity<LoggedAndRole>( new LoggedAndRole(false, null), HttpStatus.OK);
	}
	
	
}
