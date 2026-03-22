package app.electronics.User;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer id){
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Utilizatorul nu a fost găsit"));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Utilizatorul nu a fost gasit"));
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }

    public User newUser(User user){
        return userRepository.save(user);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanExpiredUsers() {
        LocalDateTime now = LocalDateTime.now();
        List<User> expiredUsers = userRepository.findByVerificationCodeExpiresAtBefore(now);
        for (User user : expiredUsers) {
//            System.out.println("User " + user.getName() + " is expired");
            userRepository.deleteById(user.getId());
        }
    }

}
