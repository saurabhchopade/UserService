package com.example.token.dto;

import com.example.token.models.Roles;
import com.example.token.models.User;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String name;
    private String email;
    private List<Roles> roles;


    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.name = user.getName();
        userDto.email = user.getEmail();
        userDto.roles = user.getRolesList();
        return userDto;
    }
}
