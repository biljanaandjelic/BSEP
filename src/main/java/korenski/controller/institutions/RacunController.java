package korenski.controller.institutions;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
//import org.slf4j.Logger;
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

import korenski.DTOs.RacunSearchDTO;
import korenski.controller.institutions.pomocni.ZatvaranjePomocni;
import korenski.intercepting.CustomAnnotation;
import korenski.logger.AppLogger;
import korenski.model.autorizacija.User;
import korenski.model.infrastruktura.Bank;
import korenski.model.infrastruktura.Racun;
import korenski.model.infrastruktura.ZatvaranjeRacuna;
import korenski.model.klijenti.Klijent;
import korenski.repository.institutions.BankRepository;
import korenski.repository.institutions.RacunRepository;
import korenski.repository.institutions.ZatvaranjeRacunaRepository;
import korenski.repository.klijenti.KlijentRepository;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;

@Controller
public class RacunController {

	@Autowired
	RacunRepository repository;

	@Autowired
	KlijentRepository klijentRepository;

	@Autowired
	BankRepository bankRepository;

	@Autowired
	ZatvaranjeRacunaRepository zatvaranjeRepository;

	public Logger logger;
	public FileHandler fh;

	@CustomAnnotation(value = "INSERT_ACCOUNT")
	@RequestMapping(value = "/noviRacun", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Racun> noviRacun(@RequestBody Klijent klijent, @Context HttpServletRequest request)
			throws Exception {
		AppLogger appLogger=AppLogger.getInstance();
		Logger logger=appLogger.getLogger();
		logger.log(Level.FINEST,"Entering  noviRacun(klijent="+klijent.toString()+", request="+request+") ");
		//logger.debug("Entering  noviRacun(klijent="+klijent.toString()+", request="+request+")");
		Racun racun = new Racun();

		User u = (User) request.getSession().getAttribute("user");
		// User user = (User)request.getSession().getAttribute("user");
		// LOGOVI
	
		Bank bank = bankRepository.findOne(u.getBank().getId());

		String brojRacuna = generateBrojRacuna(u.getBank().getCode(), getRacunBase());

		racun.setBrojRacuna(brojRacuna);
		racun.setKlijent(klijent);
		racun.setDatumOtvaranja(new Date());
		racun.setStatus(true);
		racun.setBank(bank);
		

		try {
			Klijent foundKlijent=klijentRepository.findOne(klijent.getId());
			racun = repository.save(racun);
			foundKlijent.getRacuni().add(racun);
			klijentRepository.save(foundKlijent);
			java.lang.reflect.Method m = RacunController.class.getMethod("zatvoriRacun", ZatvaranjePomocni.class,
					HttpServletRequest.class);
			String mime = m.getAnnotation(CustomAnnotation.class).value();

			String msg = "USER " + u.getId().toString() + " " + mime + " " + brojRacuna + "\n";
			logger.info(msg);
			// logger.log(Level.INFO,msg);
			System.out.println("FINEST ISPIS");
			logger.log(Level.FINEST, "Leaving noviRacun(): " + racun + "\n");
			// logger.debug("Leaving noviRacun(): "+ racun+"\n");
			System.out.println("FINEST ISPIS");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Racun>(new Racun(new Long(-1), "Greska pri upisu u bazu!", false, null, null, null), HttpStatus.OK);
		}
		
		return new ResponseEntity<Racun>(racun, HttpStatus.OK);


	}

	@CustomAnnotation(value = "FILTER_ACCOUNT")
	@RequestMapping(value = "/filtrirajRacune", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Racun>> filtrirajRacune(@RequestBody RacunSearchDTO dto,
			@Context HttpServletRequest request) throws Exception {

		User u = (User) request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());

		return new ResponseEntity<Collection<Racun>>(repository.findBySearch(dto.status, dto.datumOtvaranjaOd,
				dto.datumOtvaranjaDo, dto.ime, dto.prezime, bank.getId()), HttpStatus.OK);
	}

	@CustomAnnotation(value = "FIND_ALL_ACCOUNT")
	@RequestMapping(value = "/sviRacuni", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Racun>> sviRacuni(@Context HttpServletRequest request) throws Exception {
		User u = (User) request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());

		return new ResponseEntity<Collection<Racun>>(repository.findByBank(bank), HttpStatus.OK);
	}

	@CustomAnnotation(value = "FIND_ACCOUNT_BY_OWNER")
	@RequestMapping(value = "/nadjiRacune/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Racun>> nadjiRacune(@PathVariable("id") Long id,
			@Context HttpServletRequest request) throws Exception {

		Klijent klijent = klijentRepository.findOne(id);

		User u = (User) request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());

		return new ResponseEntity<Collection<Racun>>(repository.findByKlijentAndBank(klijent, bank), HttpStatus.OK);
	}

	private String getRacunBase() {
		String SALTCHARS = "1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 13) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	private String generateBrojRacuna(String bankCode, String racunBase) {

		BigInteger bigInt = new BigInteger(bankCode + racunBase);

		BigInteger checksum = new BigInteger("98")
				.subtract(bigInt.multiply(new BigInteger("100")).remainder(new BigInteger("97")));

		return bankCode + "-" + racunBase + "-" + checksum;
	}

