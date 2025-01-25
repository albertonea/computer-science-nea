package com.eastbarnetschool.ordermatchingengine.api.model.dto;

public class RegistrationResponseDto {
    private final String username;
    private final String password;

    public RegistrationResponseDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
