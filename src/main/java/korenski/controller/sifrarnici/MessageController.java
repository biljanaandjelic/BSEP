package korenski.controller.sifrarnici;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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

import korenski.model.sifrarnici.Message;
import korenski.repository.institutions.RacunRepository;
import korenski.service.sifrarnici.MessageService;
import korenski.singletons.ValidatorSingleton;

@Controller
public class MessageController {
	@Autowired
	MessageService messageService;
	@Autowired
	RacunRepository racunRepository;

	@RequestMapping(value = "/message", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> createMessage(@RequestBody Message message, @Context HttpServletRequest request) {
//		Logger logger = LoggerFactory.getLogger(MessageController.class);
//		System.out.println("****************************************");
//		System.out.println("SLF4J");
//		String name = "lordofthejars";
//		logger.info("Hello from Bar.");
//		logger.info("BILJANA");
//		logger.debug("In bar my name is {}.", name);
//		System.out.println("****************************************");

		Message m = validityCheck(message);
		if (m != null) {
			return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
		}
		Message newMessage = null;
		Date racun = null;
		try {
			newMessage = messageService.create(message);
			racun = racunRepository.findMaxDate();
		} catch (Exception e) {

			new ResponseEntity<Message>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (racun != null) {
			

			System.out.println("Maxdate");
		}
		System.out.println("MAXDATE " + racun);
		if (message != null) {
			return new ResponseEntity<Message>(newMessage, HttpStatus.OK);
		} else {
			return new ResponseEntity<Message>(HttpStatus.NO_CONTENT);
		}

	}

	@RequestMapping(value = "/message", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> updateMessage(@RequestBody Message message, @Context HttpServletRequest request) {
		Message validator = validityCheck(message);
		if (validator != null) {
			return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
		}
		Message newMessage = null;
		try {
			newMessage = messageService.create(message);
		} catch (Exception e) {
			return new ResponseEntity<Message>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (message != null) {
			return new ResponseEntity<Message>(newMessage, HttpStatus.OK);
		} else {
			return new ResponseEntity<Message>(HttpStatus.NO_CONTENT);
		}

	}

	@RequestMapping(value = "/messages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Message>> findMessages(@Context HttpServletRequest request) {
		Set<Message> foundMessages = messageService.finaAll();
		if (foundMessages != null) {
			return new ResponseEntity<Set<Message>>(foundMessages, HttpStatus.OK);
		}
		return new ResponseEntity<Set<Message>>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/messageByCode/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<Message>> findMessageByCode(@PathVariable("code") String code,
			@Context HttpServletRequest request) {
		Set<Message> foundMessage = null;
		try {
			foundMessage = messageService.findByCode(code);
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Set<Message>>(HttpStatus.NO_CONTENT);
		}
		if (foundMessage != null) {
			return new ResponseEntity<Set<Message>>(foundMessage, HttpStatus.OK);
		} else {
			return new ResponseEntity<Set<Message>>(HttpStatus.NO_CONTENT);
		}

	}

	@RequestMapping(value = "/message/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> findMessage(@PathVariable("id") Long id, @Context HttpServletRequest request) {
		Message foundMessage = null;
		try {
			foundMessage = messageService.findMessage(id);
		} catch (Exception e) {
			return new ResponseEntity<Message>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (foundMessage != null) {
			return new ResponseEntity<Message>(foundMessage, HttpStatus.OK);
		}
		return new ResponseEntity<Message>(HttpStatus.NO_CONTENT);

	}

	@RequestMapping(value = "/message/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> deleteMessage(@PathVariable("id") Long id, @Context HttpServletRequest request) {
		Message messageForDelete = null;
		try {
			messageForDelete = messageService.findMessage(id);
		} catch (Exception e) {
			return new ResponseEntity<Message>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (messageForDelete != null) {
			messageService.delete(id);
			return new ResponseEntity<Message>(messageForDelete, HttpStatus.OK);
		}
		return new ResponseEntity<Message>(HttpStatus.NO_CONTENT);
	}

	public Message validityCheck(Message message) {
		System.out.println("**************************");
		System.out.println("VALIDATOR");
		System.out.println("**************************");

		Set<ConstraintViolation<Message>> violations = ValidatorSingleton.getInstance().getValidator()
				.validate(message);

		if (!violations.isEmpty()) {
			Iterator iter = violations.iterator();

			ConstraintViolation<Message> first = (ConstraintViolation<Message>) iter.next();

			Message m = new Message(new Long(-1), first.getMessage());
			return m;
		} else {
			return null;
		}
	}
}
