package lk.example.springsecurity.controller;

import lk.example.springsecurity.entity.UserEntity;
import lk.example.springsecurity.service.UserServiceImpl;
import lk.example.springsecurity.util.Utility;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/forget")
public class ForgetPasswordController {

    private UserServiceImpl userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public ForgetPasswordController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/password")
    public String processForgotPassword(HttpServletRequest request){
        String email = request.getParameter("email");
        String token = RandomString.make(45);

        try{
            userService.updateResetPassword(token,email);
            String resetPasswordLink = Utility.getSiteUrl(request)+"/forget/reset_password?token="+token;

            sendEmail(email,resetPasswordLink);

        }catch (Exception e){
            throw new UsernameNotFoundException(email);
        }
         return "";
    }

    @GetMapping("reset_password")
    public void checkResetToken(@Param(value = "token")String token){
        UserEntity userEntity = userService.get(token);
        if (userEntity!= null){
            System.out.println("User token valid");
        }else {
            System.out.println("Token invalid");
        }
    }

    @PostMapping("/reset_password")
    public void resetPassword(HttpServletRequest request){
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        UserEntity userEntity = userService.get(token);
        if (userEntity!= null){
            System.out.println("User token valid");
            userService.resetPassword(userEntity,password);
        }else {
            System.out.println("Token invalid");
        }
    }

    private void sendEmail(String email, String resetPasswordLink) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("pavithradev.xx@gmail.com","Developer support");
        helper.setTo(email);
        helper.setSubject("Here's a link to reset password");
        String content = "<p>Hello,</p>" +
                "<p>You have request to reset password</p>" +
                "<p>Click link below to reset password</p>" +
                "<p><b><a href=\""+resetPasswordLink+"\">Change my password</a></b></p>" +
                "<p>Ignore this email if you do remember your password</p>";
        helper.setText(content,true);

        mailSender.send(message);
    }
}
