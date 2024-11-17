package com.mohamed.management;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Home {

    @GetMapping("/")
    public String home(){
        return "Home";
    }
}
