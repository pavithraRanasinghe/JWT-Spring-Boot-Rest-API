package lk.example.springsecurity.controller;

import lk.example.springsecurity.entity.UserEntity;
import lk.example.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody UserEntity userEntity){
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole(ROLE_USER)")
    public String getAll(Principal principal){
        String name = principal.getName();
        System.out.println(name);
        return "All thing gone be RIGHT!!!!";
    }

    @GetMapping("/view")
    public String view(Principal principal){
        System.out.println("kajsdbna");
        return "YOYO";
    }

}
