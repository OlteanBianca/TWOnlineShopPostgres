package SpringBoot.Security.DBServices;

import SpringBoot.Models.ERole;
import SpringBoot.Models.Request;
import SpringBoot.Models.Role;
import SpringBoot.Models.User;
import SpringBoot.Repository.RoleRepository;
import SpringBoot.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;


    public User getUserByUsername(String username) {
        var addedUser = userRepository.findByUsername(username);
        return addedUser.orElse(null);
    }

    public Role getUserRole(String role) {
        Role userRole;

        switch (role) {
            case "admin" -> userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            case "admin_bb" -> userRole = roleRepository.findByName(ERole.ROLE_ADMIN_BB)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            case "admin_bc" -> userRole = roleRepository.findByName(ERole.ROLE_ADMIN_BC)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            default -> userRole = roleRepository.findByName(ERole.ROLE_CLIENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        }
        return userRole;
    }

    public User addNewUser(Request userForm) {
        User user = new User(userForm.getUsername(), userForm.getEmail(), encoder.encode(userForm.getPassword()));

        if (userForm.getRole() != null) {
            user.setRole(userForm.getRole());
        } else {
            user.setRole(getUserRole(user.getRoleName()));
        }
        userRepository.save(user);

        return getUserByUsername(user.getUsername());
    }

    public boolean usernameAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailAlreadyExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public void updatePassword(Long id, String password) {
        var user = userRepository.findById(id);
        if(user.isPresent()){
            User updateUser = user.get();
            updateUser.setPassword(encoder.encode(password));
            userRepository.save(updateUser);
        }
    }
}
