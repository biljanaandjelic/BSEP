package korenski.repository.institutions;

import java.util.Date;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import korenski.model.infrastruktura.Bank;
import korenski.model.infrastruktura.Racun;
import korenski.model.infrastruktura.StavkaPrenosa;
import korenski.model.klijenti.Klijent;

@Repository
public interface RacunRepository extends CrudRepository<Racun, Long>{
	public Racun save(Racun racun);
	public Racun findOne(Long id);
	public void delete(Long id);
	public Set<Racun> findAll();
	public Set<Racun> findByBank(Bank bank);
	public Set<Racun> findByStatus(boolean status);
	public Set<Racun> findByDatumOtvaranjaAfter(Date datumOtvaranja);
	public Set<Racun> findByDatumOtvaranjaBefore(Date datumOtvaranja);
	public Set<Racun> findByKlijent(Klijent klijent);
	public Set<Racun> findByKlijentAndBank(Klijent klijent, Bank bank);
	@Query("select r from Racun r where r.status = ?1 or r.datumOtvaranja between ?2 and ?3 or r.klijent.ime like ?4 or r.klijent.prezime like ?5 and r.bank.id = ?6")
	public Set<Racun> findBySearch(boolean status, Date datumOtvaranjaOd, Date datumOtvaranjaDo, String ime, String prezime, Long id);
	
	public Racun findByBankAndBrojRacunaAndStatusTrue(Bank bank, String brojRacuna);
	/*
	 * Probe radi. Poziva se samo da bi vidjeli da li radi.
	 */
//	@Query("select id, broj_racuna, datum_deaktivacije, datum_otvaranja, stanje, status, bank_id, klijent_id from finalni.racun r inner join (SELECT MAX(datum_otvaranja) "
//			+ "as max_date FROM finalni.racun)a on a.max_date=r.datum_otvaranja;")
//	public Racun findByMaxDate();
	@Query("select r from Racun r where r.datumOtvaranja=?1 ")
	public Racun findByMaxDate(Date maxDatumOtvaranja);
	@Query("select max(r.datumOtvaranja) from Racun r")
	public Date findMaxDate();
}
