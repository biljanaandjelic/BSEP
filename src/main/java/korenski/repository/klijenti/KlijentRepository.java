package korenski.repository.klijenti;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import korenski.model.klijenti.Klijent;
import korenski.model.geografija.NaseljenoMesto;
import korenski.model.infrastruktura.Bank;

@Repository
public interface KlijentRepository extends CrudRepository<Klijent, Long>{
	public Klijent save(Klijent klijent);
	public Klijent findOne(Long id);
	public void delete(Long id);
	public Set<Klijent> findAll();
	public Set<Klijent> findByFizickoLice(boolean fizickoLice);
	public Set<Klijent> findByFizickoLiceAndBank(boolean fizickoLice, Bank bank);
	public Klijent findByJmbg(String jmbg);
	public Klijent findByIme(String ime);
	public Klijent findByPrezime(String prezime);
	public Klijent findByAdresa(String adresa);
	public Klijent findByTelefon(String telefon);
	public Klijent findByEmail(String email);
	public Set<Klijent> findByNaseljenoMesto(NaseljenoMesto naseljenoMesto);
	public Set<Klijent> findByNaseljenoMestoAndFizickoLice(NaseljenoMesto naseljenoMest, boolean fizickoLice);
	public Set<Klijent>	findByNaseljenoMestoAndFizickoLiceAndBank(NaseljenoMesto naseljenoMest, boolean fizickoLice, Bank bank);
	
	public Set<Klijent> findByJmbgContainingIgnoreCaseOrImeContainingIgnoreCaseOrPrezimeContainingIgnoreCaseOrAdresaContainingIgnoreCaseOrTelefonContainingIgnoreCaseOrEmailContainingIgnoreCase(String jmbg,
			String ime, String prezime, String adresa, String telefon, String email);
	public Set<Klijent> findByJmbgContainingIgnoreCaseOrImeContainingIgnoreCaseOrPrezimeContainingIgnoreCaseOrAdresaContainingIgnoreCaseOrTelefonContainingIgnoreCaseOrEmailContainingIgnoreCaseOrNaseljenoMesto(String jmbg,
			String ime, String prezime, String adresa, String telefon, String email, NaseljenoMesto naseljenoMesto);
	public Set<Klijent> findByJmbgContainingIgnoreCaseOrImeContainingIgnoreCaseOrPrezimeContainingIgnoreCaseOrAdresaContainingIgnoreCaseOrTelefonContainingIgnoreCaseOrEmailContainingIgnoreCaseOrNaseljenoMestoAndFizickoLiceAndBank(String jmbg,
			String ime, String prezime, String adresa, String telefon, String email, NaseljenoMesto naseljenoMesto, boolean fizickoLice, Bank bank);

}
