package korenski.repository.institutions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import korenski.model.infrastruktura.AnalitikaIzvoda;
import korenski.model.infrastruktura.DnevnoStanjeRacuna;

@Repository
public interface AnalitikaIzvodaRepository extends CrudRepository<AnalitikaIzvoda,Long>{
	
	public AnalitikaIzvoda save(AnalitikaIzvoda analitikaIzvoda);
	public void delete(Long id);
	public Set<AnalitikaIzvoda> findAll();
	//	@Query("select r from Racun r where r.status = ?1 or r.datumOtvaranja between ?2 and ?3 or r.klijent.ime like ?4 or r.klijent.prezime like ?5 and r.bank.id = ?6")
	//public Set<Racun> findBySearch(boolean status, Date datumOtvaranjaOd, Date datumOtvaranjaDo, String ime, String prezime, Long id);
	@Query("select a from AnalitikaIzvoda a where a.datumAnalitike between ?1 and ?2")
	public Set<AnalitikaIzvoda> findBySearch(Date firstDate, Date secondDate);
	@Query("select a from AnalitikaIzvoda a where a.smer=?1 and a.racunPrvi=?2")
	public Set<AnalitikaIzvoda> findBySearch(String direction, String firstAccount);
	
	public Set<AnalitikaIzvoda> findByDnevnoStanjeRacuna(DnevnoStanjeRacuna dnevnoStanjeRacuna);
	/*
	 * Moguce je da ce doci do izmjene u slucaju da vrijednosti koje predstavljaju datum treba da se krecu 
	 * u odredjenom opsegu kao i za iznose ili da vrijednosti tipa string se vrse na osnovu %like%
	 */
//	@Query("select a from AnalitikaIzvoda where a.datumAnalitike=?1 and a.smer=?2 and a.duznik=?3 and a.svrhaPlacanja=?4 and"
//			+ "a.primalac=?5 and a.datumNaloga=?6 and a.datumValute=?7 and a.racunPrvi=?8 and a.modelPrvi=?9 and a.pozivNaBrojPrvi=?10"
//			+ "and a.racunDrugi=?11 and a.modelDrugi=?12 and a.pozivNaBrojDrugi=?12 and a.iznos=?13 and a.sifraValute=?14 and a.hitno=?15"
//			+ "and a.dnevnoStanjeRacuna=?16")
//	public Set<AnalitikaIzvoda> findBySearch(Date datumAnalitike, String smer, String duznik, String svrhaPlacanja, 
//			String primalac, Date datumNaloga, Date datumValute,String racunPrvi, String modelPrvi, String pozivNaBrojPrvi, 
//			String racunDrugi,String modelDrugi,String pozivNaBrojDrugi, BigDecimal iznos, String sifraValute, Boolean hitno,
//			DnevnoStanjeRacuna dnevnoStanjeRacuna);
}
