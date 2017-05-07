package korenski.repository.autorizacija;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import korenski.model.autorizacija.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	public User save(User user);
	public User findOne(Long id);
	public void delete(Long id);
	public Set<User> findAll();
	public User findByUsername(String username);
	
}
