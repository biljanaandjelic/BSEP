package korenski.repository.geografija;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import korenski.model.geografija.Drzava;

@Repository
public interface DrzavaRepository extends CrudRepository<Drzava, Long>{
	public Drzava save(Drzava drzava);
	public Drzava findOne(Long id);
	public void delete(Long id);
	public Set<Drzava> findAll();
	public Drzava findByOznaka(String oznaka);
	public Drzava findByNaziv(String naziv);
	public Set<Drzava> findByOznakaContainingIgnoreCaseOrNazivContainingIgnoreCase(String oznaka, String naziv);
}
