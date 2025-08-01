package francesco.U5_W19_D5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import francesco.U5_W19_D5.model.User;
import francesco.U5_W19_D5.security.JwtUtil;
import francesco.U5_W19_D5.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        Optional<User> existing = userService.findByUsername(user.getUsername());
        if (existing.isPresent()) {
            return "Username già usato";
        }

        if (user.getRole() == null || (!user.getRole().equals("USER") && !user.getRole().equals("ORGANIZER"))) {
            user.setRole("USER");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "Registrazione completata";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Optional<User> found = userService.findByUsername(user.getUsername());
        if (found.isEmpty()) {
            return "Username non trovato";
        }

        if (passwordEncoder.matches(user.getPassword(), found.get().getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            return "Bearer " + token;
        } else {
            return "Password errata";
        }
    }
}