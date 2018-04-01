package io.muic.ooc.pos.Security;

import io.muic.ooc.pos.User.UserModel;
import io.muic.ooc.pos.User.UserRepository;
import io.muic.ooc.pos.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.Arrays;
//@CrossOrigin(origins = "http://localhost:3000")
@Component
public class AuthHandler implements AuthenticationProvider {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println(username);
        // Check with Database


//        if (!username.equalsIgnoreCase("admin") || !password.equalsIgnoreCase("password")){
//            return null;
//        }
//
//        if (userService.authenticate(username, password, role)){
//            return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
//        }
        if (userService.authenticate(username, password)){

            UserModel user = userRepository.findByUsername(username);
            return new UsernamePasswordAuthenticationToken(user.getRole(), true, new ArrayList<>());

        }

        return null;


    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}