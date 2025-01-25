package com.eastbarnetschool.ordermatchingengine.api.model.dto;

public class RegistrationRequestDto {
    private final String username;
    private final String password;

    public RegistrationRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