	@CustomAnnotation(value = "DEACTIVATE_ACCOUNT")
	@RequestMapping(value = "/zatvoriRacun", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Racun> zatvoriRacun(@RequestBody ZatvaranjePomocni pomocni,
			@Context HttpServletRequest request) throws Exception {
		AppLogger appLogger=AppLogger.getInstance();
		Logger logger=appLogger.getLogger();
		//Logger logger=appLogger.getLogger();
		Racun racun = repository.findOne(pomocni.getId());

		if (!racun.getStatus()) {
			return new ResponseEntity<Racun>(racun, HttpStatus.OK);
		}
		User user = (User) request.getSession().getAttribute("user");
		// LOGOVI

		if (racun.getStanje() != 0 && pomocni.getRacun().equals("")) {
			return new ResponseEntity<Racun>(
					new Racun(new Long(-1), "Stanje na racunu nije 0!Broj racuna za prenos sredstava mora biti unesen!",
							false, null, null, null),
					HttpStatus.OK);
		}

		racun.setStatus(false);

		Date current = new Date();

		racun.setDatumDeaktivacije(new java.sql.Date(current.getTime()));

		ZatvaranjeRacuna zr = new ZatvaranjeRacuna(pomocni.getRacun(), new java.sql.Date(current.getTime()), racun);

		zatvaranjeRepository.save(zr);
		java.lang.reflect.Method m = RacunController.class.getMethod("zatvoriRacun", ZatvaranjePomocni.class,
				HttpServletRequest.class);
		String mime = m.getAnnotation(CustomAnnotation.class).value();
		String msg="USER "+user.getId().toString()+" "+mime+" "+pomocni.getRacun();
		try {
			racun = repository.save(racun);
			logger.info("USER " + user.getId().toString() + " " + mime +" "+pomocni.getRacun());
			logger.log(Level.WARNING, "BILJANA");
		} catch (Exception e) {
			
			//logger.log(Level.,msg);
			//logger.log(Level);
			logger.log(Level.WARNING, msg);
	//		logger.warn(msg);
			return new ResponseEntity<Racun>(
					new Racun(new Long(-1), "Greska pri upisu u bazu!", false, current, current, null), HttpStatus.OK);
		}

		return new ResponseEntity<Racun>(racun, HttpStatus.OK);
	}

	@CustomAnnotation(value = "FIND_ONE_ACCOUNT")
	@RequestMapping(value = "/nadjiRacun/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Racun> nadjiRacun(@PathVariable("id") Long id, @Context HttpServletRequest request)
			throws Exception {

		Racun racun;
		try {
			racun = repository.findOne(id);
		} catch (Exception e) {
			return new ResponseEntity<Racun>(new Racun(new Long(-1), null, false, null, null, null), HttpStatus.OK);
		}

		if (racun == null) {
			return new ResponseEntity<Racun>(new Racun(new Long(-1), null, false, null, null, null), HttpStatus.OK);
		}

		return new ResponseEntity<Racun>(racun, HttpStatus.OK);
	}

	@RequestMapping(value = "/izvestajiRacuna", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> izvestajiRacuna(@Context HttpServletRequest request) throws Exception {
		// idiotizam
		try {

			Properties connectionProps = new Properties();
//<<<<<<< HEAD
//		    connectionProps.put("user", "test");
//		    connectionProps.put("password", "test");
//		    
//		    Connection conn = DriverManager.getConnection(
//	                   "jdbc:mysql://localhost:3306/finalni?useSSL=false",
//	                   connectionProps);
//			
//		    
//		    Bank bank = ((User)request.getSession().getAttribute("user")).getBank();
//		    HashMap<String, Object> parameters = new HashMap<String, Object>();
//		    parameters.put("id_banke", bank.getId());
//		    
//		   String jp = JasperFillManager.fillReportToFile("./files/accounts.jasper", parameters, conn);
//		    
//		//	JasperPrint jp = JasperFillManager.fillReport(
//			//	new FileInputStream("./files/test.jasper"),
//			//	new HashMap<String, Object>(), conn);
//			//eksport
//			//File pdf = File.createTempFile("output.", ".pdf");
//=======
			connectionProps.put("user", "root");
			connectionProps.put("password", "password");

			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/finalni?useSSL=false",
					connectionProps);

			Bank bank = ((User) request.getSession().getAttribute("user")).getBank();
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("id_banke", bank.getId());

			String jp = JasperFillManager.fillReportToFile("./files/accounts.jasper", parameters, conn);

			// JasperPrint jp = JasperFillManager.fillReport(
			// new FileInputStream("./files/test.jasper"),
			// new HashMap<String, Object>(), conn);
			// eksport
			// File pdf = File.createTempFile("output.", ".pdf");

			JasperExportManager.exportReportToPdfFile(jp, "./files/accounts.pdf");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
}
