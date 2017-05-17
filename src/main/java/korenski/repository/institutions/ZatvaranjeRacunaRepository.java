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
	@Query("select z from ZatvaranjeRacuna z where z.racun.bank.id = ?1")
	public Set<ZatvaranjeRacuna> findBySearch(Long id);
}
