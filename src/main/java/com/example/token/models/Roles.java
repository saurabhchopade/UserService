package com.example.token.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Roles extends BaseModel{
    private String role;
}
