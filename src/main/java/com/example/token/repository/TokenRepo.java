package com.example.token.repository;

import com.example.token.models.Token;
import com.example.token.models.User;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;


@Repository
public interface TokenRepo extends JpaRepository<Token,Long>
{
    Token save(Token token); //USERT
   Optional<Token> findByValue(String token);
    Optional<Token> findByValueAndActiveAndExpiryAfter(String value, Boolean active, Date expiry);


}
