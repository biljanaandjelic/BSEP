package korenski.repository.institutions;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import korenski.model.infrastruktura.Bank;
import korenski.model.infrastruktura.ZatvaranjeRacuna;

public interface ZatvaranjeRacunaRepository extends CrudRepository<ZatvaranjeRacuna, Long>{
	public ZatvaranjeRacuna save(ZatvaranjeRacuna zatvaranjeRacuna);
	public ZatvaranjeRacuna findOne(Long id);
	public void delete(Long id);
	public Set<ZatvaranjeRacuna> findAll();
	
}
