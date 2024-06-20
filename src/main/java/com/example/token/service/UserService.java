package com.example.token.service;

import com.example.token.models.Token;
import com.example.token.models.User;
import com.example.token.repository.TokenRepo;
import com.example.token.repository.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TokenRepo tokenRepo;

    UserService(BCryptPasswordEncoder encoder, UserRepo userRepo,TokenRepo tokenRepo) {
        this.encoder = encoder;
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
    }
    public User SignUp(String email, String password, String name) {


        Optional<User> existingUserOptional = userRepo.findByEmail(email);
        if (existingUserOptional.isPresent()) {
            // User with the given email already exists
//            throw new UserAlreadyExistsException("User with email " + email + " already exists");
            // You can define UserAlreadyExistsException as a custom exception
            return existingUserOptional.get();
        }else {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setIsVerified(false);
            user.setHashedPassword(encoder.encode(password));

            return userRepo.save(user);
        }

    }

    public Token login(String email, String password) {
        Optional<User> optionalUser = userRepo.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User with email " + email + " doesn't exist");
        }

        User user = optionalUser.get();

        if (!encoder.matches(password, user.getHashedPassword())) {
            //Throw some exception.
            return null;
        }

        //Login successful, generate a Token.
        Token token = generateToken(user);
        Token savedToken = tokenRepo.save(token);

        return savedToken;
    }

    public Token logOut(String token) {
        Optional<Token> t = tokenRepo.findByValue(token);
        if (t.isEmpty()) {
            // Token Not present;
        }

        if (t.isPresent()) {
            Token t1 = t.get();
            t1.setActive(false);
            tokenRepo.save(t1);
        }
        return t.get();


    }

    public User validateToken(String token) {
        Optional<Token> t = tokenRepo.findByValueAndActiveAndExpiryAfter(token,true,new Date());

        if (t.isEmpty()) {
            return null;
        }

        return t.get().getUser();
    }
    private Token generateToken(User user) {
        Token token = new Token();
        LocalDate localDate = LocalDate.now();
        LocalDate thirtyDateLater = localDate.plusDays(30);
        Date expiry = Date.from(thirtyDateLater.atStartOfDay(ZoneId.systemDefault()).toInstant());
        token.setExpiry(expiry);
        token.setUser(user);
        token.setActive(true);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        return token;
    }
}
