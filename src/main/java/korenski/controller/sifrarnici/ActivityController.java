package korenski.controller.sifrarnici;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
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

import korenski.intercepting.CustomAnnotation;
import korenski.model.sifrarnici.Activity;
import korenski.repository.sifrarnici.ActivityRepository;
import korenski.service.sifrarnici.ActivityService;
import korenski.singletons.ValidatorSingleton;

@Controller
public class ActivityController {
	@Autowired
	ActivityService activityService;
	@Autowired
	ActivityRepository activityRepository;
	
	/**
	 * Metoda kreira novu djelatnost i cuva je u sifrarniku djelatnosti koji se stojati
	 * na raspolaganju sluzbenicima banke koji kreiraju klijente.
	 * @param newActivity
	 * @return createdActivity
	 */
	@CustomAnnotation(value = "INSERT_ACTIVITY")
	@RequestMapping(
			value="/activity",
			method=RequestMethod.PUT,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Activity>  createActivity(@RequestBody Activity newActivity, @Context HttpServletRequest request){
		Activity createdActivity=null;
		Activity a=validityCheck(newActivity);
		if(a!=null){
			return new ResponseEntity<Activity>(HttpStatus.BAD_REQUEST);
		}
		try{
			createdActivity=activityService.create(newActivity);
		}catch(Exception e){
			return new ResponseEntity<Activity>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Activity>(activityService.create(newActivity), HttpStatus.OK);
	}
	/**
	 * Metoda koja vrsi izmjenu vrijednosti valute kao sto su oznaka i naziv.
	 * @param changedActivity
	 * @return editedActivity
	 */
	@CustomAnnotation(value = "UPDATE_ACTIVITY")
	@RequestMapping(
			value="/activity",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Activity> editActivity(@RequestBody Activity changedActivity, @Context HttpServletRequest request){
		Activity a=validityCheck(changedActivity);
		if(a!=null){
			return new ResponseEntity<Activity>(HttpStatus.BAD_REQUEST);
		}
		try{
			changedActivity=activityService.edit(changedActivity);
		}catch (Exception e) {
			return new ResponseEntity<Activity>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Activity>(changedActivity, HttpStatus.OK);
	}
	
	/**
	 * Metoda koja pronalazi djelatnost na osnovu njegovog id.
	 * @param id
	 * @param request
	 * @return
	 */
	@CustomAnnotation(value = "FIND_ONE_ACTIVITY")
	@RequestMapping(
			value="/activity/{id}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Activity>  findActivity(@PathVariable("id") Long id, @Context HttpServletRequest request){
		Activity foundActivit=null;
		try{
			foundActivit=activityService.findActivity(id);
		}catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Activity>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Activity>(foundActivit, HttpStatus.OK);
	}
	
	/**
	 * Metoda vraca sve djelatnosti koje su zabiljezene u sifrarniku.
	 * @return
	 */
	@CustomAnnotation(value = "FIND_ALL_ACTIVITY")
	@RequestMapping(
			value="/activities",
			method=RequestMethod.GET
		
			)
	public ResponseEntity<Set<Activity>> findAcitities(){
		Set<Activity> foundActivities=null;
		try{
			foundActivities=activityService.findAll();
		}catch(Exception e){
			return new ResponseEntity<Set<Activity>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Set<Activity>>(activityService.findAll(), HttpStatus.OK);
	}
	/**
	 * Metoda koja djelatnost iz sifrarnika djelatnosti.
	 * @param id
	 * @param request
	 * @return
	 */
	@CustomAnnotation(value = "DELETE_ACTIVITY")
	@RequestMapping(
			value="/activity/{id}",
			method=RequestMethod.DELETE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Activity>  deleteActivity(@PathVariable("id") Long id, @Context HttpServletRequest request){
		Activity activityForDelete=null;
		try{
			activityForDelete=activityService.findActivity(id);
		}catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Activity>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(activityForDelete!=null){
			try{
				activityService.deleteActivity(activityForDelete.getId());
			}catch (Exception e) {
				// TODO: handle exception
				return new ResponseEntity<Activity>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<Activity>(activityForDelete, HttpStatus.OK);
	}
	/**
	 * Pretraga na osnovu sadrzaja oznake djelatnosti i naziva djelatnosti.
	 * @param code
	 * @param name
	 * @return
	 */
	@CustomAnnotation(value = "FILTER_ACTIVITY")
	@RequestMapping(
			value="/activities/{code}/{name}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Set<Activity>> findActivitiesByCodeOrName(@PathVariable("code") String code, @PathVariable("name") String name){
		Set<Activity> result=null;
		/*result.add(activityService.findActivityByCode(code));
		result.add(activityService.findActivityByName(name));*/
		try{
			if(!code.equals("undefined") && !name.equals("undefined")){
				result=activityService.findActivityByCodeAndName(code, name);
			}else if(code.equals("undefined")){
				result=activityRepository.findByNameContainingIgnoreCase(name);
			}else if(name.equals("undefined")){
				result=activityRepository.findByCodeContainingIgnoreCase(code);
			}
		}catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Set<Activity>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(result==null){
			return new ResponseEntity<Set<Activity>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Set<Activity>>(result, HttpStatus.OK);
		
	}
	
	public Activity validityCheck(Activity activity) {
		System.out.println("**************************");
		System.out.println("VALIDATOR");
		System.out.println("**************************");

		Set<ConstraintViolation<Activity>> violations = ValidatorSingleton.getInstance().getValidator()
				.validate(activity);

		if (!violations.isEmpty()) {
			Iterator iter = violations.iterator();

			ConstraintViolation<Activity> first = (ConstraintViolation<Activity>) iter.next();

			Activity a=new Activity(new Long(-1), "", first.getMessage());
			return a;
		} else {
			return null;
		}
	}
}
