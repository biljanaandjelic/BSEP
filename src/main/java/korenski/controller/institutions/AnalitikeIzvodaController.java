package korenski.controller.institutions;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

import korenski.controller.institutions.pomocni.AnalitikaFilter;
import korenski.intercepting.CustomAnnotation;
import korenski.model.autorizacija.User;
import korenski.model.infrastruktura.AnalitikaIzvoda;
import korenski.model.infrastruktura.Bank;
import korenski.model.infrastruktura.DnevnoStanjeRacuna;
import korenski.model.infrastruktura.Racun;
import korenski.repository.institutions.AnalitikaIzvodaRepository;
import korenski.repository.institutions.BankRepository;
import korenski.repository.institutions.DnevnoStanjeRepository;

@Controller
public class AnalitikeIzvodaController {

	@Autowired
	AnalitikaIzvodaRepository repository;
	@Autowired
	BankRepository bankRepository;
	@Autowired
	DnevnoStanjeRepository dnevnoStanjeRepository;

	@CustomAnnotation(value = "FIND_ALL_ANALYTICS")
	@RequestMapping(value = "/sveAnalitike", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<AnalitikaIzvoda>> svaDnevnaStanja(@Context HttpServletRequest request)
			throws Exception {
		User u = (User) request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());

		return new ResponseEntity<Collection<AnalitikaIzvoda>>(repository.searchByBank(bank.getId()), HttpStatus.OK);
	}

