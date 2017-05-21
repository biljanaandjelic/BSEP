package korenski.repository.institutions;

import java.util.Date;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import korenski.model.infrastruktura.Bank;
import korenski.model.infrastruktura.Racun;
import korenski.model.infrastruktura.ZatvaranjeRacuna;

public interface ZatvaranjeRacunaRepository extends CrudRepository<ZatvaranjeRacuna, Long>{
	public ZatvaranjeRacuna save(ZatvaranjeRacuna zatvaranjeRacuna);
	public ZatvaranjeRacuna findOne(Long id);
	public void delete(Long id);
	public Set<ZatvaranjeRacuna> findAll();
	public ZatvaranjeRacuna findByRacun(Racun racun);
	@Query("select z from ZatvaranjeRacuna z where z.racun.bank.id = ?1")
	public Set<ZatvaranjeRacuna> findBySearch(Long id);
	
	@Query("select z from ZatvaranjeRacuna z where z.racun.bank.id = ?1 "
			//+"and z in (select z1 from ZatvaranjeRacuna z1 where convert(z1.racun.id to varchar) like %?2% )")
			+"and z in (select z2 from ZatvaranjeRacuna z2 where z2.racunPrebacenihSredstava like %?2% )"
			+ "and z in (select z3 from ZatvaranjeRacuna z3 where z3.datumDeaktivacije between ?3 and ?4)")
	public Set<ZatvaranjeRacuna> filter(Long id,String racunPrebacaja, java.sql.Date pocetak, java.sql.Date kraj);
	
//	@Query("select z from ZatvaranjeRacuna z where z.racun.bank.id = ?1"
//			+"and z in (select z2 from ZatvaranjeRacuna where z2.racunPrebacenihSredstava like %?3% )" 
//			+ "and z in (select z3 from ZatvaranjeRacuna z3 where z3.datumDeaktivacije between ?4 and ?5)")
//	public Set<ZatvaranjeRacuna> filterBezRacuna(Long id, String racunPrebacaja, Date pocetak, Date kraj);
}
