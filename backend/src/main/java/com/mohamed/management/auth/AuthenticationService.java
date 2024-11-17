package com.mohamed.management.auth;

import com.mohamed.management.*;
import com.mohamed.management.exception.EmailAlreadyExistsException;
import com.mohamed.management.exception.UsernameAlreadyExistsException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CategoryService categoryService;

    public LoginResponse registerUser(UserRequest ur){
        String username = ur.getUsername();
        String email = ur.getEmail();
        String password = ur.getPassword();

        if(username.length() == 0 || email.length() == 0 || password.length() == 0){
            String message = "Please fill in all fields.";
            return new LoginResponse(null, "", message);
        }

        // invalid field (spaces, size)
        if(username.contains(" ") || email.contains(" ") || password.contains(" ")){
            String message = "Please remove any whitespaces.";
            return new LoginResponse(null, "", message);
        }

        if(!email.contains("@") || !email.contains(".")){
            String message = "Please enter a valid email.";
            return new LoginResponse(null, "", message);
        }
        // invalid password
        if(password.length() < 7){
            String message = "Password has to be at least 7 characters.";
            return new LoginResponse(null, "", message);
        }

        if(userRepository.findByUsername(username).isPresent()) {
            String message = "Username " + username + " already exists.";
            return new LoginResponse(null, "", message);
        }
        if(userRepository.findByEmail(email).isPresent()) {
            String message = "Email " + email + " already exists.";
            return new LoginResponse(null, "", message);
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(new ObjectId(), username, email, encodedPassword);
        categoryService.createStaticCategories(user.getId().toString());
        userRepository.save(user);
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        String token = tokenService.GenerateJwt(auth);
        return new LoginResponse(userRepository.findByUsername(username).get(), token, "Successful signup!");
    }

    public LoginResponse loginUser(UserRequest ur){
        String username = ur.getUsername();
        String password = ur.getPassword();

        // empty field
        if(username.length() == 0 || password.length() == 0){
            String message = "Please fill in all fields.";
            return new LoginResponse(null, "", message);
        }

        // invalid field (spaces, size)
        if(username.contains(" ") || password.contains(" ")){
            String message = "Please remove any whitespaces.";
            return new LoginResponse(null, "", message);
        }

        if(userRepository.findByUsername(username).isEmpty()) {
            if(userRepository.findByEmail(username).isEmpty()){
                String message = "No user with those credentials found.";
                return new LoginResponse(null, "", message);
            }
            username = userRepository.findByEmail(username).get().getUsername();
        }
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (Exception e){
            String message = "No user with those credentials found.";
            return new LoginResponse(null, "", message);
        }
        String token = tokenService.GenerateJwt(auth);
        return new LoginResponse(userRepository.findByUsername(username).get(), token, "Successful login!");
    }
}

