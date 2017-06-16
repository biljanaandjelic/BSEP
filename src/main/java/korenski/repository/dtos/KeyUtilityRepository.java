package korenski.repository.dtos;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import korenski.model.util.KeyUtility;

public interface KeyUtilityRepository  extends CrudRepository<KeyUtility, Long>{

	public KeyUtility save(KeyUtility keyUtility);
	public Set<KeyUtility> findAll();
	public KeyUtility findOne(Long id);
	public KeyUtility findByName(String name);
}
