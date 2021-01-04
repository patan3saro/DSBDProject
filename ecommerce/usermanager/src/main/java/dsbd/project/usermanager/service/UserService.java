package dsbd.project.usermanager.service;

import dsbd.project.usermanager.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.User;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Optional<User> findById(int userId){
        return userRepository.findById(userId);
    }
}
