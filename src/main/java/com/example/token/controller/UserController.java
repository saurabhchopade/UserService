package com.example.token.controller;


import com.example.token.dto.*;
import com.example.token.models.Token;
import com.example.token.models.User;
import com.example.token.service.UserService;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Controller
public class UserController {

    private  UserService userService;



    private RateLimiter rateLimiter;

    UserController(UserService userService,RateLimiter rateLimiter ) {
        this.userService = userService;
        this.rateLimiter = rateLimiter;
    }



    @GetMapping("/rateLimiter")
    public ResponseEntity<String> RateLimter() {
        if (rateLimiter.tryAcquire()) {
            // Proceed with handling the request
            return ResponseEntity.ok("Request successful");
        } else {
            // Rate limit exceeded
            return ResponseEntity.status(429).body("Too many requests - Rate limit exceeded");
        }
    }

    @GetMapping("/f1")
    public String validateToken() {

        return "Dummy";
    }

    @PostMapping("/signup")
    private UserDto signUp(@RequestBody SignUpRequest request){
        User user = userService.SignUp(request.getEmail(), request.getPassword(), request.getName());

        return UserDto.from(user);
    }


    @PostMapping("/login")
    private LoginResponseDto login(@RequestBody LoginRequestDto request){
        Token token = userService.login(request.getUsername(), request.getPassword());
        LoginResponseDto response = new LoginResponseDto();

        if(token != null){
            response.setToken(token.getValue());
            response.setStatus("SUCCESS");
            return response;
        }


        response.setStatus("FAIL");
        return  response;
    }

    @PostMapping("/logout")
    private ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request){
        try {
            userService.logOut(request.getToken());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/validate/{token}")
    private UserDto validateToken(@PathVariable String token) {
      User u =  userService.validateToken(token);
        return  UserDto.from(u);
    }
}
