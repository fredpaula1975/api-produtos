package br.com.loja.dto;

public class LoginResponse {

    public String token;

    public String tipo;

    public String role;

    public LoginResponse(String token, String tipo, String role) {
        this.token = token;
        this.tipo = tipo;
        this.role = role;
    }
}