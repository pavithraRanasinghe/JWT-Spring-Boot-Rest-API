package lk.example.springsecurity.oauth;

import lk.example.springsecurity.entity.AuthenticationProvider;
import lk.example.springsecurity.entity.UserEntity;
import lk.example.springsecurity.repository.UserRepository;
import lk.example.springsecurity.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessfulHandler extends SimpleUrlAuthenticationSuccessHandler {

    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private OAuth2LoginSuccessfulHandler(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        String name = oAuth2User.getName();

        UserEntity userEntity = userRepository.findByEmail(email);
        System.out.println("asd");
        if (userEntity == null){
            userService.newUserFromSuccessHandler(email,name, AuthenticationProvider.FACEBOOK);
        }else{
            userService.updateUserFromSuccessHandler(userEntity,email,name, AuthenticationProvider.FACEBOOK);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
