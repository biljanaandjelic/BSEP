package korenski.repository.geografija;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import korenski.model.geografija.Drzava;
import korenski.model.geografija.NaseljenoMesto;

@Repository
public interface NaseljenoMestoRepository extends CrudRepository<NaseljenoMesto, Long>{
	public NaseljenoMesto save(NaseljenoMesto naseljenoMesto);
	public NaseljenoMesto findOne(Long id);
	public void delete(Long id);
	public Set<NaseljenoMesto> findAll();
	public NaseljenoMesto findByOznaka(String oznaka);
	public NaseljenoMesto findByNaziv(String naziv);
	public NaseljenoMesto findByPostanskiBroj(String postanskiBroj);
	public Set<NaseljenoMesto> findByDrzava(Drzava drzava);
	
	public Set<NaseljenoMesto> findByOznakaContainingIgnoreCaseOrNazivContainingIgnoreCaseOrPostanskiBrojContainingIgnoreCase(String oznaka,
			String naziv, String postanskiBroj);
	public Collection<NaseljenoMesto> findByOznakaContainingIgnoreCaseOrNazivContainingIgnoreCaseOrPostanskiBrojContainingIgnoreCaseAndDrzava(
			String oznaka, String naziv, String postanskiBroj, Drzava drzava);
}
