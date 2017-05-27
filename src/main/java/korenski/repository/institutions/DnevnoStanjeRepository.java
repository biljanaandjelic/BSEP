package korenski.repository.institutions;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import korenski.model.infrastruktura.DnevnoStanjeRacuna;
import korenski.model.infrastruktura.Racun;

@Repository
public interface DnevnoStanjeRepository  extends CrudRepository<DnevnoStanjeRacuna, Long >{

	public DnevnoStanjeRacuna save(DnevnoStanjeRacuna dnevnoStanje);
	public DnevnoStanjeRacuna findByDatumAndRacun(Date datum, Racun racun);
}
