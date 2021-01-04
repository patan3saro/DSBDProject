package dsbd.project.usermanager.data;

import org.springframework.data.repository.CrudRepository;
import user.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}
