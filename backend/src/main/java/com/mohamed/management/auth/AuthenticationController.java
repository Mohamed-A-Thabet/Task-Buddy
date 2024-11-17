package com.mohamed.management.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/*
server.port=8443
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=1234567
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=tbaselfssl
* */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> registerUser(@RequestBody UserRequest ur){
        return new ResponseEntity<>(authenticationService.registerUser(ur),HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody UserRequest ur){
        return new ResponseEntity<>(authenticationService.loginUser(ur),HttpStatus.OK);
    }
}
