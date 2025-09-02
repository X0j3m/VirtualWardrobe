package x0j3m.virtualwardrobe.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import x0j3m.virtualwardrobe.model.User;

public interface UserRepository extends CrudRepository<User,Long>, PagingAndSortingRepository<User, Long> {
    User findByUsername(String username);
}
