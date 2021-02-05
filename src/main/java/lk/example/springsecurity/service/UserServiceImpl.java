package lk.example.springsecurity.service;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lk.example.springsecurity.entity.AuthenticationProvider;
import lk.example.springsecurity.entity.UserEntity;
import lk.example.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserServiceImpl() {
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserName(s);
        if (userEntity == null){
            throw new UsernameNotFoundException(s);
        }
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole()));
        User user = new User(userEntity.getUserName(), userEntity.getPassword(), grantedAuthorities);
        return user;
    }

    public void newUserFromSuccessHandler(String email, String userName, AuthenticationProvider authenticationProvider){
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setEmail(email);
        userEntity.setAuthenticationProvider(authenticationProvider);
        userEntity.setRole("ADMIN");
        System.out.println("Working");
        userRepository.save(userEntity);
    }

    public void updateUserFromSuccessHandler(UserEntity userEntity,String email,String userName, AuthenticationProvider authenticationProvider){
        userEntity.setEmail(email);
        userEntity.setUserName(userName);
        userEntity.setAuthenticationProvider(authenticationProvider);
        System.out.println("asd");
        userRepository.save(userEntity);
    }
    
    public void updateResetPassword(String token,String email){
        UserEntity user = userRepository.findByEmail(email);

        if (user != null){
            user.setResetPasswordToken(token);
            userRepository.save(user);
        }else {
            throw new UsernameNotFoundException(email);
        }

    }

    public UserEntity get(String token){
        return userRepository.findByResetPasswordToken(token);
    }

    public void resetPassword(UserEntity userEntity, String password){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String newPassword = bCryptPasswordEncoder.encode(password);

        userEntity.setPassword(newPassword);
        userRepository.save(userEntity);
    }
}