	@CustomAnnotation(value = "FILTER_ANALYTICS")
	@RequestMapping(value = "/filtrirajAnalitike", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<AnalitikaIzvoda>> filtrirajAnalitike(@RequestBody AnalitikaFilter filter,
			@Context HttpServletRequest request) throws Exception {

		User u = (User) request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(u.getBank().getId());

		if (filter.getRacunDuznika() == null) {
			filter.setRacunDuznika("");
		}

		if (filter.getModelDuznika() == null) {
			filter.setModelDuznika("");
		}

		if (filter.getPozivNaBrojDuznika() == null) {
			filter.setPozivNaBrojDuznika("");
		}

		if (filter.getRacunPoverioca() == null) {
			filter.setRacunPoverioca("");
		}

		if (filter.getModelPoverioca() == null) {
			filter.setModelPoverioca("");
		}

		if (filter.getPozivNaBrojPoverioca() == null) {
			filter.setPozivNaBrojPoverioca("");
		}

		java.sql.Date pocetakAnalitika = null;
		java.sql.Date krajAnalitika = null;

		java.sql.Date pocetakNalog = null;
		java.sql.Date krajNalog = null;

		java.sql.Date pocetakValuta = null;
		java.sql.Date krajValuta = null;

		///////////////////////////////////////////////////////////////////////// DATUM
		///////////////////////////////////////////////////////////////////////// ANALITIKE
		if (filter.getPocetakAnalitika() == null && filter.getKrajAnalitika() == null) {
			Date d = new Date(0L);
			pocetakAnalitika = new java.sql.Date(d.getTime());
			System.out.println("Pocetak " + pocetakAnalitika.toString());
			Date current = new Date();
			krajAnalitika = new java.sql.Date(current.getTime());
			System.out.println("Kraj " + krajAnalitika.toString());
		}

		if (filter.getPocetakAnalitika() == null && filter.getKrajAnalitika() != null) {
			Date d = new Date(0L);
			pocetakAnalitika = new java.sql.Date(d.getTime());
			krajAnalitika = new java.sql.Date(filter.getKrajAnalitika().getTime()+ TimeUnit.DAYS.toMillis(1));
		}

		if (filter.getPocetakAnalitika() != null && filter.getKrajAnalitika() == null) {
			pocetakAnalitika = new java.sql.Date(filter.getPocetakAnalitika().getTime());
			Date current = new Date();
			krajAnalitika = new java.sql.Date(current.getTime());
		}

		if (filter.getPocetakAnalitika() != null && filter.getKrajAnalitika() != null) {
			pocetakAnalitika = new java.sql.Date(filter.getPocetakAnalitika().getTime());

			krajAnalitika = new java.sql.Date(filter.getKrajAnalitika().getTime()+ TimeUnit.DAYS.toMillis(1));
		}
		//////////////////////////////////////////////////// DATUM ANALITIKE

		///////////////////////////////////////////////////////////////////////// DATUM
		///////////////////////////////////////////////////////////////////////// NALOG
		if (filter.getPocetakNalog() == null && filter.getKrajNalog() == null) {
			Date d = new Date(0L);
			pocetakNalog = new java.sql.Date(d.getTime());
			System.out.println("Pocetak " + pocetakNalog.toString());
			Date current = new Date();
			krajNalog = new java.sql.Date(current.getTime());
			System.out.println("Kraj " + krajNalog.toString());
		}

		if (filter.getPocetakNalog() == null && filter.getKrajNalog() != null) {
			Date d = new Date(0L);
			pocetakNalog = new java.sql.Date(d.getTime());
			krajNalog = new java.sql.Date(filter.getKrajNalog().getTime()+ TimeUnit.DAYS.toMillis(1));
		}

		if (filter.getPocetakNalog() != null && filter.getKrajNalog() == null) {
			pocetakNalog = new java.sql.Date(filter.getPocetakNalog().getTime());
			Date current = new Date();
			krajNalog = new java.sql.Date(current.getTime());
		}

		if (filter.getPocetakNalog() != null && filter.getKrajNalog() != null) {
			pocetakNalog = new java.sql.Date(filter.getPocetakNalog().getTime());

			krajNalog = new java.sql.Date(filter.getKrajNalog().getTime()+ TimeUnit.DAYS.toMillis(1));
		}
		//////////////////////////////////////////////////// DATUM NALOG

		///////////////////////////////////////////////////////////////////////// DATUM
		///////////////////////////////////////////////////////////////////////// VALUTA
		if (filter.getPocetakValuta() == null && filter.getKrajValuta() == null) {
			Date d = new Date(0L);
			pocetakValuta = new java.sql.Date(d.getTime());
			System.out.println("Pocetak " + pocetakValuta.toString());
			Date current = new Date();
			krajValuta = new java.sql.Date(current.getTime());
			System.out.println("Kraj " + krajValuta.toString());
		}

		if (filter.getPocetakValuta() == null && filter.getKrajValuta() != null) {
			Date d = new Date(0L);
			pocetakValuta = new java.sql.Date(d.getTime());
			krajValuta = new java.sql.Date(filter.getKrajValuta().getTime()+ TimeUnit.DAYS.toMillis(1));
		}

		if (filter.getPocetakValuta() != null && filter.getKrajValuta() == null) {
			pocetakValuta = new java.sql.Date(filter.getPocetakValuta().getTime());
			Date current = new Date();
			krajValuta = new java.sql.Date(current.getTime());
		}

		if (filter.getPocetakValuta() != null && filter.getKrajValuta() != null) {
			pocetakValuta = new java.sql.Date(filter.getPocetakValuta().getTime());

			krajValuta = new java.sql.Date(filter.getKrajValuta().getTime() + TimeUnit.DAYS.toMillis(1));
		}
		//////////////////////////////////////////////////// DATUM VALUTA
		
		
		String korist ;
		
		if(filter.getKorist() == null){
			korist = "";
			System.out.println("korist je null");
		}
		else if(filter.getKorist().equals("yes")){
			korist = "K";
			System.out.println("korist je K");
		}else if(filter.getKorist().equals("no")){
			korist = "T";
			System.out.println("korist je T");
		}else{
			korist = "";
			System.out.println("korist je ELSE");
		}
		
		
		boolean hitno;
		
		if(filter.getHitno() == null){
			System.out.println("hitno je null");
			return new ResponseEntity<Collection<AnalitikaIzvoda>>(
					repository.filter(bank.getId(), filter.getRacunDuznika(),
							filter.getModelDuznika(), filter.getPozivNaBrojDuznika(),
							filter.getRacunPoverioca(), filter.getModelPoverioca(),
							filter.getPozivNaBrojPoverioca(),
							pocetakAnalitika, krajAnalitika
							,pocetakNalog, krajNalog, 
							pocetakValuta, krajValuta, korist)
					,HttpStatus.OK);
		}
		else if(filter.getHitno().equals("yes")){
			hitno = true;
			System.out.println("korist je true");
			return new ResponseEntity<Collection<AnalitikaIzvoda>>(
					repository.filterUzHitno(bank.getId(), filter.getRacunDuznika(),
							filter.getModelDuznika(), filter.getPozivNaBrojDuznika(),
							filter.getRacunPoverioca(), filter.getModelPoverioca(),
							filter.getPozivNaBrojPoverioca(),
							hitno,
							pocetakAnalitika, krajAnalitika
							,pocetakNalog, krajNalog, 
							pocetakValuta, krajValuta, korist)
					,HttpStatus.OK);
		}else if(filter.getHitno().equals("no")){
			hitno = false;
			System.out.println("korist je false");
			return new ResponseEntity<Collection<AnalitikaIzvoda>>(
					repository.filterUzHitno(bank.getId(), filter.getRacunDuznika(),
							filter.getModelDuznika(), filter.getPozivNaBrojDuznika(),
							filter.getRacunPoverioca(), filter.getModelPoverioca(),
							filter.getPozivNaBrojPoverioca(),
							hitno,
							pocetakAnalitika, krajAnalitika
							,pocetakNalog, krajNalog, 
							pocetakValuta, krajValuta, korist)
					,HttpStatus.OK);
		}else{
			System.out.println("korist je ELSE");
			return new ResponseEntity<Collection<AnalitikaIzvoda>>(
					repository.filter(bank.getId(), filter.getRacunDuznika(),
							filter.getModelDuznika(), filter.getPozivNaBrojDuznika(),
							filter.getRacunPoverioca(), filter.getModelPoverioca(),
							filter.getPozivNaBrojPoverioca(),
							pocetakAnalitika, krajAnalitika
							,pocetakNalog, krajNalog, 
							pocetakValuta, krajValuta, korist)
					,HttpStatus.OK);
		}
		
		
		
	}

	@RequestMapping(
			value = "/filtrirajAnalitikePoStanju/{stanje}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<AnalitikaIzvoda>> filtrirajRacune(@PathVariable("stanje") Long id, @Context HttpServletRequest request) throws Exception {
		
		User user = (User) request.getSession().getAttribute("user");
		Bank bank = bankRepository.findOne(user.getBank().getId());
		
		DnevnoStanjeRacuna stanjeRacuna = dnevnoStanjeRepository.findOne(id);
		Collection<AnalitikaIzvoda> izvodi = repository.filterByDnevnoStanjeAndBanka(bank.getId(), stanjeRacuna.getId());
		
		return new ResponseEntity<Collection<AnalitikaIzvoda>>(izvodi, HttpStatus.OK);
	}
	
}
