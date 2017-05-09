package korenski.repository.klijenti;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import korenski.model.klijenti.Klijent;
import korenski.model.geografija.NaseljenoMesto;

@Repository
public interface KlijentRepository extends CrudRepository<Klijent, Long>{
	public Klijent save(Klijent klijent);
	public Klijent findOne(Long id);
	public void delete(Long id);
	public Set<Klijent> findAll();
	public Klijent findByJmbg(String jmbg);
	public Klijent findByIme(String ime);
	public Klijent findByPrezime(String prezime);
	public Klijent findByAdresa(String adresa);
	public Klijent findByTelefon(String telefon);
	public Klijent findByEmail(String email);
	public Set<Klijent> findByNaseljenoMesto(NaseljenoMesto naseljenoMesto);
	
	public Set<Klijent> findByJmbgContainingIgnoreCaseOrImeContainingIgnoreCaseOrPrezimeContainingIgnoreCaseOrAdresaContainingIgnoreCaseOrTelefonContainingIgnoreCaseOrEmailContainingIgnoreCase(String jmbg,
			String ime, String prezime, String adresa, String telefon, String email);
	public Set<Klijent> findByJmbgContainingIgnoreCaseOrImeContainingIgnoreCaseOrPrezimeContainingIgnoreCaseOrAdresaContainingIgnoreCaseOrTelefonContainingIgnoreCaseOrEmailContainingIgnoreCaseOrNaseljenoMesto(String jmbg,
			String ime, String prezime, String adresa, String telefon, String email, NaseljenoMesto naseljenoMesto);
}
